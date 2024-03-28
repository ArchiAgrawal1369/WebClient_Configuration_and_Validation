package com.nagarro.MiniAssignment2.service;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nagarro.MiniAssignment2.model.User;
import com.nagarro.MiniAssignment2.model.Response;
import com.nagarro.MiniAssignment2.model.Response.PageInfo;
import com.nagarro.MiniAssignment2.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired 
	UserRepo userRepository;
	
	public Response getSortedUsers(String sortType, String sortOrder, int limit, int offset) {
		List<User> allUsers = userRepository.findAll();

		// Function Call by passing all users list
		List<User> sortedUsers = sortUsers(allUsers, sortType, sortOrder);

		// Apply pagination using limit and offset
		int total = sortedUsers.size();
		int startIndex = Math.min(offset, total);
		int endIndex = Math.min(startIndex + limit, total);

		List<User> paginatedUsers = sortedUsers.subList(startIndex, endIndex);

		PageInfo pageInfo = new PageInfo();
		pageInfo.setHasNextPage(endIndex < total);
		pageInfo.setHasPreviousPage(startIndex > 0);
		pageInfo.setTotal(total);

		Response userResponse = new Response();
		userResponse.setData(paginatedUsers);
		userResponse.setPageInfo(pageInfo);

		return userResponse;
	}
	
	public static List<User> sortUsers(List<User> userList, String sortType, String sortOrder) {
		// Define a comparator based on the given sortType and sortOrder
		Comparator<User> comparator = null;

		if ("Age".equalsIgnoreCase(sortType)) {
			comparator = Comparator.comparingInt(User::getAge);
		} else if ("Name".equalsIgnoreCase(sortType)) {
			comparator = Comparator.comparingInt(user -> user.getName().length());
		}

		if (comparator != null) {
			// Apply sorting based on sortOrder
			userList.sort(comparator);

			if ("odd".equalsIgnoreCase(sortOrder)) {
				if ("Age".equalsIgnoreCase(sortType)) {
					userList.removeIf(user -> user.getAge() % 2 == 0);
				} else {
					userList.removeIf(user -> user.getName().length() % 2 == 0);
				}

			} else if ("even".equalsIgnoreCase(sortOrder)) {
				if ("Age".equalsIgnoreCase(sortType)) {
					userList.removeIf(user -> user.getAge() % 2 != 0);
				} else {
					userList.removeIf(user -> user.getName().length() % 2 != 0);
				}
			}
		}
		return userList;
	}
	
}
