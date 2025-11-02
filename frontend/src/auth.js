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

// Interceptor para manejar errores y logout automático
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token inválido o expirado
      authService.logout()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

const authService = {
  // Login optimizado
  async login(credentials) {
    try {
      const response = await axios.post(`${API_BASE}/auth/login`, credentials)
      const { token, username, email, role } = response.data
      
      // Guardar datos de sesión
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify({ username, email, role }))
      localStorage.setItem('loginTime', Date.now().toString())
      
      // Configurar header de autorización
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      
      return response.data
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Error de autenticación')
    }
  },

  // Logout optimizado
  logout() {
    // Limpiar almacenamiento local
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    localStorage.removeItem('loginTime')
    
    // Limpiar headers de axios
    delete axios.defaults.headers.common['Authorization']
    
    // Limpiar cualquier timer de sesión
    if (this.sessionTimer) {
      clearTimeout(this.sessionTimer)
    }
  },

  // Verificar autenticación con validación de tiempo
  isAuthenticated() {
    const token = localStorage.getItem('token')
    const loginTime = localStorage.getItem('loginTime')
    
    if (!token || !loginTime) return false
    
    // Verificar si la sesión ha expirado (24 horas)
    const sessionDuration = 24 * 60 * 60 * 1000 // 24 horas
    const isExpired = Date.now() - parseInt(loginTime) > sessionDuration
    
    if (isExpired) {
      this.logout()
      return false
    }
    
    return true
  },

  // Obtener usuario actual
  getCurrentUser() {
    const user = localStorage.getItem('user')
    return user ? JSON.parse(user) : null
  },

  // Inicializar sesión automáticamente
  initializeSession() {
    const token = localStorage.getItem('token')
    if (token && this.isAuthenticated()) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      return true
    }
    return false
  },
  
  // Renovar tiempo de sesión
  refreshSession() {
    if (this.isAuthenticated()) {
      localStorage.setItem('loginTime', Date.now().toString())
    }
  },

  // Inicializar admin (solo para desarrollo)
  async initializeAdmin() {
    const response = await axios.post(`${API_BASE}/init`)
    return response.data
  },
  
  sessionTimer: null
}

export { authService }
export default authService