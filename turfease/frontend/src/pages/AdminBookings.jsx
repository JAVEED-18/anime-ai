import React, { useEffect, useState } from 'react'
import { api } from '../api'

export default function AdminBookings(){
  const [bookings, setBookings] = useState([])
  const load = () => api.get('/api/bookings/all').then(res => setBookings(res.data))
  useEffect(() => { load() }, [])
  const cancelB = async (id) => { await api.delete(`/api/bookings/${id}`); load() }

  return (
    <div>
      <h3>All Bookings</h3>
      <table className="table">
        <thead>
          <tr><th>ID</th><th>User</th><th>Turf</th><th>Start</th><th>End</th><th>Status</th><th></th></tr>
        </thead>
        <tbody>
          {bookings.map(b => (
            <tr key={b.id}>
              <td>{b.id}</td>
              <td>{b.userEmail}</td>
              <td>{b.turfId}</td>
              <td>{b.startTime?.replace('T',' ')}</td>
              <td>{b.endTime?.replace('T',' ')}</td>
              <td>{b.status}</td>
              <td>{b.status !== 'CANCELLED' && <button className="btn btn-sm btn-warning" onClick={()=>cancelB(b.id)}>Cancel</button>}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}