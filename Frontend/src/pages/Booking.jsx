import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;
function Booking() {

  const location = useLocation();
  const navigate = useNavigate();

  const movie = location.state?.movie;

  const [seats, setSeats] = useState(1);
  const [loading, setLoading] = useState(false);


  // No movie selected
  if (!movie) {
    return (
      <div className="app">

        <main>

          <section className="movies-section">

            <div className="no-movies">

              <div>🎬</div>

              <h3>No movie selected</h3>

              <p>
                Please select a movie first.
              </p>

              <button
                className="book-button"
                onClick={() => navigate("/")}
              >
                Browse Movies →
              </button>

            </div>

          </section>

        </main>

      </div>
    );
  }


  // Book ticket
  const bookTicket = async () => {

    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    if (seats <= 0) {
      alert("Please select at least one seat.");
      return;
    }

    if (seats > movie.availableSeats) {
      alert(
        `Only ${movie.availableSeats} seats are available.`
      );
      return;
    }

    try {

      setLoading(true);

      const response = await fetch(
        `${API_URL}/bookings`,
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },

          body: JSON.stringify({
            movieId: movie.id,
            seats: seats
          })
        }
      );

      if (!response.ok) {

        if (response.status === 401) {

          localStorage.removeItem("token");
          localStorage.removeItem("user");

          navigate("/login");

          return;
        }

        throw new Error("Booking failed");
      }

      const data = await response.json();

      console.log("Booking successful:", data);

      alert("🎉 Ticket booked successfully!");

      navigate("/bookings");

    } catch (error) {

      console.error("Booking error:", error);

      alert(
        "Unable to book ticket. Please try again."
      );

    } finally {

      setLoading(false);

    }
  };


  const totalPrice = movie.price * seats;


  return (
    <div className="app">

      <main>

        <section className="movies-section">

          <div className="booking-card">

            {/* POSTER */}

            <div className="booking-poster">

              {movie.posterUrl ? (

                <img
                  src={movie.posterUrl}
                  alt={`${movie.name} poster`}
                />

              ) : (

                <div className="booking-poster-placeholder">
                  🎬
                </div>

              )}

            </div>


            {/* DETAILS */}

            <div className="booking-details">

              <span className="movie-badge">
                {movie.theatre}
              </span>

              <h1>
                {movie.name}
              </h1>


              <div className="booking-info">

                <p>
                  🎭 Theatre: {movie.theatre}
                </p>

                <p>
                  ◷ Show Time: {movie.showTime}
                </p>

                <p>
                  💺 {movie.availableSeats} seats available
                </p>

                <p>
                  🎟️ Ticket Price: ₹{movie.price}
                </p>

              </div>


              {/* SEATS */}

              <div className="seat-selector">

                <label>
                  Number of seats
                </label>

                <div className="seat-controls">

                  <button
                    onClick={() =>
                      setSeats(
                        Math.max(1, seats - 1)
                      )
                    }
                  >
                    −
                  </button>

                  <span>
                    {seats}
                  </span>

                  <button
                    onClick={() =>
                      setSeats(
                        Math.min(
                          movie.availableSeats,
                          seats + 1
                        )
                      )
                    }
                  >
                    +
                  </button>

                </div>

              </div>


              {/* TOTAL */}

              <div className="booking-total">

                <span>
                  Total Amount
                </span>

                <strong>
                  ₹{totalPrice}
                </strong>

              </div>


              {/* BOOK */}

              <button
                className="book-button"
                onClick={bookTicket}
                disabled={
                  loading ||
                  movie.availableSeats === 0
                }
              >

                {loading
                  ? "Booking..."
                  : "Confirm Booking"}

                {!loading && (
                  <span>→</span>
                )}

              </button>


              {/* BACK */}

              <button
                className="back-button"
                onClick={() => navigate("/")}
              >
                ← Back to Movies
              </button>

            </div>

          </div>

        </section>

      </main>

    </div>
  );
}


// =================================================
// THIS LINE IS IMPORTANT
// =================================================

export default Booking;