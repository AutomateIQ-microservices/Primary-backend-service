package com.aman.zapier.primary_backend.JPArepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aman.zapier.primary_backend.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String email);
}

//findByEmailAndPassword
//findByEmailandPassword