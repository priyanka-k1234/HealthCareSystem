package com.example.healthcare.service;

import com.example.healthcare.model.Appointment;
import com.example.healthcare.dto.AppointmentView;
import com.example.healthcare.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Old method (not used for view anymore)
    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // New method for view:
    public List<AppointmentView> getAppointmentViewsForDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream().map(appt -> {
            String patientName = patientService.getPatientNameById(appt.getPatientId());
            String doctorName = userService.getFullNameById(appt.getDoctorId());
            return new AppointmentView(
                    appt.getDateTime(),
                    appt.getReason(),
                    appt.getStatus(),
                    patientName,
                    doctorName
            );
        }).collect(Collectors.toList());
    }

    public Appointment saveAppointment(Appointment appt) {
        return appointmentRepository.save(appt);
    }

    public List<Appointment> getRecentAppointments(int limit) {
        return appointmentRepository.findAll(org.springframework.data.domain.PageRequest.of(0, limit, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "dateTime"))).getContent();
    }

    public Map<String, Long> getAppointmentCountPerDay(int days) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(days - 1);
        List<Appointment> appts = appointmentRepository.findByDateTimeBetween(start.atStartOfDay(), today.plusDays(1).atStartOfDay());
        return appts.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getDateTime().toLocalDate().toString(),
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getAppointmentCountPerDoctor() {
        List<Appointment> all = appointmentRepository.findAll();
        Map<String, Long> map = new HashMap<>();
        for (Appointment appt : all) {
            String doctorId = appt.getDoctorId() != null ? appt.getDoctorId().toString() : "Unassigned";
            map.put(doctorId, map.getOrDefault(doctorId, 0L) + 1);
        }
        return map;
    }
}