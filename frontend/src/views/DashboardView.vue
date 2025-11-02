<template>
  <div class="container-fluid mt-4">
    <div class="row">
      <div class="col-12">
        <h1 class="mb-4">Panel Incial - SchoolApp</h1>
        <p class="text-muted">Bienvenido al sistema de gestión escolar</p>
      </div>
    </div>

    <!-- Estadísticas -->
    <div class="row mb-4 fade-in">
      <div class="col-md-3">
        <div class="card stats-card bg-primary text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <div>
                <h4 class="mb-1">{{ stats.students }}</h4>
                <p class="mb-0 opacity-75">Estudiantes</p>
              </div>
              <div class="align-self-center opacity-75">
                <i class="fas fa-user-graduate fa-2x"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card bg-success text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <div>
                <h4>{{ stats.teachers }}</h4>
                <p class="mb-0">Docentes</p>
              </div>
              <div class="align-self-center">
                <i class="fas fa-chalkboard-teacher fa-2x"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card bg-info text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <div>
                <h4>{{ stats.subjects }}</h4>
                <p class="mb-0">Materias</p>
              </div>
              <div class="align-self-center">
                <i class="fas fa-book fa-2x"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card bg-warning text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <div>
                <h4>{{ stats.exams }}</h4>
                <p class="mb-0">Exámenes</p>
              </div>
              <div class="align-self-center">
                <i class="fas fa-clipboard-list fa-2x"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-3">
        <div class="card bg-secondary text-white">
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <div>
                <h4>{{ stats.careers }}</h4>
                <p class="mb-0">Carreras</p>
              </div>
              <div class="align-self-center">
                <i class="fas fa-graduation-cap fa-2x"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Acciones Rápidas -->
    <div class="row mb-4">
      <div class="col-12">
        <h3>Acciones Rápidas</h3>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card card-professional h-100">
          <div class="card-body text-center">
            <i class="fas fa-user-plus fa-3x text-primary mb-3"></i>
            <h5 class="fw-bold">Gestionar Estudiantes</h5>
            <p class="text-muted">Agregar, editar o eliminar estudiantes</p>
            <router-link to="/students" class="btn btn-primary btn-professional">
              Ir a Estudiantes
            </router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-user-tie fa-3x text-success mb-3"></i>
            <h5>Gestionar Docentes</h5>
            <p class="text-muted">Administrar profesores y departamentos</p>
            <router-link to="/teachers" class="btn btn-success">
              Ir a Docentes
            </router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-book-open fa-3x text-info mb-3"></i>
            <h5>Gestionar Materias</h5>
            <p class="text-muted">Crear y administrar materias académicas</p>
            <router-link to="/subjects" class="btn btn-info">
              Ir a Materias
            </router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-clipboard-list fa-3x text-warning mb-3"></i>
            <h5>Gestionar Exámenes</h5>
            <p class="text-muted">Registrar y administrar calificaciones</p>
            <router-link to="/exams" class="btn btn-warning">
              Ir a Exámenes
            </router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card h-100">
          <div class="card-body text-center">
            <i class="fas fa-graduation-cap fa-3x text-secondary mb-3"></i>
            <h5>Gestionar Carreras</h5>
            <p class="text-muted">Administrar carreras académicas</p>
            <router-link to="/careers" class="btn btn-secondary">
              Ir a Carreras
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Actividad Reciente -->
    <div class="row">
      <div class="col-md-6">
        <div class="card card-professional">
          <div class="card-header">
            <h5 class="mb-0">
              <i class="fas fa-user-graduate me-2"></i>
              Estudiantes Recientes
            </h5>
          </div>
          <div class="card-body">
            <div v-if="recentStudents.length === 0" class="text-muted text-center py-3">
              <i class="fas fa-users fa-2x mb-2 opacity-50"></i>
              <p class="mb-0">No hay estudiantes registrados</p>
            </div>
            <div v-else>
              <div v-for="student in recentStudents" :key="student.id" class="d-flex align-items-center mb-3 p-2 rounded bg-light">
                <div class="me-3">
                  <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                    <i class="fas fa-user"></i>
                  </div>
                </div>
                <div class="flex-grow-1">
                  <div class="fw-bold">{{ student.firstName }} {{ student.lastName }}</div>
                  <small class="text-muted d-block">{{ student.enrollment }}</small>
                  <small class="text-success">{{ student.career?.name || 'Sin carrera asignada' }}</small>
                </div>
                <div class="text-end">
                  <span class="badge bg-primary">{{ student.subjects?.length || 0 }}</span>
                  <small class="text-muted d-block">materias</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="card card-professional">
          <div class="card-header">
            <h5 class="mb-0">
              <i class="fas fa-chart-pie me-2"></i>
              Carreras Disponibles
            </h5>
          </div>
          <div class="card-body">
            <div v-if="careers.length === 0" class="text-muted text-center py-3">
              <i class="fas fa-graduation-cap fa-2x mb-2 opacity-50"></i>
              <p class="mb-0">No hay carreras registradas</p>
            </div>
            <div v-else>
              <div v-for="career in careers.slice(0, 5)" :key="career.id" class="d-flex justify-content-between align-items-center mb-3 p-2 rounded bg-light">
                <div>
                  <div class="fw-bold">{{ career.name }}</div>
                  <small class="text-muted">{{ career.durationYears }} años de duración</small>
                </div>
                <div class="text-end">
                  <span class="badge bg-secondary">{{ getStudentsByCareer(career.id) }}</span>
                  <small class="text-muted d-block">estudiantes</small>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

const apiBase = 'http://localhost:8080/api'

// Estados
const stats = ref({
  students: 0,
  teachers: 0,
  subjects: 0,
  exams: 0,
  careers: 0
})

const students = ref([])
const teachers = ref([])
const subjects = ref([])
const exams = ref([])
const careers = ref([])

// Computadas
const totalCredits = computed(() => {
  return subjects.value.reduce((total, subject) => total + (subject.credits || 0), 0)
})

const recentStudents = computed(() => {
  return students.value.slice(0, 5)
})

const getStudentsByCareer = (careerId) => {
  return students.value.filter(student => student.career?.id === careerId).length
}

// Funciones
const loadStats = async () => {
  try {
    const [studentsRes, teachersRes, subjectsRes, examsRes, careersRes] = await Promise.all([
      axios.get(`${apiBase}/students`),
      axios.get(`${apiBase}/teachers`),
      axios.get(`${apiBase}/subjects`),
      axios.get(`${apiBase}/exams`),
      axios.get(`${apiBase}/careers`)
    ])

    students.value = studentsRes.data || []
    teachers.value = teachersRes.data || []
    subjects.value = subjectsRes.data || []
    exams.value = examsRes.data || []
    careers.value = careersRes.data || []

    stats.value = {
      students: students.value.length,
      teachers: teachers.value.length,
      subjects: subjects.value.length,
      exams: exams.value.length,
      careers: careers.value.length
    }
  } catch (error) {
    console.error('Error loading stats:', error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-2px);
}

.fas {
  opacity: 0.8;
}
</style>
