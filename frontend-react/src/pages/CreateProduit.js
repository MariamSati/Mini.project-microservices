import React, { useState } from 'react';
import api from '../services/api';
import keycloak from '../keycloak';

const CreateProduit = () => {
  const [form, setForm] = useState({ nom: '', description: '', prix: 0, quantiteStock: 0 });

  const handleSubmit = (e) => {
    e.preventDefault();
    const token = keycloak.token;
    api.post('/api/produits', form, { headers: { Authorization: 'Bearer ' + token } })
      .then(() => alert('Produit créé'))
      .catch(err => alert('Erreur: ' + err));
  };

  return (
    <div>
      <h2>Créer Produit (ADMIN)</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Nom</label>
          <input className="form-control" value={form.nom} onChange={e => setForm({ ...form, nom: e.target.value })} />
        </div>
        <div className="mb-3">
          <label className="form-label">Description</label>
          <textarea className="form-control" value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} />
        </div>
        <div className="mb-3">
          <label className="form-label">Prix</label>
          <input type="number" className="form-control" value={form.prix} onChange={e => setForm({ ...form, prix: parseFloat(e.target.value) })} />
        </div>
        <div className="mb-3">
          <label className="form-label">Quantité Stock</label>
          <input type="number" className="form-control" value={form.quantiteStock} onChange={e => setForm({ ...form, quantiteStock: parseInt(e.target.value) })} />
        </div>
        <button className="btn btn-primary" type="submit">Créer</button>
      </form>
    </div>
  );
};

export default CreateProduit;