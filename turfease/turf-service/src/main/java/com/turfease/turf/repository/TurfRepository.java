package com.turfease.turf.repository;

import com.turfease.turf.model.SportType;
import com.turfease.turf.model.Turf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurfRepository extends JpaRepository<Turf, Long> {
    List<Turf> findBySportType(SportType sportType);
}