package com.globallearning.platform.booking.controller;

import com.globallearning.platform.booking.dto.*;
import com.globallearning.platform.booking.model.Booking;
import com.globallearning.platform.booking.model.Offering;
import com.globallearning.platform.booking.model.Session;
import com.globallearning.platform.booking.service.BookingService;
import com.globallearning.platform.booking.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiController {
    private final TeacherService teacherService;
    private final BookingService bookingService;

    @PostMapping("/teacher/offerings")
    public ResponseEntity<?> createOffering(@Valid @RequestBody CreateOfferingRequest request){
        Offering offering=teacherService.createOffering(request);
        OfferingResponseDto response=new OfferingResponseDto(
                offering.getId(),
                offering.getCourse().getId(),
                offering.getTeacher().getId(),
                offering.getCapacity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/teacher/offerings/{offeringId}/sessions")
    public ResponseEntity<?> addSessionToOffering(@PathVariable Long offeringId, @RequestBody List<Session> sessions){
        OfferingSessionResponseDto response = teacherService.addSessionToOffering(offeringId, sessions);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/teacher/{teacherId}/offerings")
    public ResponseEntity<Page<OfferingResponseTeacherDto>> getTeacherOfferings(
            @PathVariable Long teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable= PageRequest.of(page,size);
        return ResponseEntity.ok(teacherService.getOfferingByTeacher(teacherId,pageable));
    }

    @PostMapping("/parent/bookings")
    public ResponseEntity<?> bookOffering(@Valid @RequestBody BookingRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookOffering(request));
    }

    @GetMapping("/parent/offerings/available")
    public ResponseEntity<Page<AvailableOfferingResponseDto>> getAvailableOfferings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable=PageRequest.of(page,size);
        return ResponseEntity.ok(bookingService.getAllAvailableOfferings(pageable));
    }

    @GetMapping("/parent/{parentId}/bookings")
    public ResponseEntity<Page<ParentBookingResponseDto>> getBookings(
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ){
        Pageable pageable=PageRequest.of(page,size);
        return ResponseEntity.ok(bookingService.getBookingsByParentId(parentId,pageable));
    }
}
