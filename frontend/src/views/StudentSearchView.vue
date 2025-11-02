<template>
  <div class="container mt-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb breadcrumb-professional">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Búsqueda de Estudiantes</li>
      </ol>
    </nav>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-search me-2"></i>Búsqueda por Matrícula</h2>
    </div>

    <!-- Formulario de búsqueda -->
    <div class="card card-professional mb-4">
      <div class="card-header">
        <h5 class="mb-0">Buscar Estudiante</h5>
      </div>
      <div class="card-body">
        <form @submit.prevent="searchStudent" class="form-professional">
          <div class="row">
            <div class="col-md-8">
              <label class="form-label">Matrícula del Estudiante</label>
              <input 
                v-model="searchEnrollment" 
                type="text" 
                class="form-control" 
                pattern="^[A-Z]-\d{4}/\d+$"
                placeholder="Ej: B-6380/1"
                title="Formato: B-6380/1"
                required 
              />
            </div>
            <div class="col-md-4 d-flex align-items-end">
              <button type="submit" class="btn btn-primary btn-professional w-100" :disabled="loading">
                <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                <i class="fas fa-search me-1"></i>Buscar
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-4">
      <div class="spinner-border spinner-professional" role="status">
        <span class="visually-hidden">Buscando...</span>
      </div>
    </div>

    <!-- Error -->
    <div v-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <!-- Resultado de búsqueda -->
    <div v-if="studentDetail && !loading" class="fade-in">
      <!-- Información del estudiante -->
      <div class="card card-professional mb-4">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-user-graduate me-2"></i>
            Información del Estudiante
          </h5>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <p><strong>Matrícula:</strong> {{ studentDetail.enrollment }}</p>
              <p><strong>Nombre:</strong> {{ studentDetail.firstName }} {{ studentDetail.lastName }}</p>
              <p><strong>Email:</strong> {{ studentDetail.email }}</p>
            </div>
            <div class="col-md-6">
              <p><strong>Carrera:</strong> {{ studentDetail.career?.name || 'Sin carrera' }}</p>
              <p><strong>Materias Inscritas:</strong> {{ studentDetail.subjects?.length || 0 }}</p>
              <p><strong>Exámenes Realizados:</strong> {{ studentDetail.exams?.length || 0 }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Materias inscritas -->
      <div class="card card-professional mb-4">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-book me-2"></i>
            Materias Inscritas ({{ studentDetail.subjects?.length || 0 }})
          </h5>
        </div>
        <div class="card-body">
          <div v-if="!studentDetail.subjects || studentDetail.subjects.length === 0" class="text-muted text-center py-3">
            No tiene materias inscritas
          </div>
          <div v-else class="table-responsive">
            <table class="table table-professional">
              <thead>
                <tr>
                  <th>Materia</th>
                  <th>Descripción</th>
                  <th>Créditos</th>
                  <th>Profesor</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="subject in studentDetail.subjects" :key="subject.id">
                  <td>{{ subject.name }}</td>
                  <td>{{ subject.description }}</td>
                  <td>{{ subject.credits }}</td>
                  <td>{{ subject.teacher?.firstName }} {{ subject.teacher?.lastName }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Exámenes realizados -->
      <div class="card card-professional">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-clipboard-list me-2"></i>
            Exámenes Realizados ({{ studentDetail.exams?.length || 0 }})
          </h5>
        </div>
        <div class="card-body">
          <div v-if="!studentDetail.exams || studentDetail.exams.length === 0" class="text-muted text-center py-3">
            No tiene exámenes registrados
          </div>
          <div v-else class="table-responsive">
            <table class="table table-professional">
              <thead>
                <tr>
                  <th>Materia</th>
                  <th>Fecha</th>
                  <th>Nota</th>
                  <th>Observaciones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="exam in studentDetail.exams" :key="exam.id">
                  <td>{{ exam.subject?.name }}</td>
                  <td>{{ formatDate(exam.examDate) }}</td>
                  <td>
                    <span class="badge" :class="getGradeBadgeClass(exam.grade)">
                      {{ exam.grade }}/10
                    </span>
                  </td>
                  <td>{{ exam.observations || 'Sin observaciones' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- No encontrado -->
    <div v-if="searched && !studentDetail && !loading && !error" class="alert alert-warning">
      <i class="fas fa-exclamation-triangle me-2"></i>
      No se encontró ningún estudiante con la matrícula "{{ searchEnrollment }}"
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const apiBase = 'http://localhost:8080/api'

// Estados
const searchEnrollment = ref('')
const studentDetail = ref(null)
const loading = ref(false)
const error = ref('')
const searched = ref(false)

// Funciones
const searchStudent = async () => {
  if (!searchEnrollment.value.trim()) return
  
  loading.value = true
  error.value = ''
  searched.value = false
  studentDetail.value = null
  
  try {
    const response = await axios.get(`${apiBase}/students/search?enrollment=${encodeURIComponent(searchEnrollment.value.trim())}`)
    studentDetail.value = response.data
    searched.value = true
  } catch (err) {
    if (err.response?.status === 404) {
      searched.value = true
    } else {
      error.value = 'Error al buscar el estudiante'
    }
    console.error('Error searching student:', err)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES')
}

const getGradeBadgeClass = (grade) => {
  if (grade >= 8) return 'bg-success'
  if (grade >= 6) return 'bg-warning'
  return 'bg-danger'
}
</script>

<style scoped>
.spinner-professional {
  color: var(--primary-color);
}
</style>