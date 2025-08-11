import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { api } from '../api'
import { useAuth } from '../auth'

export default function TurfDetails(){
  const { id } = useParams()
  const [turf, setTurf] = useState(null)
  const [date, setDate] = useState('')
  const [start, setStart] = useState('18:00')
  const [end, setEnd] = useState('19:00')
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const { user } = useAuth()

  useEffect(() => {
    api.get(`/api/turfs/public/${id}`).then(res => setTurf(res.data))
  }, [id])

  const book = async () => {
    if (!user) { navigate('/login'); return }
    try {
      const startTime = new Date(`${date}T${start}:00`).toISOString().slice(0,19)
      const endTime = new Date(`${date}T${end}:00`).toISOString().slice(0,19)
      const { data } = await api.post('/api/bookings', {
        turfId: Number(id),
        startTime,
        endTime,
        totalAmount: turf.pricePerHour,
        userEmail: user.email
      })
      navigate(`/payment/${data.id}`)
    } catch (e) {
      setError(e.response?.data || 'Booking failed')
    }
  }

  if (!turf) return <div>Loading...</div>
  return (
    <div className="row">
      <div className="col-md-7">
        <img className="img-fluid rounded" src={turf.images?.split(',')[0] || 'https://picsum.photos/800/400'} />
        <h3 className="mt-3">{turf.name}</h3>
        <p>{turf.description}</p>
        <p><strong>Location:</strong> {turf.location}</p>
        <p><strong>Sport:</strong> {turf.sportType} • <strong>Price:</strong> ${turf.pricePerHour}/hr</p>
      </div>
      <div className="col-md-5">
        <div className="card">
          <div className="card-body">
            <h5>Book this turf</h5>
            {error && <div className="alert alert-danger">{error}</div>}
            <div className="mb-3">
              <label className="form-label">Date</label>
              <input type="date" className="form-control" value={date} onChange={e=>setDate(e.target.value)} />
            </div>
            <div className="mb-3">
              <label className="form-label">Start Time</label>
              <input type="time" className="form-control" value={start} onChange={e=>setStart(e.target.value)} />
            </div>
            <div className="mb-3">
              <label className="form-label">End Time</label>
              <input type="time" className="form-control" value={end} onChange={e=>setEnd(e.target.value)} />
            </div>
            <button className="btn btn-success w-100" onClick={book}>Book & Pay</button>
          </div>
        </div>
      </div>
    </div>
  )
}