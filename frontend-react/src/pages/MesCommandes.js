import React, { useEffect, useState } from 'react';
import api from '../services/api';
import keycloak from '../keycloak';

const MesCommandes = () => {
  const [commandes, setCommandes] = useState([]);

  useEffect(() => {
    const token = keycloak.token;
    api.get('/api/commandes/mes-commandes', { headers: { Authorization: 'Bearer ' + token } })
      .then(res => setCommandes(res.data))
      .catch(err => alert('Erreur fetching commandes: ' + err));
  }, []);

  return (
    <div>
      <h2>Mes Commandes</h2>
      <table className="table">
        <thead>
          <tr><th>ID</th><th>Date</th><th>Statut</th><th>Montant</th></tr>
        </thead>
        <tbody>
          {commandes.map(c => (
            <tr key={c.id}><td>{c.id}</td><td>{c.dateCommande}</td><td>{c.statut}</td><td>{c.montantTotal}</td></tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MesCommandes;