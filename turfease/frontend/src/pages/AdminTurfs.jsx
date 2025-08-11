import React, { useEffect, useState } from 'react'
import { api } from '../api'

export default function AdminTurfs(){
  const [turfs, setTurfs] = useState([])
  const [form, setForm] = useState({ name:'', location:'', pricePerHour:10, sportType:'FOOTBALL', images:'', description:'' })

  const load = () => api.get('/api/turfs/public').then(res => setTurfs(res.data))
  useEffect(() => { load() }, [])

  const save = async () => {
    await api.post('/api/turfs', form)
    setForm({ name:'', location:'', pricePerHour:10, sportType:'FOOTBALL', images:'', description:'' })
    load()
  }
  const remove = async (id) => { await api.delete(`/api/turfs/${id}`); load() }

  return (
    <div>
      <h3>Manage Turfs</h3>
      <div className="card mb-3"><div className="card-body">
        <div className="row g-2">
          <div className="col"><input placeholder="Name" className="form-control" value={form.name} onChange={e=>setForm({...form,name:e.target.value})} /></div>
          <div className="col"><input placeholder="Location" className="form-control" value={form.location} onChange={e=>setForm({...form,location:e.target.value})} /></div>
          <div className="col"><input type="number" placeholder="Price/hr" className="form-control" value={form.pricePerHour} onChange={e=>setForm({...form,pricePerHour:Number(e.target.value)})} /></div>
          <div className="col">
            <select className="form-select" value={form.sportType} onChange={e=>setForm({...form,sportType:e.target.value})}>
              {['FOOTBALL','CRICKET','BADMINTON','TENNIS','BASKETBALL'].map(s=> <option key={s}>{s}</option>)}
            </select>
          </div>
          <div className="w-100"/>
          <div className="col-8"><input placeholder="Image URL(s), comma-separated" className="form-control" value={form.images} onChange={e=>setForm({...form,images:e.target.value})} /></div>
          <div className="col-4"><button className="btn btn-success w-100" onClick={save}>Add Turf</button></div>
        </div>
      </div></div>

      <table className="table">
        <thead><tr><th>ID</th><th>Name</th><th>Location</th><th>Sport</th><th>Price/hr</th><th></th></tr></thead>
        <tbody>
          {turfs.map(t => (
            <tr key={t.id}>
              <td>{t.id}</td>
              <td>{t.name}</td>
              <td>{t.location}</td>
              <td>{t.sportType}</td>
              <td>${t.pricePerHour}</td>
              <td><button className="btn btn-sm btn-danger" onClick={()=>remove(t.id)}>Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}