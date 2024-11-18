package com.aman.zapier.primary_backend.entities;

import java.util.UUID;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Action {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) //to auto generate uuid
	@Column(columnDefinition = "uuid")
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name="zap_id")
	private Zap zapId;
	
	@ManyToOne
	@JoinColumn(name="available_action_id")
	private AvailableActions actionId;
	
	private int sortingOrder; 
	
	private String actionName;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	//metadata section
	@Column(columnDefinition = "text")
	private String metadata;
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public void setMetadata(JsonNode metadata) {
        try {
            this.metadata = OBJECT_MAPPER.writeValueAsString(metadata);
        } catch (Exception e) {
            e.printStackTrace();
            this.metadata = null;
        }
    }
    
    // Deserialize String back to JsonNode when retrieving it
    public JsonNode getMetadata() {
        try {
            return OBJECT_MAPPER.readTree(this.metadata);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	//metadata section ends here
    
    
	public int getSortingOrder() {
		return sortingOrder;
	}

	public void setSortingOrder(int sortingOrder) {
		this.sortingOrder = sortingOrder;
	}

	public Action() {
		super();
	}

	public Action(Zap zapId, AvailableActions actionId,int sortingOrder) {
		super();
		this.zapId = zapId;
		this.actionId = actionId;
		this.sortingOrder=sortingOrder;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Zap getZapId() {
		return zapId;
	}

	public void setZapId(Zap zapId) {
		this.zapId = zapId;
	}

	public AvailableActions getActionId() {
		return actionId;
	}

	public void setActionId(AvailableActions actionId) {
		this.actionId = actionId;
	}
	
	
	
}
