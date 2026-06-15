# Frontend Implementation Guide - New Features

## 🎯 Overview
This guide shows how to add the offline booking features to your frontend while keeping your online booking system intact.

---

## 📋 New Features to Implement

### 1. **Doctor Locations** (After Booking)
**File**: `DoctorLocations.html` (NEW)
```html
<!-- Show all clinic locations for a doctor -->
<div class="location-card">
  <h3>Clinic Name</h3>
  <p>📍 Address, City, State - Pincode</p>
  <p>📞 Phone Number</p>
  <button onclick="bookAtLocation(doctorId, locationId)">Book at this Location</button>
</div>
```

**JavaScript Usage**:
```javascript
// Get doctor's locations
async function getDoctorLocations(doctorId) {
  const response = await fetch(`/api/doctor-locations/doctor/${doctorId}`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Create new location (for doctor)
async function addLocation(doctorId, location) {
  const response = await fetch('/api/doctor-locations/create', {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ ...location, doctor: { id: doctorId } })
  });
  return await response.json();
}
```

---

### 2. **Medical Records** (Post-Consultation)
**File**: `PatientMedicalHistory.html` (NEW)
```html
<!-- Show patient's medical history -->
<div class="medical-record">
  <h4>Appointment: {{appointmentDate}}</h4>
  <p><strong>Doctor:</strong> {{doctorName}}</p>
  
  <h5>Diagnosis:</h5>
  <p>{{diagnosis}}</p>
  
  <h5>Doctor's Notes:</h5>
  <p>{{notes}}</p>
  
  <h5>Observations:</h5>
  <p>{{observations}}</p>
</div>
```

**JavaScript Usage**:
```javascript
// Get patient's medical history
async function getPatientMedicalHistory(patientId) {
  const response = await fetch(`/api/medical-records/patient/${patientId}`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Get specific record for appointment
async function getRecordForAppointment(appointmentId) {
  const response = await fetch(`/api/medical-records/appointment/${appointmentId}`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Doctor creates record after consultation
async function createMedicalRecord(appointmentId, diagnosis, notes, observations) {
  const response = await fetch('/api/medical-records/create', {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({
      appointment: { id: appointmentId },
      diagnosis,
      notes,
      observations
    })
  });
  return await response.json();
}
```

---

### 3. **Prescriptions** (During/After Consultation)
**File**: `DoctorPrescriptions.html` (NEW)
```html
<!-- Doctor prescribe medicines -->
<div class="prescription-form">
  <h3>Prescribe Medicine</h3>
  
  <input type="text" placeholder="Search medicine..." onkeyup="searchMedicines(this.value)">
  <div id="medicineDropdown"></div>
  
  <div class="prescription-details">
    <input type="text" placeholder="Dosage (e.g., 500mg)">
    <input type="text" placeholder="Frequency (e.g., Twice daily)">
    <input type="text" placeholder="Duration (e.g., 7 days)">
    <input type="number" placeholder="Quantity">
    <textarea placeholder="Special instructions"></textarea>
  </div>
  
  <button onclick="createPrescription()">Save Prescription</button>
</div>

<!-- Patient view prescriptions -->
<div class="patient-prescriptions">
  <h3>My Prescriptions</h3>
  <div class="prescription-card">
    <h5>{{medicineName}}</h5>
    <p><strong>Dosage:</strong> {{dosage}}</p>
    <p><strong>Frequency:</strong> {{frequency}}</p>
    <p><strong>Duration:</strong> {{duration}}</p>
    <p><strong>Quantity:</strong> {{quantity}}</p>
    <p><strong>Status:</strong> {{status}}</p>
  </div>
</div>
```

**JavaScript Usage**:
```javascript
// Search medicines
async function searchMedicines(query) {
  const response = await fetch(`/api/medicines/search/${query}`, {
    headers: authHeaders()
  });
  const medicines = await response.json();
  displayMedicineDropdown(medicines);
}

// Get all medicines
async function getAllMedicines() {
  const response = await fetch('/api/medicines/all', {
    headers: authHeaders()
  });
  return await response.json();
}

// Create prescription
async function createPrescription(appointmentId, medicineId, dosage, frequency, duration, quantity, instructions) {
  const response = await fetch('/api/prescriptions/create', {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({
      appointment: { id: appointmentId },
      medicine: { id: medicineId },
      dosage,
      frequency,
      duration,
      quantity,
      instructions
    })
  });
  return await response.json();
}

// Get patient's prescriptions
async function getPatientPrescriptions(patientId) {
  const response = await fetch(`/api/prescriptions/patient/${patientId}`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Get prescription for appointment
async function getPrescriptionsForAppointment(appointmentId) {
  const response = await fetch(`/api/prescriptions/appointment/${appointmentId}`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Update prescription status
async function updatePrescriptionStatus(prescriptionId, status) {
  const response = await fetch(`/api/prescriptions/${prescriptionId}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify({ status })
  });
  return await response.json();
}
```

---

### 4. **Medical Tests** (Prescribed by Doctor)
**File**: `PatientTests.html` (NEW)
```html
<!-- Doctor prescribe test -->
<div class="test-prescription">
  <h3>Prescribe Test</h3>
  
  <select id="testType">
    <option>Blood Test</option>
    <option>Ultrasound</option>
    <option>X-Ray</option>
    <option>ECG</option>
  </select>
  
  <input type="text" placeholder="Test Name (e.g., Complete Blood Count)">
  <textarea placeholder="Test Description"></textarea>
  
  <button onclick="prescribeTest()">Prescribe Test</button>
