package com.example.healthcare.controller;

import com.example.healthcare.model.Patient;
import com.example.healthcare.model.Appointment;
import com.example.healthcare.model.User;
import com.example.healthcare.model.mongo.SensorData;
import com.example.healthcare.service.*;
import com.example.healthcare.dto.AppointmentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
public class WebController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private MedicalHistoryService medicalHistoryService;
    @Autowired
    private SensorDataService sensorDataService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;

    // Dashboard (home) with search support
    @GetMapping("/")
    public String home(@RequestParam(value = "search", required = false) String search, Model model, Principal principal) {
        String username = (principal != null) ? principal.getName() : null;
        User user = (username != null) ? userService.findByUsername(username).orElse(null) : null;

        model.addAttribute("patientCount", patientService.getAllPatients().size());
        model.addAttribute("medicalHistoryCount", medicalHistoryService.count());
        model.addAttribute("sensorDataCount", sensorDataService.count());

        // Recent Activity Feed
        List<String> recentActivity = new ArrayList<>();
        try {
            patientService.getRecentPatients(3).forEach(p -> recentActivity.add("New patient: " + p.getFirstName() + " " + p.getLastName()));
        } catch (Exception e) {}
        try {
            appointmentService.getRecentAppointments(2).forEach(a -> recentActivity.add("New appointment: " + a.getDateTime()));
        } catch (Exception e) {}
        model.addAttribute("recentActivity", recentActivity);

        // Appointments per day (for chart)
        Map<String, Long> apptsPerDay = new HashMap<>();
        try {
            apptsPerDay = appointmentService.getAppointmentCountPerDay(7);
        } catch (Exception e) {}
        model.addAttribute("apptsPerDay", apptsPerDay);

        // Gender distribution for patients (for chart)
        Map<String, Long> genderDist = new HashMap<>();
        try {
            genderDist = patientService.getGenderDistribution();
        } catch (Exception e) {}
        model.addAttribute("genderDist", genderDist);

        // Appointments per doctor (for chart)
        Map<String, Long> apptPerDoctor = new HashMap<>();
        try {
            apptPerDoctor = appointmentService.getAppointmentCountPerDoctor();
        } catch (Exception e) {}
        model.addAttribute("apptPerDoctor", apptPerDoctor);

        // Sensor data trends (for patient)
        List<SensorData> sensorData = List.of();
        if (user != null && "PATIENT".equals(user.getRole())) {
            try {
                sensorData = sensorDataService.findByPatientId(user.getId());
            } catch (Exception e) {}
        }
        model.addAttribute("sensorDataTrend", sensorData);

        // System Health
        model.addAttribute("dbStatus", "Connected");

        // Role-based quick actions
        model.addAttribute("userRole", user != null && user.getRole() != null ? user.getRole() : "");

        // Search logic
        if (search != null && !search.isEmpty()) {
            List<Patient> patients = patientService.searchPatientsByName(search);
            model.addAttribute("searchResults", patients);
            model.addAttribute("searchQuery", search);
        } else {
            model.addAttribute("searchResults", null);
            model.addAttribute("searchQuery", "");
        }

        return "home";
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // List all patients
    @GetMapping("/patients")
    public String patients(Model model, @ModelAttribute("success") String success) {
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("success", success);
        return "patients";
    }

    // Add Patient
    @GetMapping("/add-patient")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @PostMapping("/add-patient")
    public String addPatientSubmit(@ModelAttribute Patient patient, RedirectAttributes redirectAttributes) {
        patientService.savePatient(patient);
        redirectAttributes.addFlashAttribute("success", "Patient added successfully!");
        return "redirect:/patients";
    }

    // Edit Patient
    @GetMapping("/edit-patient/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        return "patient-form";
    }

    @PostMapping("/edit-patient/{id}")
    public String editPatientSubmit(@PathVariable Long id, @ModelAttribute Patient patient, RedirectAttributes redirectAttributes) {
        patient.setPatientId(id);
        patientService.savePatient(patient);
        redirectAttributes.addFlashAttribute("success", "Patient updated successfully!");
        return "redirect:/patients";
    }

    // Delete patient
    @GetMapping("/delete-patient/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        patientService.deletePatient(id);
        redirectAttributes.addFlashAttribute("success", "Patient deleted successfully!");
        return "redirect:/patients";
    }

    // View medical history
    @GetMapping("/medical-history/{patientId}")
    public String viewMedicalHistory(@PathVariable Long patientId, Model model) {
        model.addAttribute("history", medicalHistoryService.findByPatientId(patientId).orElse(null));
        return "medical-history";
    }

    @GetMapping("/sensor-data/{patientId}")
    public String viewSensorData(@PathVariable Long patientId, Model model) {
        List<SensorData> data = sensorDataService.findByPatientId(patientId);
        if (data == null) data = new ArrayList<>();
        model.addAttribute("sensorData", data);
        return "sensor-data";
    }

    // View all appointments (for dashboard shortcut)
    @GetMapping("/appointments")
    public String allAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<AppointmentView> apptViews = new ArrayList<>();
        for (Appointment appt : appointments) {
            String patientName = "";
            try {
                Patient patient = patientService.getPatientById(appt.getPatientId());
                patientName = patient.getFirstName() + " " + patient.getLastName();
            } catch (Exception e) {}

            String doctorName = "";
            try {
                User doctor = userService.getUserById(appt.getDoctorId());
                doctorName = doctor.getFirstName() + " " + doctor.getLastName();
            } catch (Exception e) {}

            apptViews.add(new AppointmentView(
                    appt.getDateTime(),
                    appt.getReason(),
                    appt.getStatus(),
                    patientName,
                    doctorName
            ));
        }
        model.addAttribute("appointments", apptViews);
        return "appointments";
    }

    // Appointments for patients (as AppointmentView)
    @GetMapping("/patient/appointments")
    public String patientAppointments(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            List<Appointment> appointments = appointmentService.getAppointmentsForPatient(user.getId());
            List<AppointmentView> apptViews = new ArrayList<>();
            for (Appointment appt : appointments) {
                String doctorName = "";
                try {
                    User doctor = userService.getUserById(appt.getDoctorId());
                    doctorName = doctor.getFirstName() + " " + doctor.getLastName();
                } catch (Exception e) {}
                apptViews.add(new AppointmentView(
                        appt.getDateTime(),
                        appt.getReason(),
                        appt.getStatus(),
                        user.getFirstName() + " " + user.getLastName(),
                        doctorName
                ));
            }
            model.addAttribute("appointments", apptViews);
        } else {
            model.addAttribute("appointments", new ArrayList<>());
        }
        return "appointments";
    }

    // Patient: Show book appointment form (no patient dropdown)
    @GetMapping("/patient/book-appointment")
    public String bookAppointmentFormForPatient(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("isDoctor", false);
        return "book-appointment";
    }

    // Patient: Handle book appointment form submission
    @PostMapping("/patient/book-appointment")
    public String bookAppointmentSubmitForPatient(@ModelAttribute Appointment appt, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            appt.setPatientId(user.getId());
            appt.setStatus("Scheduled");
            appointmentService.saveAppointment(appt);
            redirectAttributes.addFlashAttribute("success", "Appointment booked successfully!");
        }
        return "redirect:/patient/appointments";
    }

    // Appointments for doctors
    @GetMapping("/doctor/appointments")
    public String doctorAppointments(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            model.addAttribute("appointments", appointmentService.getAppointmentViewsForDoctor(user.getId()));
        } else {
            model.addAttribute("appointments", new ArrayList<>());
        }
        return "appointments";
    }

    // Doctor: Show book appointment form
    @GetMapping("/book-appointment")
    public String bookAppointmentFormForDoctor(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("isDoctor", true);
        return "book-appointment";
    }

    // Doctor: Handle book appointment form submission
    @PostMapping("/book-appointment")
    public String bookAppointmentSubmitForDoctor(@ModelAttribute Appointment appointment, Principal principal, RedirectAttributes redirectAttributes) {
        User doctor = userService.findByUsername(principal.getName()).orElse(null);
        if (doctor != null) {
            appointment.setDoctorId(doctor.getId());
            appointment.setStatus("Scheduled");
            appointmentService.saveAppointment(appointment);
            redirectAttributes.addFlashAttribute("success", "Appointment booked successfully!");
        }
        return "redirect:/appointments";
    }

    // Manage Users (Admin only)
    @GetMapping("/admin/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "manage-users";
    }

    // Reports Page
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("patientCount", patientService.getAllPatients().size());
        model.addAttribute("appointmentCount", appointmentService.getAllAppointments().size());
        model.addAttribute("userCount", userService.getAllUsers().size());
        return "reports";
    }

    // Show add user form
    @GetMapping("/admin/add-user")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    // Handle add user form submission
    @PostMapping("/admin/add-user")
    public String addUserSubmit(@ModelAttribute User user, @RequestParam String rawPassword, RedirectAttributes redirectAttributes) {
        userService.saveUser(user, rawPassword);
        redirectAttributes.addFlashAttribute("success", "User added successfully!");
        return "redirect:/admin/users";
    }

    // Show edit user form
    @GetMapping("/admin/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found!");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", userOpt.get());
        return "user-form";
    }

    // Handle edit user form submission
    @PostMapping("/admin/edit-user/{id}")
    public String editUserSubmit(@PathVariable Long id, @ModelAttribute User user, @RequestParam(required = false) String rawPassword, RedirectAttributes redirectAttributes) {
        user.setId(id);
        if (rawPassword != null && !rawPassword.isEmpty()) {
            userService.saveUser(user, rawPassword);
        } else {
            userService.saveUserWithoutPassword(user);
        }
        redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        return "redirect:/admin/users";
    }

    // Delete user
    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        return "redirect:/admin/users";
    }
}