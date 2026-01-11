import React from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import keycloak from './keycloak';
import ProduitList from './pages/ProduitList';
import CreateProduit from './pages/CreateProduit';
import CreateCommande from './pages/CreateCommande';
import MesCommandes from './pages/MesCommandes';

const App = () => {
  return (
    <ReactKeycloakProvider authClient={keycloak} initOptions={{ onLoad: 'login-required' }}>
      <BrowserRouter>
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
          <div className="container-fluid">
            <Link className="navbar-brand" to="/">Mini Projet</Link>
            <div>
              <Link className="nav-link" to="/produits">Produits</Link>
              <Link className="nav-link" to="/mes-commandes">Mes commandes</Link>
            </div>
          </div>
        </nav>
        <div className="container mt-4">
          <Routes>
            <Route path="/" element={<ProduitList />} />
            <Route path="/produits" element={<ProduitList />} />
            <Route path="/produits/new" element={<CreateProduit />} />
            <Route path="/commandes/new" element={<CreateCommande />} />
            <Route path="/mes-commandes" element={<MesCommandes />} />
          </Routes>
        </div>
      </BrowserRouter>
    </ReactKeycloakProvider>
  );
};

export default App;
