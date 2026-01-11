import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: process.env.REACT_APP_KEYCLOAK_URL || 'http://localhost:8180/auth',
  realm: process.env.REACT_APP_KEYCLOAK_REALM || 'mini-projet',
  clientId: process.env.REACT_APP_KEYCLOAK_CLIENT || 'frontend'
});

export default keycloak;
