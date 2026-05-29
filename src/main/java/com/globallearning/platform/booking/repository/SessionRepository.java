package com.globallearning.platform.booking.repository;

import com.globallearning.platform.booking.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("SELECT COUNT(s_new) FROM Session s_new " +
            "JOIN Session s_exist ON (s_exist.startTime < s_new.endTime AND s_exist.endTime > s_new.startTime)" +
            "JOIN Booking b ON b.offering.id = s_exist.offering.id " +
            "Where s_new.offering.id = :newOfferingId AND b.parent.id = :parentId")
    long countOverlappingSession(@Param("newOfferingId") Long newOfferingId, @Param("parentId") Long parentId);
}