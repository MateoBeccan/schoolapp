import axios from 'axios'

const API_BASE = 'http://localhost:8080/api'

// Configurar interceptor para incluir token automáticamente
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Interceptor simplificado para manejar errores
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export const authService = {
  // Login
  async login(credentials) {
    const response = await axios.post(`${API_BASE}/auth/login`, credentials)
    return response.data
  },

  // Logout
  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    delete axios.defaults.headers.common['Authorization']
  },

  // Verificar si está autenticado
  isAuthenticated() {
    return !!localStorage.getItem('token')
  },

  // Obtener usuario actual
  getCurrentUser() {
    const user = localStorage.getItem('user')
    return user ? JSON.parse(user) : null
  },

  // Validar token
  async validateToken() {
    try {
      await axios.get(`${API_BASE}/auth/validate`)
      return true
    } catch {
      return false
    }
  },

  // Inicializar admin (solo para desarrollo)
  async initializeAdmin() {
    const response = await axios.post(`${API_BASE}/init`)
    return response.data
  }
}

export default authService