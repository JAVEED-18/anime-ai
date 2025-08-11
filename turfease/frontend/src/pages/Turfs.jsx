import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'

export default function Turfs(){
  const [turfs, setTurfs] = useState([])

  useEffect(() => {
    api.get('/api/turfs/public').then(res => setTurfs(res.data))
  }, [])

  return (
    <div>
      <div className="hero mb-4">
        <h2>Find and book your perfect turf</h2>
        <p>Football, badminton, cricket and more.</p>
      </div>
      <div className="row g-3">
        {turfs.map(t => (
          <div className="col-md-4" key={t.id}>
            <div className="card h-100">
              <img className="card-img-top" src={t.images?.split(',')[0] || 'https://picsum.photos/800/400'} alt="turf" />
              <div className="card-body d-flex flex-column">
                <h5 className="card-title">{t.name}</h5>
                <p className="card-text">{t.location} • {t.sportType} • ${t.pricePerHour}/hr</p>
                <Link to={`/turfs/${t.id}`} className="btn btn-success mt-auto">View & Book</Link>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}