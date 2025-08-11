import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../api'

export default function Payment(){
  const { bookingId } = useParams()
  const [status, setStatus] = useState('PENDING')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const payNow = async () => {
    setError('')
    try {
      await api.post(`/api/payments/initiate/${bookingId}`)
      setStatus('PAID')
      setTimeout(() => navigate('/bookings'), 800)
    } catch (e) {
      setError('Payment failed')
    }
  }

  return (
    <div className="row justify-content-center">
      <div className="col-md-6">
        <div className="card">
          <div className="card-body">
            <h4>Complete Payment</h4>
            <p>Booking ID: <strong>#{bookingId}</strong></p>
            <p>Status: <span className={`badge ${status==='PAID'?'bg-success':'bg-secondary'}`}>{status}</span></p>
            {error && <div className="alert alert-danger">{error}</div>}
            <button className="btn btn-success" onClick={payNow} disabled={status==='PAID'}>Pay Now</button>
          </div>
        </div>
      </div>
    </div>
  )
}