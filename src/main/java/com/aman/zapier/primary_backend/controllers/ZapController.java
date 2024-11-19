package com.aman.zapier.primary_backend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aman.zapier.primary_backend.JPArepositories.ActionRepository;
import com.aman.zapier.primary_backend.JPArepositories.AvailableActionsRepository;
import com.aman.zapier.primary_backend.JPArepositories.AvailableTriggersRepository;
import com.aman.zapier.primary_backend.JPArepositories.TriggerRepository;
import com.aman.zapier.primary_backend.JPArepositories.UserRepository;
import com.aman.zapier.primary_backend.JPArepositories.ZapRepository;
import com.aman.zapier.primary_backend.entities.Action;
import com.aman.zapier.primary_backend.entities.AvailableActions;
import com.aman.zapier.primary_backend.entities.AvailableTriggers;
import com.aman.zapier.primary_backend.entities.Trigger;
import com.aman.zapier.primary_backend.entities.User;
import com.aman.zapier.primary_backend.entities.Zap;
import com.aman.zapier.primary_backend.jwt.AuthTokenFilter;
import com.aman.zapier.primary_backend.objects.AvailableActionObject;
import com.aman.zapier.primary_backend.objects.AvailableTriggerObject;
import com.aman.zapier.primary_backend.objects.ZapObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path="/api/v1/zaps")
@CrossOrigin(origins="*")
public class ZapController {

	private static final Logger logger = LoggerFactory.getLogger(ZapController.class);
	@Autowired
	private TriggerRepository triggerRepo;
	
	@Autowired 
	private AvailableActionsRepository availableActionRepo;
	
	@Autowired
	private AvailableTriggersRepository availableTriggerRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ZapRepository zapRepo;
	
