import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api } from '../api'
import { useAuth } from '../auth'

export default function Signup(){
  const [fullName, setFullName] = useState('New User')
  const [email, setEmail] = useState('newuser@example.com')
  const [password, setPassword] = useState('User@1234')
  const [error, setError] = useState('')
  const { login } = useAuth()
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      const { data } = await api.post('/api/auth/user/signup', { fullName, email, password })
      login(data)
      navigate('/')
    } catch (e) {
      setError('Signup failed')
    }
  }

  return (
    <div className="row justify-content-center">
      <div className="col-md-4">
        <h3>Signup</h3>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={submit}>
          <div className="mb-3">
            <label className="form-label">Full Name</label>
            <input className="form-control" value={fullName} onChange={e=>setFullName(e.target.value)} />
          </div>
          <div className="mb-3">
            <label className="form-label">Email</label>
            <input className="form-control" value={email} onChange={e=>setEmail(e.target.value)} />
          </div>
          <div className="mb-3">
            <label className="form-label">Password</label>
            <input type="password" className="form-control" value={password} onChange={e=>setPassword(e.target.value)} />
          </div>
          <button className="btn btn-success">Create Account</button>
        </form>
      </div>
    </div>
  )
}