package com.nagarro.MiniAssignment2.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.MiniAssignment2.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>{
	
}
