package com.globallearning.platform.booking.service;

import com.globallearning.platform.booking.dto.AvailableOfferingResponseDto;
import com.globallearning.platform.booking.dto.BookingRequest;
import com.globallearning.platform.booking.dto.BookingResponseDto;
import com.globallearning.platform.booking.dto.ParentBookingResponseDto;
import com.globallearning.platform.booking.model.Booking;
import com.globallearning.platform.booking.model.Offering;
import com.globallearning.platform.booking.model.Parent;
import com.globallearning.platform.booking.repository.BookingRepository;
import com.globallearning.platform.booking.repository.OfferingRepository;
import com.globallearning.platform.booking.repository.ParentRepository;
import com.globallearning.platform.booking.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final ParentRepository parentRepository;
    private final OfferingRepository offeringRepository;
    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingResponseDto bookOffering(BookingRequest request){
        Parent parent=parentRepository.findByIdForUpdate(request.parentId())
                .orElseThrow(()-> new IllegalArgumentException("Parent not found"));

        Offering offering=offeringRepository.findById(request.offeringId())
                .orElseThrow(()-> new IllegalArgumentException("Offering not found"));

        if(offering.getBookedSeats() >= offering.getCapacity()){
            throw new IllegalArgumentException(("This offering is fully booked"));
        }

        long overlaps = sessionRepository.countOverlappingSession(offering.getId(), parent.getId());
        if(overlaps > 0){
            throw  new IllegalArgumentException("Time conflict: You have already booked a session that overlaps with this offering.");
        }

        offering.setBookedSeats(offering.getBookedSeats()+1);
        offeringRepository.save(offering);

        Booking booking=new Booking();
        booking.setParent(parent);
        booking.setOffering(offering);
        bookingRepository.save(booking);
        return new BookingResponseDto(booking.getId(),booking.getOffering().getId(),booking.getOffering().getCourse().getTitle(), booking.getCreatedAt());
    }
    public Page<AvailableOfferingResponseDto> getAllAvailableOfferings(Pageable pageable){
        Page<Offering> offerings=offeringRepository.findAvailableOffering(pageable);
        return offerings.map(offering -> new AvailableOfferingResponseDto(
                offering.getId(),
                offering.getCapacity(),
                offering.getCourse().getTitle()
        ));
    }

    public Page<ParentBookingResponseDto> getBookingsByParentId(Long parentId, Pageable pageable){
        Page<Booking> bookings=bookingRepository.findByParentId(parentId,pageable);

        return bookings.map(booking ->new ParentBookingResponseDto(
                booking.getId(),
                booking.getOffering().getId(),
                booking.getOffering().getCourse().getTitle(),
                booking.getCreatedAt()
        ));
    }
}
