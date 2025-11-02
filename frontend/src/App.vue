<template>
  <div>
    <nav v-if="isAuthenticated" class="navbar navbar-expand-lg navbar-professional shadow-sm">
      <div class="container-fluid px-4">
        <!-- Brand -->
        <router-link class="navbar-brand fw-bold" to="/dashboard">
          <i class="fas fa-graduation-cap me-2 text-primary"></i>
          <span class="brand-text">SchoolApp</span>
        </router-link>

        <!-- Toggle button for mobile -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navigation items -->
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link class="nav-link" to="/dashboard">
                <i class="fas fa-tachometer-alt me-1"></i>Dashboard
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/students">
                <i class="fas fa-user-graduate me-1"></i>Estudiantes
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/teachers">
                <i class="fas fa-chalkboard-teacher me-1"></i>Docentes
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/subjects">
                <i class="fas fa-book me-1"></i>Materias
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/exams">
                <i class="fas fa-clipboard-list me-1"></i>Exámenes
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/careers">
                <i class="fas fa-graduation-cap me-1"></i>Carreras
              </router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/search">
                <i class="fas fa-search me-1"></i>Búsqueda
              </router-link>
            </li>
          </ul>

          <!-- User menu -->
          <div class="navbar-nav">
            <div class="nav-item dropdown">
              <a class="nav-link dropdown-toggle user-menu" href="#" role="button" data-bs-toggle="dropdown">
                <div class="user-avatar">
                  <i class="fas fa-user"></i>
                </div>
                <span class="user-name ms-2">{{ currentUser?.username || 'Usuario' }}</span>
              </a>
              <ul class="dropdown-menu dropdown-menu-end">
                <li>
                  <div class="dropdown-header">
                    <div class="fw-bold">{{ currentUser?.username }}</div>
                    <small class="text-muted">{{ currentUser?.email || 'Administrador' }}</small>
                  </div>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                  <a class="dropdown-item" href="#" @click.prevent="handleLogout">
                    <i class="fas fa-sign-out-alt me-2"></i>Cerrar Sesión
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <router-view />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from './auth.js'

const router = useRouter()
const currentUser = ref(null)

const isAuthenticated = computed(() => {
  return authService.isAuthenticated() && currentUser.value !== null
})

const handleLogout = () => {
  const confirmed = confirm('¿Estás seguro de que deseas cerrar sesión?')
  
  if (confirmed) {
    authService.logout()
    currentUser.value = null
    router.push('/login')
  }
}

onMounted(() => {
  // Verificar autenticación y cargar usuario
  if (authService.isAuthenticated()) {
    currentUser.value = authService.getCurrentUser()
    authService.initializeSession()
  } else {
    currentUser.value = null
  }
})

// Escuchar cambios de ruta para actualizar estado
router.afterEach(() => {
  if (authService.isAuthenticated()) {
    currentUser.value = authService.getCurrentUser()
  } else {
    currentUser.value = null
  }
})
</script>

<style>
/* Estilos específicos de App.vue */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}

/* Navbar styles */
.navbar-professional {
  background: linear-gradient(135deg, var(--navy-900) 0%, var(--navy-700) 100%);
  border-bottom: 3px solid var(--primary-color);
}

.brand-text {
  background: linear-gradient(135deg, #ffffff, var(--primary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-link {
  color: rgba(255, 255, 255, 0.9) !important;
  font-weight: 500;
  padding: 0.75rem 1rem !important;
  border-radius: 6px;
  transition: all 0.3s ease;
  margin: 0 2px;
}

.nav-link:hover {
  color: var(--primary-color) !important;
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-1px);
}

.nav-link.router-link-active {
  color: var(--primary-color) !important;
  background: rgba(255, 255, 255, 0.15);
  font-weight: 600;
}

.user-menu {
  color: rgba(255, 255, 255, 0.9) !important;
  padding: 0.5rem 1rem !important;
  border-radius: 25px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.user-menu:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

.user-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: var(--primary-color);
  border-radius: 50%;
  color: white;
  font-size: 14px;
}

.user-name {
  font-weight: 500;
}

.dropdown-menu {
  border: none;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  border-radius: 10px;
  padding: 0.5rem 0;
  margin-top: 0.5rem;
}

.dropdown-header {
  padding: 0.75rem 1rem;
  background: linear-gradient(135deg, var(--navy-700), var(--primary-color));
  color: white;
  border-radius: 10px 10px 0 0;
  margin: -0.5rem -0rem 0.5rem -0rem;
}

.dropdown-item {
  padding: 0.75rem 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background: var(--primary-color);
  color: white;
}

.navbar-toggler {
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 0.8%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='m4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}

@media (max-width: 991px) {
  .navbar-nav {
    padding-top: 1rem;
  }
  
  .nav-link {
    margin: 2px 0;
  }
}
</style>

