import React, { useEffect, useState } from 'react';
import api from '../services/api';
import keycloak from '../keycloak';

const CreateCommande = () => {
  const [produits, setProduits] = useState([]);
  const [lines, setLines] = useState([]);

  useEffect(() => {
    const token = keycloak.token;
    api.get('/api/produits', { headers: { Authorization: 'Bearer ' + token } })
      .then(res => setProduits(res.data))
      .catch(err => alert('Erreur fetching produits: ' + err));
  }, []);

  const addLine = () => setLines([...lines, { idProduit: null, quantite: 1 }]);
  const updateLine = (idx, key, val) => { const copy = [...lines]; copy[idx][key] = val; setLines(copy); };

  const handleSubmit = (e) => {
    e.preventDefault();
    const token = keycloak.token;
    api.post('/api/commandes', { lignes: lines }, { headers: { Authorization: 'Bearer ' + token } })
      .then(res => alert('Commande créée'))
      .catch(err => alert('Erreur création commande: ' + err));
  };

  return (
    <div>
      <h2>Créer Commande</h2>
      <button className="btn btn-secondary mb-2" onClick={addLine}>Ajouter ligne</button>
      <form onSubmit={handleSubmit}>
        {lines.map((l, idx) => (
          <div className="row mb-2" key={idx}>
            <div className="col">
              <select className="form-control" value={l.idProduit || ''} onChange={e => updateLine(idx, 'idProduit', parseInt(e.target.value))}>
                <option value="">--Choisir produit--</option>
                {produits.map(p => <option value={p.id} key={p.id}>{p.nom} ({p.quantiteStock} en stock)</option>)}
              </select>
            </div>
            <div className="col">
              <input type="number" className="form-control" value={l.quantite} onChange={e => updateLine(idx, 'quantite', parseInt(e.target.value))} />
            </div>
          </div>
        ))}
        <button className="btn btn-primary" type="submit">Valider</button>
      </form>
    </div>
  );
};

export default CreateCommande;