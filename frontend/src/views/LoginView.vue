<template>
  <div class="login-container">
    <div class="login-card">
      <div class="text-center mb-4">
        <h2 class="text-primary">SchoolApp Admin</h2>
        <p class="text-muted">Ingresa tus credenciales para acceder</p>
      </div>

      <form @submit.prevent="handleLogin" class="form-professional">
        <div class="mb-3">
          <label class="form-label">Usuario</label>
          <input 
            v-model="loginForm.username" 
            type="text" 
            class="form-control" 
            :class="{ 'is-invalid': errors.username }"
            placeholder="Ingresa tu usuario"
            autocomplete="username"
            required 
          />
          <div v-if="errors.username" class="invalid-feedback">
            {{ errors.username }}
          </div>
        </div>

        <div class="mb-3">
          <label class="form-label">Contraseña</label>
          <div class="input-group">
            <input 
              v-model="loginForm.password" 
              :type="showPassword ? 'text' : 'password'" 
              class="form-control"
              :class="{ 'is-invalid': errors.password }"
              placeholder="Ingresa tu contraseña"
              required 
            />
            <button 
              type="button" 
              class="btn btn-outline-secondary"
              @click="showPassword = !showPassword"
            >
              <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </button>
          </div>
          <div v-if="errors.password" class="invalid-feedback d-block">
            {{ errors.password }}
          </div>
        </div>

        <div v-if="errorMessage" class="alert alert-danger">
          {{ errorMessage }}
        </div>

        <button 
          type="submit" 
          class="btn btn-primary btn-professional w-100"
          :disabled="loading"
        >
          <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
          {{ loading ? 'Ingresando...' : 'Ingresar' }}
        </button>
      </form>

      <div class="text-center mt-3">
        <small class="text-muted">
          Usuario por defecto: admin / admin123
        </small>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../auth.js'

const router = useRouter()

const loginForm = ref({
  username: 'admin',
  password: ''
})

const errors = ref({})
const errorMessage = ref('')
const loading = ref(false)
const showPassword = ref(false)

// Validación simple
const validateForm = () => {
  return loginForm.value.username.trim() && loginForm.value.password.trim()
}

const handleLogin = async () => {
  if (!validateForm()) return
  
  loading.value = true
  errorMessage.value = ''

  try {
    await authService.login(loginForm.value)
    
    // Redirigir inmediatamente
    router.push('/dashboard')
    
  } catch (error) {
    errorMessage.value = error.message
    loginForm.value.password = ''
  } finally {
    loading.value = false
  }
}

// Verificar si ya está autenticado
onMounted(() => {
  if (authService.isAuthenticated()) {
    router.push('/dashboard')
  }
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--navy-900) 0%, var(--navy-700) 50%, var(--primary-color) 100%);
  position: relative;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="%23ffffff" opacity="0.05"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
  pointer-events: none;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 2.5rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-lg);
  width: 100%;
  max-width: 420px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.text-primary {
  background: linear-gradient(135deg, var(--navy-700), var(--primary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-weight: 700;
}
</style>