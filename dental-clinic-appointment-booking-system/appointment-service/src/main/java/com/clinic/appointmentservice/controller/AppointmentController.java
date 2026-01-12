package com.clinic.appointmentservice.controller;

import com.clinic.appointmentservice.dto.*;
import com.clinic.appointmentservice.entity.Slot;
import com.clinic.appointmentservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public AppointmentResponse book(@RequestBody BookAppointmentRequest request) {
        return appointmentService.bookAppointment(request);
    }

    @PutMapping("/cancel/{id}")
    public void cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<AppointmentResponse> getByPatient(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsByPatient(patientId);
    }

    @GetMapping("/slots")
    public List<Slot> getSlots(@RequestParam Long doctorId, @RequestParam LocalDate date) {
        return appointmentService.getSlots(doctorId, date);
    }

    @PostMapping("/slots")
    public Slot createSlot(@RequestBody CreateSlotRequest request) {
        return appointmentService.createSlot(request);
    }
}
