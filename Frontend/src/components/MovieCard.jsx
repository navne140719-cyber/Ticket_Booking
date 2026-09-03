import { useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

function MovieCard({ movie }) {

  const soldOut = movie.availableSeats === 0;

  // Booking states
  const [showBooking, setShowBooking] = useState(false);
  const [seats, setSeats] = useState(1);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [bookingMessage, setBookingMessage] = useState("");
  const [bookingError, setBookingError] = useState("");

  // OPEN BOOKING
  const handleOpenBooking = () => {

    const token = localStorage.getItem("token");

    // User must be logged in
    if (!token) {
      alert("Please login before booking a ticket.");
      return;
    }

    setSeats(1);
    setBookingMessage("");
    setBookingError("");
    setShowBooking(true);
  };

  // CLOSE BOOKING
  const handleCloseBooking = () => {

    if (bookingLoading) {
      return;
    }

    setShowBooking(false);
    setBookingMessage("");
    setBookingError("");
  };

  // DECREASE SEATS
  const decreaseSeats = () => {

    if (seats > 1) {
      setSeats(seats - 1);
    }
  };

  // INCREASE SEATS
  const increaseSeats = () => {

    if (seats < movie.availableSeats) {
      setSeats(seats + 1);
    }
  };

  // CONFIRM BOOKING
  const handleConfirmBooking = async () => {

    const token = localStorage.getItem("token");

    if (!token) {
      setBookingError(
        "Please login before booking a ticket."
      );
      return;
    }

    if (seats <= 0) {
      setBookingError(
        "Please select at least one seat."
      );
      return;
    }

    if (seats > movie.availableSeats) {
      setBookingError(
        "Not enough seats available."
      );
      return;
    }

    try {

      setBookingLoading(true);
      setBookingError("");
      setBookingMessage("");

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

      const data = await response.json();

      if (!response.ok) {

        throw new Error(
          data.message ||
          data.error ||
          "Booking failed"
        );

      }

      console.log(
        "BOOKING SUCCESSFUL:",
        data
      );

      setBookingMessage(
        "Ticket booked successfully!"
      );

      // Give the user a moment to see success message
      setTimeout(() => {
        setShowBooking(false);

        // Reload movies so available seats update
        window.location.reload();

      }, 1200);

    } catch (error) {

      console.error(
        "BOOKING ERROR:",
        error
      );

      setBookingError(
        error.message ||
        "Booking failed. Please try again."
      );

    } finally {

      setBookingLoading(false);

    }
  };

  const totalPrice = seats * movie.price;

  return (

    <>

      <article className="movie-card">

        <div className="movie-poster">

          <div className="poster-overlay">

            <span className="movie-badge">
              {movie.theatre}
            </span>

          </div>

          {movie.posterUrl ? (

            <img
              src={movie.posterUrl}
              alt={`${movie.name} poster`}
              className="movie-poster-image"
              onError={(e) => {
                e.currentTarget.style.display = "none";
              }}
            />

          ) : (

            <div className="poster-icon">
              🎬
            </div>

          )}

        </div>


        <div className="movie-content">

          <h3>
            {movie.name}
          </h3>


          <div className="movie-info">

            <p>
              <span>🎭</span>
              Theatre: {movie.theatre}
            </p>

            <p>
              <span>◷</span>
              Show Time: {movie.showTime}
            </p>

            <p>
              <span>💺</span>

              {soldOut
                ? "0 seats available"
                : `${movie.availableSeats} seats available`}
            </p>

          </div>


          <div className="movie-bottom">

            <div>

              <span className="seats-label">
                Ticket Price
              </span>

              <strong
                className={
                  soldOut
                    ? "sold-out-text"
                    : ""
                }
              >
                ₹{movie.price}
              </strong>

            </div>

          </div>


          <button
            className={`book-button ${
              soldOut ? "disabled" : ""
            }`}
            disabled={soldOut}
            onClick={handleOpenBooking}
          >

            {soldOut
              ? "Sold Out"
              : "Book Ticket"}

            {!soldOut && (
              <span>→</span>
            )}

          </button>

        </div>

      </article>


      {/* =========================
          BOOKING MODAL
      ========================= */}

      {showBooking && (

        <div className="booking-modal-overlay">

          <div className="booking-modal">

            {/* Close button */}

            <button
              className="booking-close-button"
              onClick={handleCloseBooking}
              disabled={bookingLoading}
            >
              ×
            </button>


            {/* Heading */}

            <p className="booking-modal-label">
              BOOK TICKETS
            </p>

            <h2>
              {movie.name}
            </h2>


            <div className="booking-movie-details">

              <p>
                📍 {movie.theatre}
              </p>

              <p>
                🕐 {movie.showTime}
              </p>

              <p>
                💰 ₹{movie.price} per ticket
              </p>

            </div>


            {/* Seat selection */}

            <div className="seat-selection">

              <span>
                Number of tickets
              </span>


              <div className="seat-controls">

                <button
                  type="button"
                  onClick={decreaseSeats}
                  disabled={
                    seats <= 1 ||
                    bookingLoading
                  }
                >
                  −
                </button>


                <strong>
                  {seats}
                </strong>


                <button
                  type="button"
                  onClick={increaseSeats}
                  disabled={
                    seats >= movie.availableSeats ||
                    bookingLoading
                  }
                >
                  +
                </button>

              </div>

            </div>


            <p className="booking-available">

              {movie.availableSeats} seats currently available

            </p>


            {/* Total */}

            <div className="booking-total">

              <span>
                Total
              </span>

              <strong>
                ₹{totalPrice}
              </strong>

            </div>


            {/* Success */}

            {bookingMessage && (

              <div className="booking-success">
                ✓ {bookingMessage}
              </div>

            )}


            {/* Error */}

            {bookingError && (

              <div className="booking-error">
                {bookingError}
              </div>

            )}


            {/* Confirm */}

            <button
              className="confirm-booking-button"
              onClick={handleConfirmBooking}
              disabled={bookingLoading}
            >

              {bookingLoading
                ? "Booking..."
                : "Confirm Booking"}

            </button>


            {/* Cancel */}

            <button
              className="cancel-booking-button"
              onClick={handleCloseBooking}
              disabled={bookingLoading}
            >
              Cancel
            </button>

          </div>

        </div>

      )}

    </>

  );
}

export default MovieCard;