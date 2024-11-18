package com.aman.zapier.primary_backend.JPArepositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aman.zapier.primary_backend.entities.AvailableActions;

public interface AvailableActionsRepository extends JpaRepository<AvailableActions, UUID>{

}
