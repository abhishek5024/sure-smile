package com.clinic.appointmentservice.repository;

import com.clinic.appointmentservice.entity.Slot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Slot s WHERE s.doctorId = :doctorId AND s.date = :date AND s.startTime = :startTime")
    Optional<Slot> findSlotForUpdate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime);

    List<Slot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
