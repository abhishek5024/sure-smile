package com.clinic.appointmentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinic.appointmentservice.dto.AppointmentResponse;
import com.clinic.appointmentservice.dto.BookAppointmentRequest;
import com.clinic.appointmentservice.dto.CreateSlotRequest;
import com.clinic.appointmentservice.entity.Appointment;
import com.clinic.appointmentservice.entity.Slot;
import com.clinic.appointmentservice.exception.AppointmentNotFoundException;
import com.clinic.appointmentservice.exception.SlotUnavailableException;
import com.clinic.appointmentservice.repository.AppointmentRepository;
import com.clinic.appointmentservice.repository.SlotRepository;

@Service
public class AppointmentService {
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public AppointmentResponse bookAppointment(BookAppointmentRequest request) {
        Slot slot = slotRepository.findSlotForUpdate(request.getDoctorId(), request.getDate(), request.getStartTime())
                .orElseThrow(() -> new SlotUnavailableException("Slot not found"));
        if (!slot.isAvailable()) {
            throw new SlotUnavailableException("Slot is not available");
        }
        slot.setAvailable(false);
        slotRepository.save(slot);

        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setDoctorId(request.getDoctorId());
        appointment.setDate(request.getDate());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setStatus(Appointment.Status.BOOKED);
        appointmentRepository.save(appointment);

        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setPatientId(appointment.getPatientId());
        response.setDoctorId(appointment.getDoctorId());
        response.setDate(appointment.getDate());
        response.setStartTime(appointment.getStartTime());
        response.setEndTime(appointment.getEndTime());
        response.setStatus(appointment.getStatus().name());
        return response;
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        appointment.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appointment);

        Slot slot = slotRepository.findSlotForUpdate(appointment.getDoctorId(), appointment.getDate(), appointment.getStartTime())
                .orElseThrow(() -> new SlotUnavailableException("Slot not found"));
        slot.setAvailable(true);
        slotRepository.save(slot);
    }

    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(a -> {
                    AppointmentResponse r = new AppointmentResponse();
                    r.setId(a.getId());
                    r.setPatientId(a.getPatientId());
                    r.setDoctorId(a.getDoctorId());
                    r.setDate(a.getDate());
                    r.setStartTime(a.getStartTime());
                    r.setEndTime(a.getEndTime());
                    r.setStatus(a.getStatus().name());
                    return r;
                })
                .collect(Collectors.toList());
    }

    public List<Slot> getSlots(Long doctorId, java.time.LocalDate date) {
        return slotRepository.findByDoctorIdAndDate(doctorId, date);
    }

    public Slot createSlot(CreateSlotRequest request) {
        Slot slot = new Slot();
        slot.setDoctorId(request.getDoctorId());
        slot.setDate(request.getDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setAvailable(request.isAvailable());
        return slotRepository.save(slot);
    }
}
