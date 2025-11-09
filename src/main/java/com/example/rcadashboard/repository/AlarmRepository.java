package com.example.rcadashboard.repository;

import com.example.rcadashboard.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    // Custom finder for Host filtering (case-insensitive containment)
    List<Alarm> findByHostContainingIgnoreCase(String host);
    
    // Custom finder for State filtering (exact match)
    List<Alarm> findByState(String state);
}
