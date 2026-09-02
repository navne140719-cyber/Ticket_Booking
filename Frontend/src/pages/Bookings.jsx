import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;
function Bookings() {

  const navigate = useNavigate();

  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [unauthorized, setUnauthorized] = useState(false);

  useEffect(() => {

    const fetchBookings = async () => {

      const token = localStorage.getItem("token");

      console.log("BOOKINGS TOKEN:", token);

      // No token
      if (!token) {
        setUnauthorized(true);
        setLoading(false);
        return;
      }

      try {

        const response = await fetch(
          `${API_URL}/bookings`,
          {
            method: "GET",

            headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          }
        );

        console.log(
          "BOOKINGS STATUS:",
          response.status
        );


        if (response.status === 401 || response.status === 403) {

          console.error(
            "JWT rejected by backend"
          );

          setUnauthorized(false);

          setError(
            "Your login token was rejected by the server."
          );

          setLoading(false);

          // IMPORTANT:
          // Don't delete token yet.
          // We need to see why backend rejected it.

          return;
        }


        if (!response.ok) {

          throw new Error(
            "Failed to fetch bookings"
          );

        }


        const data = await response.json();

        console.log(
          "BOOKINGS RESPONSE:",
          data
        );

        setBookings(data || []);

      } catch (error) {

        console.error(
          "BOOKINGS ERROR:",
          error
        );

        setError(
          error.message ||
          "Unable to load bookings."
        );

      } finally {

        setLoading(false);

      }

    };

    fetchBookings();

  }, []);


  // =====================================
  // NO TOKEN
  // =====================================

  if (unauthorized) {

    return (

      <div className="bookings-page">

        <div className="bookings-card">

          <div className="booking-empty-icon">
            🔐
          </div>

          <h2>
            Login Required
          </h2>

          <p>
            Please login to view your bookings.
          </p>

          <button
            className="booking-primary-button"
            onClick={() => navigate("/login")}
          >
            Go to Login
          </button>

          <button
            className="booking-back-button"
            onClick={() => navigate("/")}
          >
            ← Back to Movies
          </button>

        </div>

      </div>

    );
  }


  // =====================================
  // LOADING
  // =====================================

  if (loading) {

    return (

      <div className="bookings-page">

        <div className="bookings-card">

          <div className="booking-loading">
            Loading your bookings...
          </div>

        </div>

      </div>

    );

  }


  // =====================================
  // BACKEND AUTH ERROR
  // =====================================

  if (error) {

    return (

      <div className="bookings-page">

        <div className="bookings-card">

          <div className="booking-empty-icon">
            ⚠️
          </div>

          <h2>
            Unable to load bookings
          </h2>

          <p>
            {error}
          </p>

          <button
            className="booking-primary-button"
            onClick={() => navigate("/")}
          >
            ← Back to Movies
          </button>

        </div>

      </div>

    );

  }


  // =====================================
  // NO BOOKINGS
  // =====================================

  if (bookings.length === 0) {

    return (

      <div className="bookings-page">

        <div className="bookings-header">

          <div>

            <p className="section-label">
              YOUR ACTIVITY
            </p>

            <h1>
              My Bookings
            </h1>

          </div>

          <button
            className="booking-back-top"
            onClick={() => navigate("/")}
          >
            ← Movies
          </button>

        </div>


        <div className="bookings-empty">

          <div className="booking-empty-icon">
            🎟️
          </div>

          <h2>
            No bookings yet
          </h2>

          <p>
            You haven't booked any movies yet.
          </p>

          <button
            className="booking-primary-button"
            onClick={() => navigate("/")}
          >
            Explore Movies
          </button>

        </div>

      </div>

    );

  }


  // =====================================
  // BOOKINGS
  // =====================================

  return (

    <div className="bookings-page">

      <div className="bookings-header">

        <div>

          <p className="section-label">
            YOUR ACTIVITY
          </p>

          <h1>
            My Bookings
          </h1>

          <p className="bookings-subtitle">
            Your movie tickets and booking history.
          </p>

        </div>

        <button
          className="booking-back-top"
          onClick={() => navigate("/")}
        >
          ← Movies
        </button>

      </div>


      <div className="booking-list">

        {bookings.map((booking) => (

          <article
            className="booking-item"
            key={booking.bookingId}
          >

            <div className="booking-icon">
              🎬
            </div>

            <div className="booking-details">

              <h2>
                {booking.movieName}
              </h2>

              <div className="booking-info">

                <span>
                  🎭 {booking.theatre}
                </span>

                <span>
                  🕐 {booking.showTime}
                </span>

                <span>
                  💺 {booking.seats} seat
                  {booking.seats !== 1 ? "s" : ""}
                </span>

              </div>

            </div>

            <div className="booking-price">

              <span>
                TOTAL
              </span>

              <strong>
                ₹{booking.totalPrice}
              </strong>

            </div>

          </article>

        ))}

      </div>


      <button
        className="booking-back-button bottom"
        onClick={() => navigate("/")}
      >
      </button>

    </div>

  );
}

export default Bookings;