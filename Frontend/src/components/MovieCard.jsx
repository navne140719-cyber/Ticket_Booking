function MovieCard({ movie }) {

  const soldOut = movie.availableSeats === 0;

  return (
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
          <div className="poster-icon">🎬</div>
        )}

      </div>

      <div className="movie-content">

        <h3>{movie.name}</h3>

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

            <strong className={soldOut ? "sold-out-text" : ""}>
              ₹{movie.price}
            </strong>
          </div>

        </div>

        <button
          className={`book-button ${soldOut ? "disabled" : ""}`}
          disabled={soldOut}
        >
          {soldOut ? "Sold Out" : "Book Ticket"}

          {!soldOut && <span>→</span>}
        </button>

      </div>

    </article>
  );
}

export default MovieCard;