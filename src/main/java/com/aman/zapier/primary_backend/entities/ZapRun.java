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
public class ZapRun {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "zap_id") // Specifies the column name for the foreign key
	private Zap zap;
	 
	@Column(columnDefinition = "text")
	private String metadata;
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public ZapRun() {
		super();
	}

	public ZapRun(Zap zap,JsonNode metadata) {
		super();
		this.zap = zap;
		setMetadata(metadata);
	}

	// Serialize JsonNode to String when storing it
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
	
	
	
}
