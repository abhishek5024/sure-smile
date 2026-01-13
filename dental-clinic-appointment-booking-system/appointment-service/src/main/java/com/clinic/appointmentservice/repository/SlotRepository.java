package com.clinic.appointmentservice.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinic.appointmentservice.entity.Slot;

import jakarta.persistence.LockModeType;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Slot s WHERE s.doctorId = :doctorId AND s.date = :date AND s.startTime = :startTime")
    Optional<Slot> findSlotForUpdate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    List<Slot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
