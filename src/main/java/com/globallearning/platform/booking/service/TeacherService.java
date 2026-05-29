package com.globallearning.platform.booking.service;

import com.globallearning.platform.booking.dto.CreateOfferingRequest;
import com.globallearning.platform.booking.dto.OfferingResponseTeacherDto;
import com.globallearning.platform.booking.dto.OfferingSessionResponseDto;
import com.globallearning.platform.booking.dto.SessionResponseDto;
import com.globallearning.platform.booking.model.Course;
import com.globallearning.platform.booking.model.Offering;
import com.globallearning.platform.booking.model.Session;
import com.globallearning.platform.booking.model.Teacher;
import com.globallearning.platform.booking.repository.CourseRepository;
import com.globallearning.platform.booking.repository.OfferingRepository;
import com.globallearning.platform.booking.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final OfferingRepository offeringRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public Offering createOffering(CreateOfferingRequest request){
        Course course=courseRepository.findById(request.courseId())
                .orElseThrow(()-> new IllegalArgumentException("Course not found"));
        Teacher teacher=teacherRepository.findById(request.teacherId())
                .orElseThrow(()->new IllegalArgumentException("Teacher not found"));

        Offering offering=new Offering();
        offering.setCourse(course);
        offering.setTeacher(teacher);
        offering.setCapacity(request.capacity());

        request.sessions().forEach(dto -> {
            Session session=new Session();
            session.setStartTime(dto.startTime());
            session.setEndTime(dto.endTime());
            offering.addSession(session);
        });

        return offeringRepository.save(offering);
    }
    @Transactional
    public OfferingSessionResponseDto addSessionToOffering(Long offeringId, List<Session> sessions){
        Offering offering=offeringRepository.findById(offeringId)
                .orElseThrow(()-> new EntityNotFoundException("Offering not found with ID: "+offeringId));
        for(Session session: sessions){
            boolean alreadyExists=offering.getSessions().stream()
                            .anyMatch(existing-> existing.getStartTime().equals(session.getStartTime()));
            if(!alreadyExists){
                session.setOffering(offering);
                offering.getSessions().add(session);
            }
        }
        Offering savedOffering = offeringRepository.save(offering);
        List<SessionResponseDto> sessionDtos = savedOffering.getSessions().stream()
                .map(s -> new SessionResponseDto(s.getId(), s.getStartTime(), s.getEndTime()))
                .toList();
        return new OfferingSessionResponseDto(
                savedOffering.getId(),
                savedOffering.getCapacity(),
                sessionDtos
        );
    }
    @Transactional
    public Page<OfferingResponseTeacherDto> getOfferingByTeacher(Long teacherId, Pageable pageable){
        Page<Offering> offerings = offeringRepository.findByTeacherId(teacherId, pageable);

        return offerings.map(offering -> new OfferingResponseTeacherDto(
                offering.getId(),
                offering.getCapacity(),
                offering.getCourse().getTitle() // Hibernate fetches this safely here!
        ));
    }
}
