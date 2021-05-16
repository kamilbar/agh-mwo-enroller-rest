package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;
	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		return session.createCriteria(Meeting.class).list();
	}
	
	public Meeting findById(long id) {
		return (Meeting) session.get(Meeting.class, id);
	}
	
	public Meeting findByTitle(String title) {
		return (Meeting) session.get(Meeting.class, title);
	}

	public void add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
	}

	public void delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}
	
	public void update(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.saveOrUpdate(meeting);
		transaction.commit();
	}

}
