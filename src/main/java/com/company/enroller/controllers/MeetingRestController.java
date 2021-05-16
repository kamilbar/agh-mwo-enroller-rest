package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;
	@Autowired
	ParticipantService participantService;
	
	// 1.1 Pobieranie listy spotkań
	// GET http://localhost:8080/meetings/
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	// 1.2 Pobieranie pojedynczego spotkania
	// GET http://localhost:8080/meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
		if (meeting == null) { 
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
	}
	
	// 1.3 Dodawanie spotkań
	// POST http://localhost:8080/meetings + JSON
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting){
		if (meetingService.findById(meeting.getId()) != null){
			return new ResponseEntity<String>("Unable to create. Meeting with id '" + meeting.getId() + "' already exists", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	// 2.1 dodawanie uczestnika do spotkania
	//PUT http://localhost:8080/meetings/2/AddParticipant_user9
	@RequestMapping(value = "/{meeting_id}/AddParticipant_{participant_id}", method = RequestMethod.PUT) 
	public ResponseEntity<?> addParticipantToTheMeeting(@PathVariable("meeting_id") long id, @PathVariable("participant_id") String login){
		if (meetingService.findById(id) == null || participantService.findByLogin(login) == null) {
			return new ResponseEntity<String>("Unable to add participant. Meeting or participant does not exist", HttpStatus.CONFLICT);
		}
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		meeting.addParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Collection>(meeting.getParticipants(), HttpStatus.CREATED);
	}

	// 2.2 Pobieranie uczestników spotkania
	//GET http://localhost:8080/meetings/4/participants
	@RequestMapping(value = "/{meeting_id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("meeting_id") long id) {
	    Meeting meeting = meetingService.findById(id);
		if (meeting == null) { 
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 
		return new ResponseEntity<Collection>(meeting.getParticipants(), HttpStatus.OK); 
	}
	
	// 3.1 Usuwanie spotkania
	// DELETE http://localhost:8080/meetings/5
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
		if (meeting == null) { 
		return new ResponseEntity(HttpStatus.NOT_FOUND);
		} 
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT); 
	}
	
	// 3.2 Aktualizacja spotkania
	// PUT http://localhost:8080/meetings/ + JSON
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@RequestBody Meeting meeting) {
		meetingService.update(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT); 
	}
	
	// 3.3 Usuwanie uczestnika ze spotkania
	// PUT http://localhost:8080/meetings/2/removeParticipant_user9
	@RequestMapping(value = "/{meeting_id}/removeParticipant_{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable("meeting_id") long id,@PathVariable("id") String login) {
		Meeting meeting = meetingService.findById(id);
		Participant participant = participantService.findByLogin(login);
		meeting.removeParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT); 
	}
	
	
}