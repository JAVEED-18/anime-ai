package com.turfease.booking.repository;

import com.turfease.booking.model.Booking;
import com.turfease.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.turfId = :turfId AND b.status <> 'CANCELLED' AND (b.startTime < :endTime AND b.endTime > :startTime)")
    List<Booking> findConflicts(@Param("turfId") Long turfId,
                                @Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime);

    List<Booking> findByUserId(Long userId);
}