</div>

<!-- Patient view tests -->
<div class="patient-tests">
  <h3>My Tests</h3>
  
  <div class="test-card">
    <h5>{{testName}}</h5>
    <p><strong>Type:</strong> {{testType}}</p>
    <p><strong>Status:</strong> {{status}}</p>
    <p><strong>Prescribed:</strong> {{prescribedDate}}</p>
    
    <div class="results" v-if="status === 'COMPLETED'">
      <h6>Results:</h6>
      <p>{{results}}</p>
      <p><strong>Normal Range:</strong> {{normalRange}}</p>
    </div>
  </div>
</div>
```

**JavaScript Usage**:
```javascript
// Prescribe test
async function prescribeTest(patientId, doctorId, testName, testType, description) {
  const response = await fetch('/api/tests/create', {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({
      patientId,
      doctorId,
      testName,
      testType,
      testDescription: description,
      prescribedDate: new Date().toISOString(),
      status: 'PRESCRIBED'
    })
  });
  return await response.json();
}

// Get patient's tests
async function getPatientTests(patientId) {
  const response = await fetch(`/api/tests/patient/${patientId}`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Get pending tests
async function getPendingTests(patientId) {
  const response = await fetch(`/api/tests/patient/${patientId}/pending`, {
    headers: authHeaders()
  });
  return await response.json();
}

// Doctor enters test results
async function updateTestResults(testId, results, normalRange) {
  const response = await fetch(`/api/tests/${testId}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify({
      results,
      normalRange,
      testDate: new Date().toISOString(),
      status: 'COMPLETED'
    })
  });
  return await response.json();
}

// Get test by type
async function getTestsByType(testType) {
  const response = await fetch(`/api/tests/type/${testType}`, {
    headers: authHeaders()
  });
  return await response.json();
}
```

---

### 5. **Medicine Database Management** (Admin/Doctor)
**File**: `MedicineManagement.html` (NEW - Admin only)
```html
<!-- Manage medicine database -->
<div class="medicine-form">
  <h3>Add Medicine</h3>
  
  <input type="text" placeholder="Medicine Name">
  <input type="text" placeholder="Generic Name">
  <input type="text" placeholder="Manufacturer">
  <input type="number" placeholder="Price (₹)">
  <input type="text" placeholder="Dosage (e.g., 500mg)">
  
  <select>
    <option>Tablet</option>
    <option>Capsule</option>
    <option>Syrup</option>
    <option>Injection</option>
  </select>
  
  <textarea placeholder="Description"></textarea>
  <textarea placeholder="Side Effects"></textarea>
  <textarea placeholder="Contraindications"></textarea>
  <input type="number" placeholder="Stock Quantity">
  
  <button onclick="addMedicine()">Add Medicine</button>
</div>

<!-- View all medicines -->
<div class="medicines-list">
  <table>
    <tr>
      <th>Name</th>
      <th>Generic</th>
      <th>Manufacturer</th>
      <th>Price</th>
      <th>Type</th>
      <th>Stock</th>
      <th>Actions</th>
    </tr>
    <tr v-for="medicine in medicines">
      <td>{{medicine.name}}</td>
      <td>{{medicine.genericName}}</td>
      <td>{{medicine.manufacturer}}</td>
      <td>₹{{medicine.price}}</td>
      <td>{{medicine.type}}</td>
      <td>{{medicine.stockQuantity}}</td>
      <td>
        <button onclick="editMedicine(medicine.id)">Edit</button>
        <button onclick="deleteMedicine(medicine.id)">Delete</button>
      </td>
    </tr>
  </table>
</div>
```

**JavaScript Usage**:
```javascript
// Add new medicine
async function addMedicine(medicine) {
  const response = await fetch('/api/medicines/create', {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify(medicine)
  });
  return await response.json();
}

// Get all medicines
async function getAllMedicines() {
  const response = await fetch('/api/medicines/all', {
    headers: authHeaders()
  });
  return await response.json();
}

// Update medicine
async function updateMedicine(medicineId, medicine) {
  const response = await fetch(`/api/medicines/${medicineId}`, {
    method: 'PUT',
    headers: authHeaders(),
    body: JSON.stringify(medicine)
  });
  return await response.json();
}

// Delete medicine
async function deleteMedicine(medicineId) {
  const response = await fetch(`/api/medicines/${medicineId}`, {
    method: 'DELETE',
    headers: authHeaders()
  });
  return response.ok;
}
```

---

## 🔄 Integration with Existing Features

### **Appointment Booking Flow**
```javascript
// EXISTING: Patient books appointment online
POST /api/appointments/book/{patientId}/{doctorId}
{
  appointmentTime: "2026-06-20T10:00:00",
  symptoms: "Chest pain",  // ⭐ NEW FIELD
  notes: "Had for 2 days"  // ⭐ NEW FIELD
}

// NEW: After consultation, doctor creates medical record
POST /api/medical-records/create
{
  appointment: { id: appointmentId },
  diagnosis: "Mild Angina",
  notes: "Refer to cardiologist",
  observations: "Patient stable"
}

// NEW: Doctor prescribes medicine
POST /api/prescriptions/create
{
  appointment: { id: appointmentId },
  medicine: { id: medicineId },
  dosage: "5mg",
  frequency: "Twice daily",
  duration: "7 days"
}

// NEW: Doctor prescribes test
POST /api/tests/create
{
  patientId: patientId,
  doctorId: doctorId,
  testName: "ECG",
  testType: "ECG",
  status: "PRESCRIBED"
}
```

---

## 🎨 UI Components to Add

### **1. Appointment Details Card** (Enhanced)
```html
<!-- Show appointment with symptoms & notes -->
<div class="appointment-detail">
  <h4>Appointment Details</h4>
  <p><strong>Date:</strong> {{appointmentTime}}</p>
  <p><strong>Doctor:</strong> {{doctorName}}</p>
  <p><strong>Symptoms:</strong> {{symptoms}}</p>
  <p><strong>Notes:</strong> {{notes}}</p>
</div>
```

### **2. Medical Timeline** (New)
```html
<!-- Show patient's medical journey -->
<div class="medical-timeline">
  <div class="timeline-item appointment">
    <h5>Appointment Completed</h5>
    <p>Dr. {{doctorName}} - {{date}}</p>
  </div>
  
  <div class="timeline-item record">
    <h5>Medical Record Created</h5>
    <p>Diagnosis: {{diagnosis}}</p>
  </div>
  
  <div class="timeline-item prescription">
    <h5>Prescription Added</h5>
    <p>{{medicineName}} - {{dosage}}</p>
  </div>
  
  <div class="timeline-item test">
    <h5>Test Prescribed</h5>
    <p>{{testName}} - {{status}}</p>
  </div>
  
  <div class="timeline-item result">
    <h5>Test Results Ready</h5>
    <p>View detailed results</p>
  </div>
</div>
```

---

## ✅ Implementation Checklist

### **Phase 1: Medical Records** (Week 1)
- [ ] Create `PatientMedicalHistory.html`
- [ ] Add fetch calls to get medical records
- [ ] Display diagnosis, notes, observations
- [ ] Link from appointment detail page

### **Phase 2: Prescriptions** (Week 2)
- [ ] Create `DoctorPrescriptions.html`
- [ ] Medicine search functionality
- [ ] Doctor prescribe medicine form
- [ ] Patient view prescriptions
- [ ] Status tracking (ACTIVE/COMPLETED/EXPIRED)

### **Phase 3: Tests** (Week 2)
- [ ] Create `PatientTests.html`
- [ ] Doctor prescribe test form
- [ ] Patient pending tests view
- [ ] Doctor enter test results
- [ ] Patient view test results

### **Phase 4: Doctor Locations** (Week 3)
- [ ] Create `DoctorLocations.html`
- [ ] Show all clinic locations
- [ ] Admin manage locations
- [ ] Book at specific location

### **Phase 5: Medicine Management** (Week 3)
- [ ] Create `MedicineManagement.html` (Admin only)
- [ ] CRUD operations for medicines
- [ ] Search & filter medicines
- [ ] Stock management

### **Phase 6: Integration** (Week 4)
- [ ] Add navigation links
- [ ] Create medical timeline view
- [ ] Add to patient dashboard
- [ ] Add to doctor dashboard

---

## 🎯 Quick Start

**1. Add to PatientMeets.html** (after booking):
```html
<button onclick="viewMedicalHistory()">📋 Medical History</button>
<button onclick="viewPrescriptions()">💊 My Prescriptions</button>
<button onclick="viewTests()">🔬 My Tests</button>
```

**2. Add to DoctorMeets.html** (after consultation):
```html
<div class="post-consultation">
  <button onclick="createMedicalRecord()">📝 Add Medical Record</button>
  <button onclick="prescribeMedicine()">💊 Prescribe Medicine</button>
  <button onclick="prescribeTest()">🔬 Prescribe Test</button>
</div>
```

**3. Update api.js**:
```javascript
// Add medical record
async function createMedicalRecord(appointmentId, diagnosis, notes, observations) {
  return await apiPost('/api/medical-records/create', {
    appointment: { id: appointmentId },
    diagnosis,
    notes,
    observations
  });
}

// Add similar functions for prescriptions and tests
```

---

**Status**: Ready for implementation
**Estimated Time**: 3-4 weeks
**Complexity**: Medium
