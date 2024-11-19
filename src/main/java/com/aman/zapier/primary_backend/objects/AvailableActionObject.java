package com.aman.zapier.primary_backend.objects;

import java.util.UUID;

public class AvailableActionObject {
	private UUID id;
	private String name;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AvailableActionObject() {
		super();
	}
	public AvailableActionObject(UUID id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
