import React, { createContext } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();

  const login = async (email, password, isDoctor) => {
    const endpoint = isDoctor ? "/auth/doctor/login" : "/auth/login";
    try {
      const response = await api.post(endpoint, { email, password });
      const { token, role, userId } = response.data;
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
      localStorage.setItem("userId", userId);
      if (role === "DOCTOR") {
        navigate("/doctor/dashboard");
      } else {
        navigate("/patient/dashboard");
      }
    } catch (error) {
      // Handle error (show message, etc.)
    }
  };

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <AuthContext.Provider value={{ login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};