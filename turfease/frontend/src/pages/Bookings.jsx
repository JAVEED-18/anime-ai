import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'

export default function Bookings(){
  const [bookings, setBookings] = useState([])

  useEffect(() => {
    api.get('/api/bookings').then(res => setBookings(res.data))
  }, [])

  return (
    <div>
      <h3>My Bookings</h3>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th><th>Turf</th><th>Start</th><th>End</th><th>Status</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map(b => (
            <tr key={b.id}>
              <td>{b.id}</td>
              <td>{b.turfId}</td>
              <td>{b.startTime?.replace('T',' ')}</td>
              <td>{b.endTime?.replace('T',' ')}</td>
              <td>{b.status}</td>
              <td>
                {b.status !== 'PAID' && <Link className="btn btn-sm btn-success me-2" to={`/payment/${b.id}`}>Pay</Link>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}