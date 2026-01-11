import React, { useEffect, useState } from 'react';
import api from '../services/api';
import keycloak from '../keycloak';

const ProduitList = () => {
  const [produits, setProduits] = useState([]);

  useEffect(() => {
    const token = keycloak.token;
    api.get('/api/produits', { headers: { Authorization: 'Bearer ' + token } })
      .then(res => setProduits(res.data))
      .catch(err => alert('Erreur fetching produits: ' + err));
  }, []);

  return (
    <div>
      <h2>Catalogue Produits</h2>
      <table className="table">
        <thead>
          <tr><th>Nom</th><th>Description</th><th>Prix</th><th>Stock</th></tr>
        </thead>
        <tbody>
          {produits.map(p => (
            <tr key={p.id}>
              <td>{p.nom}</td>
              <td>{p.description}</td>
              <td>{p.prix}</td>
              <td>{p.quantiteStock}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProduitList;