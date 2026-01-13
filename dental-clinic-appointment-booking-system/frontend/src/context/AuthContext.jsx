import React, { createContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();
  const [auth, setAuth] = useState(() => {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    const role = localStorage.getItem("role");
    return token && userId && role ? { token, userId, role } : null;
  });

  useEffect(() => {
    if (auth) {
      localStorage.setItem("token", auth.token);
      localStorage.setItem("userId", auth.userId);
      localStorage.setItem("role", auth.role);
    } else {
      localStorage.removeItem("token");
      localStorage.removeItem("userId");
      localStorage.removeItem("role");
    }
  }, [auth]);

  const login = async (email, password, isDoctor = false) => {
    console.log("AuthContext login called", { email, password, isDoctor });
    try {
      const endpoint = isDoctor ? "/auth/doctor/login" : "/auth/login";
      const response = await api.post(endpoint, {
        email,
        password,
      });
      console.log("API response", response);
      const { token, role, userId } = response.data;
      setAuth({ token, userId, role });
      if (role === "PATIENT") navigate("/patient/dashboard");
      else if (role === "DOCTOR") navigate("/doctor/dashboard");
    } catch (err) {
      console.log("API error", err);
      throw err;
    }
  };

  const logout = () => {
    setAuth(null);
    navigate("/login");
  };

  return (
    <AuthContext.Provider value={{ auth, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
