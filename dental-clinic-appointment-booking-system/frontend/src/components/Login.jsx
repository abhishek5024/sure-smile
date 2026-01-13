import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import "../styles/main.css";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (!email || !password) return alert("All fields required");
    try {
      await login(email, password, false); // Use context login for redirect
      // Redirect handled by AuthContext
    } catch (err) {
      setError(err.message || "Login failed");
    }
  };

  return (
    <div className="form-container">
      <h2>Patient Login</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Patient Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
      </form>
      <button onClick={() => navigate("/doctor/login")}>Login as Doctor</button>
      {error && <p className="error">{error}</p>}
    </div>
  );
}
