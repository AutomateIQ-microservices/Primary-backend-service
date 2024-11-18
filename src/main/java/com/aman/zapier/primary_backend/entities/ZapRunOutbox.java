package com.aman.zapier.primary_backend.entities;

import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ZapRunOutbox {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;
	
	@OneToOne
	@JoinColumn(name = "zap_run_id")
	private ZapRun zapRun;

	public ZapRunOutbox(ZapRun zapRun) {
		super();
		this.zapRun = zapRun;
	}

	public ZapRunOutbox() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ZapRun getZapRun() {
		return zapRun;
	}

	public void setZapRun(ZapRun zapRun) {
		this.zapRun = zapRun;
	}
	
	
	
}