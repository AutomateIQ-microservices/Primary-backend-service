package com.aman.zapier.primary_backend.JPArepositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aman.zapier.primary_backend.entities.AvailableTriggers;

public interface AvailableTriggersRepository extends JpaRepository<AvailableTriggers, UUID>{

}
