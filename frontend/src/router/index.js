import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import StudentsView from '../views/StudentsView.vue'
import StudentSearchView from '../views/StudentSearchView.vue'
import SearchView from '../views/SearchView.vue'
import SubjectsView from '../views/SubjectsView.vue'
import TeachersView from '../views/TeachersView.vue'
import ExamsView from '../views/ExamsView.vue'
import CareersView from '../views/CareersView.vue'
import LoginView from '../views/LoginView.vue'
import { authService } from '../auth.js'

const routes = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresGuest: true }
  },
  {
    path: '/',
    name: 'home',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: DashboardView,
    meta: { requiresAuth: true }
  },
  {
    path: '/students',
    name: 'students',
    component: StudentsView,
    meta: { requiresAuth: true }
  },
  {
    path: '/student-search',
    name: 'student-search',
    component: StudentSearchView,
    meta: { requiresAuth: true }
  },
  {
    path: '/search',
    name: 'search',
    component: SearchView,
    meta: { requiresAuth: true }
  },
  {
    path: '/subjects',
    name: 'subjects',
    component: SubjectsView,
    meta: { requiresAuth: true }
  },
  {
    path: '/teachers',
    name: 'teachers',
    component: TeachersView,
    meta: { requiresAuth: true }
  },
  {
    path: '/exams',
    name: 'exams',
    component: ExamsView,
    meta: { requiresAuth: true }
  },
  {
    path: '/careers',
    name: 'careers',
    component: CareersView,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// Guard de autenticación optimizado
router.beforeEach((to, from, next) => {
  const isAuthenticated = authService.isAuthenticated()
  
  // Inicializar sesión si hay token válido
  if (isAuthenticated) {
    authService.initializeSession()
  }
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    // Redirigir a login con mensaje
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.meta.requiresGuest && isAuthenticated) {
    next('/dashboard')
  } else {
    // Renovar sesión en navegación
    if (isAuthenticated) {
      authService.refreshSession()
    }
    next()
  }
})

export default router
