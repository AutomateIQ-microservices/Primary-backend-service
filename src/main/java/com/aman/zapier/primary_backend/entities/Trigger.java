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
import jakarta.persistence.OneToOne;

@Entity
public class Trigger {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;
	
	@OneToOne(mappedBy="trig")
	private Zap zap;
	
	@ManyToOne
	@JoinColumn(name = "available_trigger_id")
	private AvailableTriggers availableTrigger;
	
	
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
	
    private String triggerName;
	

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public void setAvailableTrigger(AvailableTriggers availableTrigger) {
		this.availableTrigger = availableTrigger;
	}

	public Trigger(Zap zap, AvailableTriggers trigger) {
		super();
		this.zap = zap;
		this.availableTrigger = trigger;
	}

	public Trigger() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Zap getZap() {
		return zap;
	}

	public void setZap(Zap zap) {
		this.zap = zap;
	}

	public AvailableTriggers getAvailableTrigger() {
		return availableTrigger;
	}

	public void setTriggerId(AvailableTriggers trigger) {
		this.availableTrigger = trigger;
	}
	
	
}
