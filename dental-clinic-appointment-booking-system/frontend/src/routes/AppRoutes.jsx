import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../components/Login";
import DoctorLogin from "../components/DoctorLogin";
import ProtectedRoute from "../components/ProtectedRoute";

const PatientDashboard = () => <div>Patient Dashboard</div>;
const DoctorDashboard = () => <div>Doctor Dashboard</div>;

const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<Login />} />
    <Route path="/login" element={<Login />} />
    <Route path="/doctor/login" element={<DoctorLogin />} />
    <Route element={<ProtectedRoute allowedRoles={["PATIENT"]} />}>
      <Route path="/patient/dashboard" element={<PatientDashboard />} />
    </Route>
    <Route element={<ProtectedRoute allowedRoles={["DOCTOR"]} />}>
      <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
    </Route>
    <Route path="*" element={<Login />} />
  </Routes>
);

export default AppRoutes;
