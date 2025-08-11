import React from 'react'
import { Routes, Route, Link, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Signup from './pages/Signup'
import Turfs from './pages/Turfs'
import TurfDetails from './pages/TurfDetails'
import Bookings from './pages/Bookings'
import AdminTurfs from './pages/AdminTurfs'
import AdminBookings from './pages/AdminBookings'
import Payment from './pages/Payment'
import { useAuth } from './auth'

function NavBar() {
  const { user, logout } = useAuth()
  return (
    <nav className="navbar navbar-expand navbar-dark bg-dark px-3">
      <Link to="/" className="navbar-brand">TurfEase</Link>
      <div className="navbar-nav">
        <Link to="/" className="nav-link">Browse</Link>
        {user && <Link to="/bookings" className="nav-link">My Bookings</Link>}
        {user?.role === 'ADMIN' && <Link to="/admin/turfs" className="nav-link">Admin Turfs</Link>}
        {user?.role === 'ADMIN' && <Link to="/admin/bookings" className="nav-link">Admin Bookings</Link>}
      </div>
      <div className="ms-auto navbar-nav">
        {!user && <Link to="/login" className="nav-link">Login</Link>}
        {!user && <Link to="/signup" className="nav-link">Signup</Link>}
        {user && <span className="navbar-text me-3">{user.email}</span>}
        {user && <button className="btn btn-outline-light" onClick={logout}>Logout</button>}
      </div>
    </nav>
  )
}

function ProtectedRoute({ children, roles }) {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" />
  if (roles && !roles.includes(user.role)) return <Navigate to="/" />
  return children
}

export default function App() {
  return (
    <div>
      <NavBar />
      <div className="container py-4">
        <Routes>
          <Route path="/" element={<Turfs />} />
          <Route path="/turfs/:id" element={<TurfDetails />} />
          <Route path="/payment/:bookingId" element={<ProtectedRoute><Payment /></ProtectedRoute>} />
          <Route path="/bookings" element={<ProtectedRoute><Bookings /></ProtectedRoute>} />
          <Route path="/admin/turfs" element={<ProtectedRoute roles={["ADMIN"]}><AdminTurfs /></ProtectedRoute>} />
          <Route path="/admin/bookings" element={<ProtectedRoute roles={["ADMIN"]}><AdminBookings /></ProtectedRoute>} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
        </Routes>
      </div>
    </div>
  )
}