import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;

function Signup() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();

    setError("");
    setSuccess("");

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    if (password.length < 6) {
      setError("Password must be at least 6 characters");
      return;
    }

    try {
      const response = await fetch(`${API_URL}/users`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          name,
          email,
          password
        })
      });

      if (!response.ok) {
        throw new Error("Signup failed");
      }

      setSuccess("Account created successfully!");

      setTimeout(() => {
        navigate("/Login");
      }, 1000);

    } catch (error) {
      setError("Unable to create account. Email may already exist.");
    }
  };

  return (
    <div className="signup-page">

      <div className="signup-card">

        <h2>Create Account</h2>

        <form
          className="signup-form"
          onSubmit={handleSignup}
        >

          {/* NAME */}

          <div className="signup-field">

            <label>Name</label>

            <input
              type="text"
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />

          </div>


          {/* EMAIL */}

          <div className="signup-field">

            <label>Email</label>

            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

          </div>


          {/* PASSWORD */}

          <div className="signup-field">

            <label>Password</label>

            <input
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

          </div>


          {/* CONFIRM PASSWORD */}

          <div className="signup-field">

            <label>Confirm Password</label>

            <input
              type="password"
              placeholder="Confirm your password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />

          </div>


          {/* ERROR */}

          {error && (
            <p className="signup-error">
              {error}
            </p>
          )}


          {/* SUCCESS */}

          {success && (
            <p className="signup-success">
              {success}
            </p>
          )}


          {/* SIGN UP BUTTON */}

          <button className="signup-button-main" type="submit">
            Sign Up
          </button>
        </form>

        {/* LOGIN LINK */}
        <p className="signup-login-text">
          Already have an account?{" "}
          <Link to="/Login">
            Login
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Signup;

