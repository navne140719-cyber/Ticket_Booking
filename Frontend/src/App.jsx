import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Booking from "./pages/Booking";
import Bookings from "./pages/Bookings";

import "./App.css";

function App() {
  return (
    <BrowserRouter>

      <Routes>

        {/* =========================
            HOME / MOVIES
        ========================= */}

        <Route
          path="/"
          element={<Home />}
        />


        {/* =========================
            LOGIN
        ========================= */}

        <Route
          path="/Login"
          element={<Login />}
        />


        {/* =========================
            BOOK MOVIE
        ========================= */}

        <Route
          path="/booking"
          element={<Booking />}
        />


        {/* =========================
            MY BOOKINGS
        ========================= */}

        <Route
          path="/bookings"
          element={<Bookings />}
        />


        {/* =========================
            FALLBACK
        ========================= */}

        <Route
          path="*"
          element={<Home />}
        />

      </Routes>

    </BrowserRouter>
  );
}

export default App;