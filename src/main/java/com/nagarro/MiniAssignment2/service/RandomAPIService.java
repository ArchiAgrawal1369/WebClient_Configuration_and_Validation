package com.nagarro.MiniAssignment2.service;

import com.nagarro.MiniAssignment2.model.RandomAPIUser;
import com.nagarro.MiniAssignment2.model.User;
import com.nagarro.MiniAssignment2.model.GenderizeAPI;
import com.nagarro.MiniAssignment2.model.NationalizeAPI;
import com.nagarro.MiniAssignment2.repo.*;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
public class RandomAPIService {
	
	@Autowired
	private UserRepo userRepo;

	private String randomUserApiUrl = "https://randomuser.me/api";
    private String nationalizeApiUrl = "https://api.nationalize.io";
    private String genderizeApiUrl = "https://api.genderize.io";

    private final WebClient randomUserWebClient;
    private final WebClient nationalizeWebClient;
    private final WebClient genderizeWebClient;

    public RandomAPIService(WebClient.Builder webClientBuilder) {
        this.randomUserWebClient = configureWebClient(webClientBuilder, randomUserApiUrl, 2000, 2000, 2000);
        this.nationalizeWebClient = configureWebClient(webClientBuilder, nationalizeApiUrl, 1000, 1000, 1000);
        this.genderizeWebClient = configureWebClient(webClientBuilder, genderizeApiUrl, 1000, 1000, 1000);
    }

    private WebClient configureWebClient(WebClient.Builder webClientBuilder, String baseUrl, int connectTimeout, int readTimeout, int writeTimeout) {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))
                )
                .option(ChannelOption.TCP_NODELAY, true);

        return webClientBuilder.baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .defaultHeader(HttpHeaders.CONNECTION, "close")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build();
    }

    private NationalizeAPI callNationalizeAPI(WebClient webClient, String name) {
        return webClient.get()
                .uri("/?name=" + name)
                .retrieve()
                .bodyToMono(NationalizeAPI.class)
                .block();
    }

    private GenderizeAPI callGenderizeAPI(WebClient webClient, String name) {
        return webClient.get()
                .uri("/?name=" + name)
                .retrieve()
                .bodyToMono(GenderizeAPI.class)
                .block();
    }

    public String verification(String name, String gender, String nat) {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        CompletableFuture<NationalizeAPI> nationalityFuture = CompletableFuture.supplyAsync(() ->
                callNationalizeAPI(nationalizeWebClient, name), executorService);

        CompletableFuture<GenderizeAPI> genderFuture = CompletableFuture.supplyAsync(() ->
                callGenderizeAPI(genderizeWebClient, name), executorService);

        CompletableFuture<String> resultFuture = new CompletableFuture<>();

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(nationalityFuture, genderFuture);

        combinedFuture.thenRunAsync(() -> {
            try {
                NationalizeAPI nationalityResult = nationalityFuture.get();
                GenderizeAPI genderResult = genderFuture.get();

                boolean nationExists = nationalityResult.getCountry().stream()
                        .anyMatch(country -> nat.equalsIgnoreCase(country.getCountry_id()));

              
                if (genderResult.toString().equals(gender) && nationExists) {
                    resultFuture.complete("VERIFIED");
                } else {
                    resultFuture.complete("To_BE_VERIFIED");
                }
            } catch (Exception e) {
                resultFuture.complete("To_BE_VERIFIED");
            }
        }, executorService);

        combinedFuture.join();
        executorService.shutdown();

        return resultFuture.join();
    }
      
//   public RandomAPIService(WebClient.Builder webClientBuilder) {
//	   this.webClient = webClientBuilder.build();
//   }
     
   public Mono<List<User>> getRandomUsers(int size) {
       return randomUserWebClient.get()
               .uri(randomUserApiUrl + "?results=" + size)
               .retrieve()
               .bodyToMono(RandomAPIUser.class)
               .flatMap(response -> {
                   List<RandomAPIUser.Result> results = response.getResults();
                   return Flux.fromIterable(results)
                           .flatMap(result -> {
                               RandomAPIUser.Result.Name name = result.getName();
                               RandomAPIUser.Result.Dob dob = result.getDob();
                               String status = verification(name.getFirst(), result.getGender(), result.getNat());

                               User user = new User();
                               user.setGender(result.getGender());
                               user.setName(name.getFirst() + " " + name.getLast());
                               user.setDob(dob.getDate());
                               user.setAge(dob.getAge());
                               user.setNationality(result.getNat());
                               user.setDate_created(LocalDate.now());
                               user.setDate_modified(LocalDate.now());
                               user.setVerificationStatus(status);

                               return Mono.fromSupplier(() -> userRepo.save(user));
                           })
                           .collectList();
               });
   }
    
   public Flux<User> fetchAndSaveData(int size) {
       return getRandomUsers(size)
               .flatMapMany(Flux::fromIterable);
   }
}
