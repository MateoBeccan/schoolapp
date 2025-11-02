<template>
  <div>
    <nav v-if="isAuthenticated" class="navbar navbar-expand navbar-professional px-4">
      <router-link class="navbar-brand" to="/dashboard">
        <i class="fas fa-graduation-cap me-2"></i>SchoolApp
      </router-link>
      <div class="navbar-nav me-auto">
        <router-link class="nav-link" to="/dashboard">
          <i class="fas fa-tachometer-alt me-1"></i>Dashboard
        </router-link>
        <router-link class="nav-link" to="/students">
          <i class="fas fa-user-graduate me-1"></i>Estudiantes
        </router-link>
        <router-link class="nav-link" to="/search">
          <i class="fas fa-search me-1"></i>Búsqueda
        </router-link>
        <router-link class="nav-link" to="/subjects">
          <i class="fas fa-book me-1"></i>Materias
        </router-link>
        <router-link class="nav-link" to="/teachers">
          <i class="fas fa-chalkboard-teacher me-1"></i>Docentes
        </router-link>
        <router-link class="nav-link" to="/exams">
          <i class="fas fa-clipboard-list me-1"></i>Exámenes
        </router-link>
        <router-link class="nav-link" to="/careers">
          <i class="fas fa-graduation-cap me-1"></i>Carreras
        </router-link>
      </div>
      <div class="navbar-nav">
        <span class="navbar-text me-3">
          <i class="fas fa-user me-1"></i>{{ currentUser?.username }}
        </span>
        <button class="btn btn-outline-light btn-sm" @click="handleLogout">
          <i class="fas fa-sign-out-alt me-1"></i>Salir
        </button>
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
  return authService.isAuthenticated()
})

const handleLogout = () => {
  authService.logout()
  router.push('/login')
}

onMounted(() => {
  currentUser.value = authService.getCurrentUser()
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
</style>

