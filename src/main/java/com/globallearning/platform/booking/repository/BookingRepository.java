package com.globallearning.platform.booking.repository;

import com.globallearning.platform.booking.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;



public interface BookingRepository extends JpaRepository<Booking, Long> {
    @EntityGraph(attributePaths = {"offering", "offering.course"})
    Page<Booking> findByParentId(Long parentId, Pageable pageable);
}