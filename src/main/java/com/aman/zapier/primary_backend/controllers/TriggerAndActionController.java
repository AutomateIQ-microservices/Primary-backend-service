package com.aman.zapier.primary_backend.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aman.zapier.primary_backend.JPArepositories.AvailableActionsRepository;
import com.aman.zapier.primary_backend.JPArepositories.AvailableTriggersRepository;
import com.aman.zapier.primary_backend.entities.AvailableActions;
import com.aman.zapier.primary_backend.entities.AvailableTriggers;
import com.aman.zapier.primary_backend.objects.AvailableActionObject;
import com.aman.zapier.primary_backend.objects.AvailableTriggerObject;

@RestController
@RequestMapping(path="/api/v1")
@CrossOrigin(origins="*")
public class TriggerAndActionController {
	
	private static final Logger logger = LoggerFactory.getLogger(ZapController.class);
 	@Autowired
	private AvailableActionsRepository actionRepo;
	
	@Autowired
	private AvailableTriggersRepository triggerRepo;
	
	@GetMapping(path="/available-actions")
	public ResponseEntity<List<AvailableActionObject>> sendAllAvailableActions(){
		List<AvailableActions> allActions=actionRepo.findAll();
		
		List<AvailableActionObject> responseObj=new ArrayList<AvailableActionObject>();
		
		for(AvailableActions actions:allActions) {
			AvailableActionObject obj=new AvailableActionObject();
			obj.setId(actions.getId());
			obj.setName(actions.getName());
			obj.setImage(actions.getImage());
			
			responseObj.add(obj);
			
		}
		logger.debug("the size of response object is: " +responseObj.size());
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseObj);		
	}
	
	@GetMapping(path="/available-triggers")
	public ResponseEntity<List<AvailableTriggerObject>> sendAllAvailableTriggers(){
		List<AvailableTriggers> allTriggers=triggerRepo.findAll();
		
		List<AvailableTriggerObject> responseObj=new ArrayList<AvailableTriggerObject>();
		
		for(AvailableTriggers trigger:allTriggers) {
			AvailableTriggerObject obj=new AvailableTriggerObject();
			obj.setId(trigger.getId());
			obj.setName(trigger.getName());
			obj.setImage(trigger.getImage());
			
			responseObj.add(obj);
		}
		
		logger.debug("the size of response object is: " +responseObj.size());
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseObj);
	}
}
