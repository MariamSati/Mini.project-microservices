import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_GATEWAY_URL || 'http://localhost:8080'
});

export default api;
