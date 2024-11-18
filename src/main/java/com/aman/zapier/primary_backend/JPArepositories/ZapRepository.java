package com.aman.zapier.primary_backend.JPArepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aman.zapier.primary_backend.entities.User;
import com.aman.zapier.primary_backend.entities.Zap;

public interface ZapRepository extends JpaRepository<Zap, UUID> {
	Optional<List<Zap>> findAllByUsers(User user);
}
