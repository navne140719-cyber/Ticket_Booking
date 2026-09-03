import { useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();

  const token = localStorage.getItem("token");

  const userData = localStorage.getItem("user");

  let user = null;

  try {
    user = userData
      ? JSON.parse(userData)
      : null;
  } catch (error) {
    user = null;
  }


  // ==============================
  // LOGIN
  // ==============================

  const handleLogin = () => {
    navigate("/login");
  };


  // ==============================
  // SIGNUP
  // ==============================

  const handleSignup = () => {
    navigate("/signup");
  };


  // ==============================
  // ADMIN PORTAL
  // ==============================

  const handleAdminPortal = () => {
    navigate("/admin");
  };


  // ==============================
  // LOGOUT
  // ==============================

  const handleLogout = () => {

    localStorage.removeItem("token");
    localStorage.removeItem("user");

    navigate("/login");
  };


  return (

    <nav className="navbar">

      <div className="navbar-container">


        {/* ==============================
            LOGO
        ============================== */}

        <div
          className="logo"
          onClick={() => navigate("/")}
          style={{ cursor: "pointer" }}
        >

          <span className="logo-icon">
            ✦
          </span>

          <span>
            Ticket
            <span className="logo-highlight">
              Booking
            </span>
          </span>

        </div>


        {/* ==============================
            NAVIGATION LINKS
        ============================== */}

        <div className="nav-links">

          {/* MOVIES */}

          <a
            href="/#movies"
            onClick={(e) => {

              e.preventDefault();

              navigate("/");

              setTimeout(() => {

                document
                  .getElementById("movies")
                  ?.scrollIntoView({
                    behavior: "smooth"
                  });

              }, 100);

            }}
          >
            Movies
          </a>


          {/* MY BOOKINGS */}

          <a
            href="/bookings"
            onClick={(e) => {

              e.preventDefault();

              if (token) {

                navigate("/bookings");

              } else {

                navigate("/login");

              }

            }}
          >
            My Bookings
          </a>

        </div>


        {/* ==============================
            USER SECTION
        ============================== */}

        <div className="navbar-user">

          {token && user ? (

            <>


              {/* USER INFO */}

              <div className="user-info">

                <div className="user-avatar">

                  {user.name
                    ? user.name
                        .charAt(0)
                        .toUpperCase()
                    : "U"}

                </div>


                <div className="user-details">

                  <span className="user-greeting">
                    Hi,
                  </span>

                  <strong>
                    {user.name}
                  </strong>

                </div>

              </div>


              <div className="navbar-divider"></div>


              {/* ==============================
                  ADMIN PORTAL
                  ONLY ADMIN CAN SEE THIS
              ============================== */}

              {user.role === "ADMIN" && (

                <button
                  className="admin-button"
                  onClick={handleAdminPortal}
                >
                  Admin Portal
                </button>

              )}


              {/* ==============================
                  LOGOUT
              ============================== */}

              <button
                className="logout-button"
                onClick={handleLogout}
              >

                <span className="logout-icon">
                  ↪
                </span>

                Logout

              </button>


            </>

          ) : (

            /* ==============================
               LOGGED OUT
            ============================== */

            <div className="auth-buttons">

              <button
                className="signup-button"
                onClick={handleSignup}
              >
                Sign Up
              </button>


              <button
                className="login-button"
                onClick={handleLogin}
              >

                Login

                <span>
                  →
                </span>

              </button>

            </div>

          )}

        </div>

      </div>

    </nav>

  );
}

export default Navbar;