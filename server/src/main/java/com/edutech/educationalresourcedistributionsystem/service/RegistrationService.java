package com.edutech.educationalresourcedistributionsystem.service;
import com.edutech.educationalresourcedistributionsystem.entity.Event;
import com.edutech.educationalresourcedistributionsystem.entity.EventRegistration;
import com.edutech.educationalresourcedistributionsystem.repository.EventRegistrationRepository;
import com.edutech.educationalresourcedistributionsystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class RegistrationService {
   @Autowired
   private EventRepository eventRepository;
   @Autowired
   private EventRegistrationRepository registrationRepository;
   public EventRegistration registerStudent(Long eventId, Long studentId) {
       Event event = eventRepository.findById(eventId)
              .orElseThrow(() -> new RuntimeException("Event not found"));
      EventRegistration registration = new EventRegistration();
      registration.setStudentId(studentId);
      registration.setStatus("REGISTERED");
      registration.setEvent(event);
       return registrationRepository.save(registration);
   }
   public List<EventRegistration> getAllRegistrations() {
       return registrationRepository.findAll();
   }
}