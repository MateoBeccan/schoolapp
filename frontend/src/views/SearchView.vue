<template>
  <div class="container mt-4">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb breadcrumb-professional">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Búsqueda Avanzada</li>
      </ol>
    </nav>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-search me-2"></i>Búsqueda Avanzada</h2>
    </div>

    <!-- Formulario de búsqueda -->
    <div class="card card-professional mb-4">
      <div class="card-header">
        <h5 class="mb-0">Buscar en el Sistema</h5>
      </div>
      <div class="card-body">
        <div class="row">
          <div class="col-md-4 mb-3">
            <label class="form-label">Tipo de búsqueda</label>
            <select v-model="searchType" class="form-select">
              <option value="students">Estudiantes</option>
              <option value="teachers">Profesores</option>
              <option value="subjects">Materias</option>
              <option value="exams">Exámenes</option>
            </select>
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Término de búsqueda</label>
            <input 
              v-model="searchQuery" 
              type="text" 
              class="form-control" 
              :placeholder="getPlaceholder()"
              @keyup.enter="performSearch"
            />
          </div>
          <div class="col-md-2 d-flex align-items-end mb-3">
            <button 
              @click="performSearch" 
              class="btn btn-primary btn-professional w-100"
              :disabled="loading || !searchQuery.trim()"
            >
              <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
              <i class="fas fa-search me-1"></i>Buscar
            </button>
          </div>
        </div>
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

    <!-- Resultados -->
    <div v-if="results.length > 0 && !loading" class="fade-in">
      <!-- Estudiantes -->
      <div v-if="searchType === 'students'" class="card card-professional">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-user-graduate me-2"></i>
            Estudiantes Encontrados ({{ results.length }})
          </h5>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-professional">
              <thead>
                <tr>
                  <th>Matrícula</th>
                  <th>Nombre</th>
                  <th>Email</th>
                  <th>Carrera</th>
                  <th>Materias</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="student in results" :key="student.id">
                  <td><strong>{{ student.enrollment }}</strong></td>
                  <td>{{ student.firstName }} {{ student.lastName }}</td>
                  <td>{{ student.email }}</td>
                  <td>{{ student.career?.name || 'Sin carrera' }}</td>
                  <td>{{ student.subjects?.length || 0 }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Profesores -->
      <div v-if="searchType === 'teachers'" class="card card-professional">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-chalkboard-teacher me-2"></i>
            Profesores Encontrados ({{ results.length }})
          </h5>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-professional">
              <thead>
                <tr>
                  <th>Legajo</th>
                  <th>Nombre</th>
                  <th>Email</th>
                  <th>Departamento</th>
                  <th>Materias</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="teacher in results" :key="teacher.id">
                  <td><strong>{{ teacher.enrollment }}</strong></td>
                  <td>{{ teacher.firstName }} {{ teacher.lastName }}</td>
                  <td>{{ teacher.email }}</td>
                  <td>{{ teacher.department }}</td>
                  <td>{{ teacher.subjects?.length || 0 }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Materias -->
      <div v-if="searchType === 'subjects'" class="card card-professional">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-book me-2"></i>
            Materias Encontradas ({{ results.length }})
          </h5>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-professional">
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Descripción</th>
                  <th>Créditos</th>
                  <th>Profesor</th>
                  <th>Estudiantes</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="subject in results" :key="subject.id">
                  <td><strong>{{ subject.name }}</strong></td>
                  <td>{{ subject.description }}</td>
                  <td>{{ subject.credits }}</td>
                  <td>{{ subject.teacher?.firstName }} {{ subject.teacher?.lastName }}</td>
                  <td>{{ subject.students?.length || 0 }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Exámenes -->
      <div v-if="searchType === 'exams'" class="card card-professional">
        <div class="card-header">
          <h5 class="mb-0">
            <i class="fas fa-clipboard-list me-2"></i>
            Exámenes Encontrados ({{ results.length }})
          </h5>
        </div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-professional">
              <thead>
                <tr>
                  <th>Estudiante</th>
                  <th>Materia</th>
                  <th>Fecha</th>
                  <th>Nota</th>
                  <th>Observaciones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="exam in results" :key="exam.id">
                  <td>{{ exam.student?.firstName }} {{ exam.student?.lastName }}</td>
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
    <div v-if="searched && results.length === 0 && !loading && !error" class="alert alert-warning">
      <i class="fas fa-exclamation-triangle me-2"></i>
      No se encontraron resultados para "{{ searchQuery }}" en {{ getSearchTypeName() }}
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const apiBase = 'http://localhost:8080/api'

const searchType = ref('students')
const searchQuery = ref('')
const results = ref([])
const loading = ref(false)
const error = ref('')
const searched = ref(false)

const getPlaceholder = () => {
  const placeholders = {
    students: 'Matrícula (B-6380/1), nombre o apellido',
    teachers: 'Legajo (PROF-001), nombre o apellido',
    subjects: 'Nombre de la materia',
    exams: 'Nombre del estudiante o materia'
  }
  return placeholders[searchType.value]
}

const getSearchTypeName = () => {
  const names = {
    students: 'estudiantes',
    teachers: 'profesores',
    subjects: 'materias',
    exams: 'exámenes'
  }
  return names[searchType.value]
}

const performSearch = async () => {
  if (!searchQuery.value.trim()) return
  
  loading.value = true
  error.value = ''
  searched.value = false
  results.value = []
  
  try {
    let response
    const query = searchQuery.value.trim()
    
    switch (searchType.value) {
      case 'students':
        response = await axios.get(`${apiBase}/students/search?enrollment=${encodeURIComponent(query)}`)
        results.value = response.data ? [response.data] : []
        break
      case 'teachers':
        response = await axios.get(`${apiBase}/teachers/search?query=${encodeURIComponent(query)}`)
        results.value = response.data || []
        break
      case 'subjects':
        response = await axios.get(`${apiBase}/subjects`)
        results.value = response.data.filter(subject => 
          subject.name.toLowerCase().includes(query.toLowerCase()) ||
          subject.description.toLowerCase().includes(query.toLowerCase())
        )
        break
      case 'exams':
        response = await axios.get(`${apiBase}/exams`)
        results.value = response.data.filter(exam => 
          exam.student?.firstName?.toLowerCase().includes(query.toLowerCase()) ||
          exam.student?.lastName?.toLowerCase().includes(query.toLowerCase()) ||
          exam.subject?.name?.toLowerCase().includes(query.toLowerCase())
        )
        break
    }
    
    searched.value = true
  } catch (err) {
    if (err.response?.status === 404) {
      searched.value = true
    } else {
      error.value = 'Error al realizar la búsqueda'
    }
    console.error('Error searching:', err)
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