package com.clinic.appointmentservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.appointmentservice.dto.AppointmentResponse;
import com.clinic.appointmentservice.dto.BookAppointmentRequest;
import com.clinic.appointmentservice.dto.CreateSlotRequest;
import com.clinic.appointmentservice.entity.Slot;
import com.clinic.appointmentservice.service.AppointmentService;

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
