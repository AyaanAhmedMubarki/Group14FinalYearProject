package com.health.mediconnectx.controller;

import com.health.mediconnectx.entity.OfflineSlot;
import com.health.mediconnectx.entity.Doctor;
import com.health.mediconnectx.entity.DoctorLocation;
import com.health.mediconnectx.entity.Patient;
import com.health.mediconnectx.entity.User;
import com.health.mediconnectx.service.OfflineSlotService;
import com.health.mediconnectx.service.DoctorLocationService;
import com.health.mediconnectx.repository.DoctorRepository;
import com.health.mediconnectx.repository.OfflineSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/offline-slots")
@CrossOrigin("*")
public class OfflineSlotController {

    @Autowired
    private OfflineSlotService slotService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorLocationService locationService;

    @Autowired
    private OfflineSlotRepository offlineSlotRepository;

    @GetMapping
    public ResponseEntity<List<OfflineSlot>> getAllSlots() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User currentUser = (User) principal;
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName()));
        if (!isAdmin) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(slotService.getAllSlots());
    }

    @PostMapping("/create")
    public OfflineSlot createSlot(
            @RequestParam Long doctorId,
            @RequestParam Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        DoctorLocation location = locationService.getLocationById(locationId);

        if (doctor != null && location != null) {
            return slotService.createSlot(doctor, location, slotDate, startTime, endTime);
        }
        return null;
    }

    @GetMapping("/doctor/{doctorId}")
    public List<OfflineSlot> getSlotsByDoctor(@PathVariable Long doctorId) {
        return slotService.getSlotsByDoctor(doctorId);
    }

    @GetMapping("/doctor/{doctorId}/date")
    public List<OfflineSlot> getSlotsByDoctorAndDate(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate) {
        return slotService.getSlotsByDoctorAndDate(doctorId, slotDate);
    }

    @GetMapping("/doctor/{doctorId}/available")
    public List<OfflineSlot> getAvailableSlotsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate) {
        return slotService.getAvailableSlotsByDoctorAndDate(doctorId, slotDate);
    }

    @GetMapping("/available")
    public List<OfflineSlot> getAvailableSlotsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate) {
        return slotService.getAvailableSlotsByDate(slotDate);
    }

    @GetMapping("/{id}")
    public OfflineSlot getSlotById(@PathVariable Long id) {
        return slotService.getSlotById(id);
    }

    @PostMapping("/{slotId}/book")
    public OfflineSlot bookSlot(@PathVariable Long slotId, @RequestParam Long patientId) {
        return slotService.bookSlot(slotId, patientId);
    }

    @DeleteMapping("/{id}")
    public void deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
    }

    @GetMapping("/location/{locationId}")
    public List<OfflineSlot> getSlotsByLocation(@PathVariable Long locationId) {
        return slotService.getSlotsByLocation(locationId);
    }

    @GetMapping("/patient/{patientId}")
    public List<OfflineSlot> getSlotsByPatient(@PathVariable Long patientId) {
        return slotService.getSlotsByPatient(patientId);
    }

    @PutMapping("/{id}/prescription")
    public ResponseEntity<?> uploadPrescription(@PathVariable Long id,
                                                @RequestParam("file") MultipartFile file) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User currentUser = (User) principal;

        OfflineSlot slot = offlineSlotRepository.findById(id).orElse(null);
        if (slot == null) return ResponseEntity.notFound().build();

        Doctor d = currentUser.getDoctor();
        if (d == null || !d.getId().equals(slot.getDoctor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied. Only the assigned doctor can upload a prescription."));
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equalsIgnoreCase("application/pdf") &&
                 !contentType.equalsIgnoreCase("image/jpeg") &&
                 !contentType.equalsIgnoreCase("image/jpg") &&
                 !contentType.equalsIgnoreCase("image/png"))) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid file type. Only PDF, JPG, JPEG, and PNG are allowed."));
        }

        try {
            slot.setPrescriptionFile(file.getBytes());
            slot.setPrescriptionFileType(contentType);
            offlineSlotRepository.save(slot);
            return ResponseEntity.ok(Map.of("message", "Prescription uploaded successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/prescription")
    public ResponseEntity<?> getPrescription(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User currentUser = (User) principal;

        OfflineSlot slot = offlineSlotRepository.findById(id).orElse(null);
        if (slot == null) return ResponseEntity.notFound().build();

        Doctor d = currentUser.getDoctor();
        Patient p = currentUser.getPatient();
        boolean isDoctor  = d != null && d.getId().equals(slot.getDoctor().getId());
        boolean isPatient = p != null && slot.getPatient() != null && p.getId().equals(slot.getPatient().getId());
        boolean isAdmin   = currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getName()));

        if (!isDoctor && !isPatient && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied."));
        }

        if (slot.getPrescriptionFile() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No prescription uploaded yet."));
        }

        String b64 = java.util.Base64.getEncoder().encodeToString(slot.getPrescriptionFile());
        String fileType = slot.getPrescriptionFileType() != null ? slot.getPrescriptionFileType() : "application/octet-stream";
        return ResponseEntity.ok(Map.of("prescription", b64, "contentType", fileType));
    }
}
