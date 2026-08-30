import { useEffect, useState } from "react";

import Navbar from "../components/Navbar";
import MovieCard from "../components/MovieCard";

const API_URL = "http://localhost:8081";

function Home() {

  const [movies, setMovies] = useState([]);

  const [search, setSearch] = useState("");

  const [minPrice, setMinPrice] = useState("");

  const [maxPrice, setMaxPrice] = useState("");

  const [loading, setLoading] = useState(true);


  // =====================================================
  // LOAD ALL MOVIES
  // =====================================================

  const fetchMovies = async () => {

    try {

      setLoading(true);

      const response =
        await fetch(`${API_URL}/movies`);


      if (!response.ok) {

        throw new Error(
          "Failed to fetch movies"
        );

      }


      const data =
        await response.json();


      setMovies(data);

    } catch (error) {

      console.error(
        "Movie fetch error:",
        error
      );

    } finally {

      setLoading(false);

    }
  };


  // =====================================================
  // SEARCH MOVIES
  // =====================================================

  const searchMovies = async () => {

    if (!search.trim()) {

      fetchMovies();

      return;
    }


    try {

      setLoading(true);


      const response =
        await fetch(
          `${API_URL}/movies/search?name=${encodeURIComponent(
            search
          )}`
        );


      if (!response.ok) {

        throw new Error(
          "Search failed"
        );

      }


      const data =
        await response.json();


      setMovies(data);

    } catch (error) {

      console.error(
        "Search error:",
        error
      );

    } finally {

      setLoading(false);

    }
  };


  // =====================================================
  // FILTER MOVIES BY PRICE
  // =====================================================

  const filterMovies = async () => {

    if (
      minPrice === "" &&
      maxPrice === ""
    ) {

      fetchMovies();

      return;
    }


    const min =
      minPrice === ""
        ? 0
        : minPrice;


    const max =
      maxPrice === ""
        ? 100000
        : maxPrice;


    try {

      setLoading(true);


      const response =
        await fetch(
          `${API_URL}/movies/filter?minPrice=${min}&maxPrice=${max}`
        );


      if (!response.ok) {

        throw new Error(
          "Filter failed"
        );

      }


      const data =
        await response.json();


      setMovies(data);

    } catch (error) {

      console.error(
        "Filter error:",
        error
      );

    } finally {

      setLoading(false);

    }
  };


  // =====================================================
  // CLEAR SEARCH / FILTER
  // =====================================================

  const clearFilters = () => {

    setSearch("");

    setMinPrice("");

    setMaxPrice("");

    fetchMovies();

  };


  // =====================================================
  // LOAD MOVIES WHEN PAGE OPENS
  // =====================================================

  useEffect(() => {

    fetchMovies();

  }, []);


  // =====================================================
  // UI
  // =====================================================

  return (

    <div className="app">

      {/* =========================
          NAVBAR
      ========================= */}

      <Navbar />


      <main>


        {/* =========================
            HERO
        ========================= */}

        <section className="hero-section">

          <div className="hero-content">


            <p className="hero-tag">
              🎬 YOUR MOVIE EXPERIENCE
            </p>


            <h1>

              Movies worth

              <span>
                watching.
              </span>

            </h1>


            <p className="hero-description">

              Discover the latest movies,
              choose your seats and book
              your perfect movie experience.

            </p>


            <button
              className="explore-button"

              onClick={() => {

                document
                  .getElementById("movies")
                  ?.scrollIntoView({
                    behavior: "smooth"
                  });

              }}
            >

              Explore Movies

              <span>
                ↓
              </span>

            </button>

          </div>

        </section>


        {/* =========================
            MOVIES SECTION
        ========================= */}

        <section
          className="movies-section"
          id="movies"
        >


          {/* =========================
              SECTION HEADING
          ========================= */}

          <div className="section-heading">


            <div>

              <p className="section-label">
                NOW SHOWING
              </p>


              <h2>
                Choose your movie
              </h2>

            </div>


            <p className="movie-count">

              {movies.length}

              {" "}

              {movies.length === 1
                ? "movie"
                : "movies"
              }

              {" "}
              available

            </p>

          </div>


          {/* =========================
              SEARCH + FILTER
          ========================= */}

          <div className="movie-controls">


            {/* SEARCH */}

            <div className="search-box">

              <span>
                🔍
              </span>


              <input
                type="text"

                placeholder="Search movies..."

                value={search}

                onChange={(e) =>
                  setSearch(
                    e.target.value
                  )
                }

                onKeyDown={(e) => {

                  if (
                    e.key === "Enter"
                  ) {

                    searchMovies();

                  }

                }}
              />


              <button
                onClick={searchMovies}
              >

                Search

              </button>

            </div>


            {/* PRICE FILTER */}

            <div className="price-filter">


              <input
                type="number"

                placeholder="Min ₹"

                value={minPrice}

                onChange={(e) =>
                  setMinPrice(
                    e.target.value
                  )
                }
              />


              <span>
                —
              </span>


              <input
                type="number"

                placeholder="Max ₹"

                value={maxPrice}

                onChange={(e) =>
                  setMaxPrice(
                    e.target.value
                  )
                }
              />


              <button
                onClick={filterMovies}
              >

                Filter

              </button>

            </div>


            {/* CLEAR */}

            <button
              className="clear-button"

              onClick={clearFilters}
            >

              Clear

            </button>

          </div>


          {/* =========================
              MOVIE RESULTS
          ========================= */}

          {loading ? (

            <div className="loading">

              Loading movies...

            </div>

          ) : movies.length === 0 ? (

            <div className="no-movies">


              <div>
                🎬
              </div>


              <h3>
                No movies found
              </h3>


              <p>
                Try another search
                or price range.
              </p>


            </div>

          ) : (

            <div className="movie-list">

              {movies.map(
                (movie) => (

                  <MovieCard
                    key={movie.id}
                    movie={movie}
                  />

                )
              )}

            </div>

          )}

        </section>

      </main>

    </div>

  );
}

export default Home;