	@Autowired
	private ActionRepository actionRepo;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	
	@PostMapping(path="/")
	@Transactional
	public ResponseEntity<String> createZap(@RequestBody Map<String,Object> requestBody,HttpServletRequest request){
		// Check if the request body is null
		if(requestBody == null) {
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("invalid credentials");
		}
		
		// Extract the user ID from the JWT attribute
		Long userId = (Long) request.getAttribute("id");
		User associatedUser = userRepo.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));

		// Extract available trigger ID and metadata from the request body
		String availableTriggerId = (String) requestBody.get("available_trigger_id");
		//JsonNode triggerMetadata = (JsonNode) requestBody.get("metadata");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> metadataMap = (Map<String, Object>) requestBody.get("metadata");

         // Convert the Map to JsonNode
         JsonNode triggerMetadata = objectMapper.valueToTree(metadataMap);
		// Find the available trigger and create a new Trigger entity
		AvailableTriggers availableTrigger = availableTriggerRepo.findById(UUID.fromString(availableTriggerId))
			.orElseThrow(() -> new RuntimeException("Trigger not found"));
		Trigger newTrigger = new Trigger();
		newTrigger.setTriggerId(availableTrigger);
		newTrigger.setMetadata(triggerMetadata);
		newTrigger.setTriggerName(availableTrigger.getName());
		
		// Save the newly created trigger
		Trigger createdTrigger = triggerRepo.save(newTrigger);

		// Create a new Zap entity based on the trigger
		Zap newZap = new Zap();
		newZap.setUsers(associatedUser);
		newZap.setTrig(createdTrigger);
		
		// Save the newly created Zap
		Zap createdZap = zapRepo.save(newZap);
		
		// Extract actions from the request body
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> actions = (List<Map<String, Object>>) requestBody.get("actions");
		if (actions != null) {
			for (int i = 0; i < actions.size(); i++) {
				Map<String,Object> eachAction = actions.get(i);
				String availableActionId = (String) eachAction.get("available_action_id");
				
				@SuppressWarnings("unchecked")
				Map<String, Object> actionMetadataMap = (Map<String, Object>)eachAction.get("metadata");
				
				JsonNode actionMetadata =objectMapper.valueToTree(actionMetadataMap);

				// Find the available action
				AvailableActions availableAction = availableActionRepo.findById(UUID.fromString(availableActionId))
					.orElseThrow(() -> new RuntimeException("Action not found"));

				// Create a new Action entity and set its properties
				Action newAction = new Action();
				newAction.setActionId(availableAction);
				newAction.setMetadata(actionMetadata);
				newAction.setSortingOrder(i + 1);
				newAction.setZapId(createdZap);
				newAction.setActionName(availableAction.getName());

				// Save the newly created action
				actionRepo.save(newAction);
			}
		}
		
		// Return the created Zap in the response
		return ResponseEntity.status(HttpStatus.SC_OK).body("Zap created successfully : "+createdZap.getId());
	}
	
	
	
	
	//send an object with zapid,with it's trigger name and action names associated with it to build the ui
	@GetMapping(path="/")
	public ResponseEntity<List<ZapObject>> returnAllZaps(HttpServletRequest request){
		Long userId=(Long)request.getAttribute("id");
		User associatedUser = userRepo.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		
		List<Zap> allZaps=zapRepo.findAllByUsers(associatedUser).orElseThrow(()->new RuntimeException("no zaps associated "));
		
		List<ZapObject> returnObject=new ArrayList<ZapObject>();
		
		for (Zap currentZap : allZaps) {
		        // Creating a new ZapObject for each Zap
		        ZapObject currentZapObject = new ZapObject();

		        // Setting the Zap ID
		        currentZapObject.setId(currentZap.getId());

		        // Setting the trigger name from the associated Trigger
		        Trigger zapTrigger = currentZap.getTrig();
		        //currentZapObject.setTriggerName(zapTrigger != null ? zapTrigger.getTriggerName() : "No Trigger");
		        if(zapTrigger!=null) {
		        	AvailableTriggerObject trigObject=new AvailableTriggerObject();
		        	trigObject.setId(zapTrigger.getId());
		        	trigObject.setName(zapTrigger.getTriggerName());
		        	currentZapObject.setTriggerName(trigObject);
		        }

		        // Setting the action names by iterating over associated actions
		        List<Action> zapActions = currentZap.getActions();
		        List<AvailableActionObject> actionNames = new ArrayList<>();
		        for (Action action : zapActions) {
		        	AvailableActionObject actObj=new AvailableActionObject();
		        	actObj.setId(action.getId());
		        	actObj.setName(action.getActionName());
		            actionNames.add(actObj); // Assuming Action has a `getName` method
		        }
		        currentZapObject.setActionNames(actionNames);

		        // Adding the ZapObject to the return list
		        returnObject.add(currentZapObject);
		}
		return ResponseEntity.status(HttpStatus.SC_OK).body(returnObject);
		
	}
	
	//return a single zap based on zapId 
	
	@GetMapping(path="/{zapId}")
	public ResponseEntity<ZapObject> returnSingleZap(@PathVariable String zapId){
		UUID zapIdValue=UUID.fromString(zapId);
		
		Zap currentZap=zapRepo.findById(zapIdValue).orElseThrow(()->new RuntimeException("no zaps found with this id"));
		
		List<Action> currentActions=currentZap.getActions();
		Trigger currentTrigger=currentZap.getTrig();
		
		ZapObject responseObject=new ZapObject();
		AvailableTriggerObject trigObj=new AvailableTriggerObject();
		trigObj.setId(currentTrigger.getId());
		trigObj.setName(currentTrigger.getTriggerName());
		responseObject.setTriggerName(trigObj);
		responseObject.setId(currentZap.getId());
		
		
		List<AvailableActionObject> actionNames=new ArrayList<>();
		for(Action action:currentActions) {
			AvailableActionObject actObj=new AvailableActionObject();
			actObj.setId(action.getId());
			actObj.setName(action.getActionName());
			actionNames.add(actObj);
		}
		responseObject.setActionNames(actionNames);
		
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseObject);
		
	}
}
