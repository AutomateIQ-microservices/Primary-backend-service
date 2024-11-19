package com.aman.zapier.primary_backend.objects;

import java.util.List;
import java.util.UUID;

public class ZapObject {
	private UUID id;
	private List<AvailableActionObject> actionNames;
	private AvailableTriggerObject triggerName;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public List<AvailableActionObject> getActionNames() {
		return actionNames;
	}
	public void setActionNames(List<AvailableActionObject> actionNames) {
		this.actionNames = actionNames;
	}
	public AvailableTriggerObject getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(AvailableTriggerObject triggerName) {
		this.triggerName = triggerName;
	}
	
	
	
}
