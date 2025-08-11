import React, { createContext, useContext, useEffect, useState } from 'react'
import { api } from './api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)

  useEffect(() => {
    const token = localStorage.getItem('token')
    const role = localStorage.getItem('role')
    const email = localStorage.getItem('email')
    if (token && email && role) {
      setUser({ email, role, token })
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  }, [])

  const login = (data) => {
    localStorage.setItem('token', data.token)
    localStorage.setItem('role', data.role)
    localStorage.setItem('email', data.email)
    setUser({ email: data.email, role: data.role, token: data.token })
    api.defaults.headers.common['Authorization'] = `Bearer ${data.token}`
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('email')
    setUser(null)
    delete api.defaults.headers.common['Authorization']
  }

  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}