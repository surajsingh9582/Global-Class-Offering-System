package com.globallearning.platform.booking.repository;

import com.globallearning.platform.booking.model.Offering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OfferingRepository extends JpaRepository<Offering,Long> {
    Page<Offering> findByTeacherId(Long teacherId, Pageable pageable);
    @Query("SELECT o FROM Offering o WHERE (SELECT COUNT(b) FROM Booking b WHERE b.offering.id = o.id) < o.capacity")
    @EntityGraph(attributePaths = {"course"})
    Page<Offering> findAvailableOffering(Pageable pageable);
}
