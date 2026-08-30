import { useState } from "react";
import { useNavigate } from "react-router-dom";

const API_URL = "http://localhost:8081";

function Login() {

  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");


  const handleLogin = async (e) => {

    e.preventDefault();

    setError("");

    if (!email.trim() || !password.trim()) {
      setError("Please enter email and password.");
      return;
    }

    try {

      setLoading(true);

      const response = await fetch(
        `${API_URL}/users/login`,
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json"
          },

          body: JSON.stringify({
            email: email.trim(),
            password: password
          })
        }
      );


      // Read response ONCE
      const data = await response.json();

      console.log("LOGIN STATUS:", response.status);
      console.log("LOGIN RESPONSE:", data);


      if (!response.ok) {

        throw new Error(
          data.message ||
          data.error ||
          "Invalid email or password"
        );

      }


      // Check token

      if (!data.token) {

        console.error(
          "Backend response does not contain token:",
          data
        );

        throw new Error(
          "Login successful but server did not return a token."
        );
      }


      // =====================================
      // SAVE JWT
      // =====================================

      localStorage.setItem(
        "token",
        data.token
      );


      // =====================================
      // SAVE USER
      // =====================================

      const user = {
        id: data.id,
        name: data.name,
        email: data.email
      };

      localStorage.setItem(
        "user",
        JSON.stringify(user)
      );


      console.log("JWT SAVED");
      console.log("USER SAVED:", user);


      // =====================================
      // GO HOME
      // =====================================

      navigate("/");

    } catch (error) {

      console.error(
        "LOGIN ERROR:",
        error
      );

      setError(
        error.message ||
        "Login failed."
      );

    } finally {

      setLoading(false);

    }

  };


  return (

    <div className="login-page">

      <div className="login-card">


        {/* LOGO */}

        <div className="login-logo">

          <div className="login-logo-icon">
            ✦
          </div>

          <h1>
            Ticket<span>Booking</span>
          </h1>

        </div>


        {/* TITLE */}

        <h2>
          Welcome back
        </h2>

        <p className="login-subtitle">
          Login to manage your movie bookings.
        </p>


        {/* ERROR */}

        {error && (

          <div className="login-error">
            {error}
          </div>

        )}


        {/* FORM */}

        <form onSubmit={handleLogin}>


          {/* EMAIL */}

          <div className="form-group">

            <label>
              Email
            </label>

            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) =>
                setEmail(e.target.value)
              }
              autoComplete="email"
            />

          </div>


          {/* PASSWORD */}

          <div className="form-group">

            <label>
              Password
            </label>

            <input
              type="password"
              placeholder="Enter your password"
              value={password}
              onChange={(e) =>
                setPassword(e.target.value)
              }
              autoComplete="current-password"
            />

          </div>


          {/* LOGIN BUTTON */}

          <button
            type="submit"
            className="login-submit"
            disabled={loading}
          >

            {loading
              ? "Logging in..."
              : "Login"}

            {!loading && (
              <span style={{ marginLeft: "8px" }}>
                →
              </span>
            )}

          </button>

        </form>


        {/* BACK */}

{/*         <button */}
{/*           type="button" */}
{/*           className="back-home" */}
{/*           onClick={() => navigate("/")} */}
{/*         > */}
{/*         </button> */}


      </div>

    </div>

  );
}

export default Login;