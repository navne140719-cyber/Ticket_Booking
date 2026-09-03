import { useEffect, useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

function Admin() {

  const [movies, setMovies] = useState([]);

  // Track which movie is being edited
  const [editingMovieId, setEditingMovieId] = useState(null);

  // Movie form states
  const [name, setName] = useState("");
  const [theatre, setTheatre] = useState("");
  const [showTime, setShowTime] = useState("");
  const [totalSeats, setTotalSeats] = useState("");
  const [availableSeats, setAvailableSeats] = useState("");
  const [price, setPrice] = useState("");
  const [posterUrl, setPosterUrl] = useState("");

  useEffect(() => {
    fetchMovies();
  }, []);

  // FETCH MOVIES
  const fetchMovies = async () => {

    try {

      const response = await fetch(
        `${API_URL}/movies`
      );

      const data = await response.json();

      setMovies(data);

    } catch (error) {

      console.error(
        "ERROR FETCHING MOVIES:",
        error
      );

    }
  };

  // ADD MOVIE
  const handleAddMovie = async (e) => {

    e.preventDefault();

    const token = localStorage.getItem("token");

    try {

      const response = await fetch(
        `${API_URL}/movies`,
        {
          method: "POST",

          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },

          body: JSON.stringify({
            name: name,
            theatre: theatre,
            showTime: showTime,
            totalSeats: Number(totalSeats),
            availableSeats: Number(availableSeats),
            price: Number(price),
            posterUrl: posterUrl
          })
        }
      );

      if (!response.ok) {
        throw new Error("Failed to add movie");
      }

      const newMovie = await response.json();

      console.log(
        "MOVIE ADDED:",
        newMovie
      );

      await fetchMovies();

      // Clear form
      clearForm();

    } catch (error) {

      console.error(
        "ERROR ADDING MOVIE:",
        error
      );

    }
  };

  // EDIT MOVIE
  const handleEditMovie = (movie) => {

    // Store the movie ID
    setEditingMovieId(movie.id);

    // Fill the form with existing movie data
    setName(movie.name);
    setTheatre(movie.theatre);
    setShowTime(movie.showTime);
    setTotalSeats(movie.totalSeats);
    setAvailableSeats(movie.availableSeats);
    setPrice(movie.price);
    setPosterUrl(movie.posterUrl || "");

    // Scroll to the form
    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  };

  // UPDATE MOVIE
  const handleUpdateMovie = async (e) => {

    e.preventDefault();

    const token = localStorage.getItem("token");

    try {

      const response = await fetch(
        `${API_URL}/movies/${editingMovieId}`,
        {
          method: "PUT",

          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },

          body: JSON.stringify({
            name: name,
            theatre: theatre,
            showTime: showTime,
            totalSeats: Number(totalSeats),
            availableSeats: Number(availableSeats),
            price: Number(price),
            posterUrl: posterUrl
          })
        }
      );

      if (!response.ok) {
        throw new Error("Failed to update movie");
      }

      const updatedMovie = await response.json();

      console.log(
        "MOVIE UPDATED:",
        updatedMovie
      );

      // Refresh movie list
      await fetchMovies();

      // Exit edit mode
      setEditingMovieId(null);

      // Clear form
      clearForm();

    } catch (error) {

      console.error(
        "ERROR UPDATING MOVIE:",
        error
      );

    }
  };

  // DELETE MOVIE
  const handleDeleteMovie = async (id) => {

    const confirmed = window.confirm(
      "Are you sure you want to delete this movie?"
    );

    if (!confirmed) {
      return;
    }

    const token = localStorage.getItem("token");

    try {

      const response = await fetch(
        `${API_URL}/movies/${id}`,
        {
          method: "DELETE",

          headers: {
            "Authorization": `Bearer ${token}`
          }
        }
      );

      if (!response.ok) {
        throw new Error("Failed to delete movie");
      }

      console.log(
        "MOVIE DELETED:",
        id
      );

      await fetchMovies();

    } catch (error) {

      console.error(
        "ERROR DELETING MOVIE:",
        error
      );

    }
  };

  // CLEAR FORM
  const clearForm = () => {

    setName("");
    setTheatre("");
    setShowTime("");
    setTotalSeats("");
    setAvailableSeats("");
    setPrice("");
    setPosterUrl("");
    setEditingMovieId(null);

  };

  // CANCEL EDIT
  const handleCancelEdit = () => {

    clearForm();

  };

  return (

    <div className="admin-page">

      {/* Page Header */}

      <div className="admin-header">

        <div>

          <p className="admin-label">
            ADMIN DASHBOARD
          </p>

          <h1>
            Movie Management
          </h1>

          <p className="admin-description">
            Add and manage movies available for booking.
          </p>

        </div>

        <div className="movie-count">

          <span>
            {movies.length}
          </span>

          <small>
            Movies
          </small>

        </div>

      </div>


      {/* Add / Edit Movie Section */}

      <div className="admin-section">

        <div className="section-header">

          <div>

            <h2>
              {editingMovieId !== null
                ? "Edit Movie"
                : "Add New Movie"}
            </h2>

            <p>
              {editingMovieId !== null
                ? "Update the movie details."
                : "Add a movie to the booking platform."}
            </p>

          </div>

        </div>


        <form
          className="movie-form"
          onSubmit={
            editingMovieId !== null
              ? handleUpdateMovie
              : handleAddMovie
          }
        >

          {/* Movie Name + Theatre */}

          <div className="form-row">

            <div className="admin-form-group">

              <label>
                Movie Name
              </label>

              <input
                type="text"
                value={name}
                onChange={(e) =>
                  setName(e.target.value)
                }
                placeholder="e.g. The Dark Knight"
                required
              />

            </div>


            <div className="admin-form-group">

              <label>
                Theatre
              </label>

              <input
                type="text"
                value={theatre}
                onChange={(e) =>
                  setTheatre(e.target.value)
                }
                placeholder="e.g. PVR Cinemas"
                required
              />

            </div>

          </div>


          {/* Show Time + Price */}

          <div className="form-row">

            <div className="admin-form-group">

              <label>
                Show Time
              </label>

              <input
                type="text"
                value={showTime}
                onChange={(e) =>
                  setShowTime(e.target.value)
                }
                placeholder="e.g. 7:00 PM"
                required
              />

            </div>


            <div className="admin-form-group">

              <label>
                Price (₹)
              </label>

              <input
                type="number"
                value={price}
                onChange={(e) =>
                  setPrice(e.target.value)
                }
                placeholder="e.g. 250"
                min="0"
                required
              />

            </div>

          </div>


          {/* Total Seats + Available Seats */}

          <div className="form-row">

            <div className="admin-form-group">

              <label>
                Total Seats
              </label>

              <input
                type="number"
                value={totalSeats}
                onChange={(e) =>
                  setTotalSeats(e.target.value)
                }
                placeholder="e.g. 100"
                min="1"
                required
              />

            </div>


            <div className="admin-form-group">

              <label>
                Available Seats
              </label>

              <input
                type="number"
                value={availableSeats}
                onChange={(e) =>
                  setAvailableSeats(e.target.value)
                }
                placeholder="e.g. 100"
                min="0"
                required
              />

            </div>

          </div>


          {/* Poster URL */}

          <div className="admin-form-group">

            <label>
              Poster URL
            </label>

            <input
              type="url"
              value={posterUrl}
              onChange={(e) =>
                setPosterUrl(e.target.value)
              }
              placeholder="https://example.com/poster.jpg"
            />

          </div>


          {/* Form Buttons */}

          <div className="movie-form-actions">

            <button
              type="submit"
              className="add-movie-button"
            >

              <span>
                {editingMovieId !== null
                  ? "✓"
                  : "＋"}
              </span>

              {editingMovieId !== null
                ? "Save Changes"
                : "Add Movie"}

            </button>


            {editingMovieId !== null && (

              <button
                type="button"
                className="edit-movie-button"
                onClick={handleCancelEdit}
              >
                Cancel
              </button>

            )}

          </div>

        </form>

      </div>


      {/* Movies Section */}

      <div className="admin-section">

        <div className="section-header">

          <div>

            <h2>
              Current Movies
            </h2>

            <p>
              Manage movies currently available on the platform.
            </p>

          </div>

        </div>


        <div className="admin-movie-grid">

          {movies.map((movie) => (

            <div
              className="admin-movie-card"
              key={movie.id}
            >

              {/* Poster */}

              <div className="admin-poster">

                {movie.posterUrl ? (

                  <img
                    src={movie.posterUrl}
                    alt={movie.name}
                  />

                ) : (

                  <div className="no-poster">
                    🎬
                  </div>

                )}

              </div>


              {/* Movie Information */}

              <div className="admin-movie-content">

                <h3>
                  {movie.name}
                </h3>

                <p className="movie-theatre">
                  📍 {movie.theatre}
                </p>

                <p className="movie-time">
                  🕐 {movie.showTime}
                </p>


                {/* Movie Stats */}

                <div className="movie-stats">

                  <div>

                    <span>
                      Price
                    </span>

                    <strong>
                      ₹{movie.price}
                    </strong>

                  </div>


                  <div>

                    <span>
                      Available
                    </span>

                    <strong>
                      {movie.availableSeats}
                    </strong>

                  </div>


                  <div>

                    <span>
                      Total
                    </span>

                    <strong>
                      {movie.totalSeats}
                    </strong>

                  </div>

                </div>


                {/* Buttons */}

                <div className="movie-actions">

                  {/* EDIT */}

                  <button
                    className="edit-movie-button"
                    type="button"
                    onClick={() =>
                      handleEditMovie(movie)
                    }
                  >
                    ✎ Edit
                  </button>


                  {/* DELETE */}

                  <button
                    className="delete-movie-button"
                    type="button"
                    onClick={() =>
                      handleDeleteMovie(movie.id)
                    }
                  >
                    🗑 Delete
                  </button>

                </div>

              </div>

            </div>

          ))}

        </div>

      </div>

    </div>
  );
}

export default Admin;