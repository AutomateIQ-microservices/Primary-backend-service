package com.aman.zapier.primary_backend.objects;

import java.util.List;
import java.util.UUID;

public class ZapObject {
	private UUID id;
	private List<String> actionNames;
	private String triggerName;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public List<String> getActionNames() {
		return actionNames;
	}
	public void setActionNames(List<String> actionNames) {
		this.actionNames = actionNames;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	
	
	
}
