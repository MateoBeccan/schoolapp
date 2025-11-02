<template>
  <div class="container mt-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb breadcrumb-professional">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Estudiantes</li>
      </ol>
    </nav>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-user-graduate me-2"></i>Estudiantes</h2>
      <span class="badge bg-primary fs-6">{{ students.length }} registrados</span>
    </div>

    <!-- Loading spinner -->
    <div v-if="loading" class="text-center py-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

    <!-- Error message -->
    <div v-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <!-- Tabla de estudiantes -->
    <div class="table-responsive">
    <table class="table table-professional">
      <thead class="table-dark">
        <tr>
          <th>Matrícula</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Email</th>
          <th>Carrera</th>
          <th>Materias</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="!loading && students.length === 0">
          <td colspan="7" class="text-center py-4 text-muted">
            No hay estudiantes registrados. Agrega el primero usando el formulario de abajo.
          </td>
        </tr>
        <tr v-for="student in students" :key="student.id">
          <td><strong>{{ student.enrollment }}</strong></td>
          <td>{{ student.firstName }}</td>
          <td>{{ student.lastName }}</td>
          <td>{{ student.email }}</td>
          <td>{{ student.career?.name || 'Sin carrera' }}</td>
          <td>
            <ul class="mb-0" v-if="student.subjects && student.subjects.length > 0">
              <li v-for="subject in student.subjects" :key="subject.id">
                {{ subject.name }}
              </li>
            </ul>
            <span v-else class="text-muted">Sin materias</span>
          </td>
          <td>
            <button class="btn btn-sm btn-primary me-2" @click="editStudent(student)">Editar</button>
            <button class="btn btn-sm btn-danger" @click="deleteStudent(student.id)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>
    </div>

    <hr class="my-4" />
    <div class="card card-professional">
      <div class="card-header">
        <h5 class="mb-0">{{ formTitle }}</h5>
      </div>
      <div class="card-body">
        <!-- Formulario de estudiante -->
        <form @submit.prevent="submitForm" class="form-professional">
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Matrícula</label>
          <input 
            v-model="form.enrollment" 
            class="form-control" 
            :class="{ 'is-invalid': enrollmentError }"
            pattern="^[A-Z]-\d{4}/\d+$"
            placeholder="Ej: B-6380/1"
            @input="validateEnrollment"
            required 
          />
          <div v-if="enrollmentError" class="invalid-feedback">
            {{ enrollmentError }}
          </div>
          <div class="form-text">Formato: Letra-Números/Número (Ej: B-6380/1)</div>
        </div>
        
        <div class="col-md-6 mb-3">
          <label class="form-label">Nombre</label>
          <input v-model="form.firstName" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Apellido</label>
          <input v-model="form.lastName" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Email</label>
          <input v-model="form.email" type="email" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Carrera</label>
          <select v-model="form.careerId" class="form-select" required>
            <option value="">Seleccionar carrera</option>
            <option v-for="career in careers" :key="career.id" :value="career.id">
              {{ career.name }} ({{ career.durationYears }} años)
            </option>
          </select>
        </div>

        <div class="col-12 mb-3">
          <label class="form-label">Materias</label>
          <select v-model="form.subjectIds" multiple class="form-select" :disabled="subjects.length === 0">
            <option v-if="subjects.length === 0" disabled>No hay materias disponibles</option>
            <option v-for="subject in subjects" :key="subject.id" :value="subject.id">
              {{ subject.name }}
            </option>
          </select>
          <div class="form-text">Mantén Ctrl presionado para seleccionar múltiples materias</div>
        </div>

        <div class="col-12">
          <button type="submit" class="btn btn-success btn-professional me-2">Guardar</button>
          <button type="button" class="btn btn-secondary btn-professional" @click="resetForm">Cancelar</button>
        </div>
      </div>
    </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// Datos
const students = ref([])
const subjects = ref([])
const careers = ref([])

const emptyForm = () => ({
  id: null,
  enrollment: '',
  firstName: '',
  lastName: '',
  email: '',
  careerId: '',
  subjectIds: []
})

const form = ref(emptyForm())
const formTitle = ref('Agregar nuevo estudiante')
const enrollmentError = ref('')

// API base
const apiBase = 'http://localhost:8080/api'

// Estados de carga
const loading = ref(false)
const error = ref('')

// Fetch estudiantes
const fetchStudents = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await axios.get(`${apiBase}/students`)
    students.value = res.data || []
    console.log('Estudiantes cargados:', students.value)
  } catch (err) {
    console.error('Error al cargar estudiantes:', err)
    error.value = `Error al obtener los estudiantes: ${err.message}`
  } finally {
    loading.value = false
  }
}

// Fetch materias
const fetchSubjects = async () => {
  try {
    const res = await axios.get(`${apiBase}/subjects`)
    subjects.value = res.data || []
    console.log('Materias cargadas:', subjects.value)
  } catch (err) {
    console.error('Error al cargar materias:', err)
    error.value = `Error al obtener las materias: ${err.message}`
  }
}

// Fetch carreras
const fetchCareers = async () => {
  try {
    const res = await axios.get(`${apiBase}/careers`)
    careers.value = res.data || []
    console.log('Carreras cargadas:', careers.value)
  } catch (err) {
    console.error('Error al cargar carreras:', err)
    error.value = `Error al obtener las carreras: ${err.message}`
  }
}

// Guardar estudiante
const submitForm = async () => {
  // Validar matrícula antes de enviar
  validateEnrollment()
  if (enrollmentError.value) {
    return
  }
  
  try {
    const selectedSubjects = subjects.value.filter(sub =>
      form.value.subjectIds.includes(sub.id)
    )
    
    const careerId = parseInt(form.value.careerId)
    const selectedCareer = careers.value.find(c => c.id === careerId)
    console.log('Carrera seleccionada:', selectedCareer)
    console.log('ID de carrera del form:', careerId)

    const studentToSend = {
      id: form.value.id,
      enrollment: form.value.enrollment,
      firstName: form.value.firstName,
      lastName: form.value.lastName,
      email: form.value.email,
      careerId: careerId || null,
      subjectIds: form.value.subjectIds
    }
    
    console.log('Enviando estudiante:', studentToSend)

    if (form.value.id) {
      await axios.put(`${apiBase}/students/${form.value.id}`, studentToSend)
    } else {
      await axios.post(`${apiBase}/students`, studentToSend)
    }

    await fetchStudents()
    resetForm()
  } catch (err) {
    console.error('Error al guardar estudiante:', err)
    console.error('Response data:', err.response?.data)
    console.error('Response status:', err.response?.status)
    let errorMsg = 'Error desconocido'
    if (err.response?.data) {
      errorMsg = typeof err.response.data === 'string' ? err.response.data : JSON.stringify(err.response.data)
    } else if (err.message) {
      errorMsg = err.message
    }
    alert('Error al guardar estudiante: ' + errorMsg)
  }
}

// Eliminar estudiante
const deleteStudent = async (id) => {
  if (!confirm('¿Estás seguro de eliminar este estudiante?')) return

  try {
    await axios.delete(`${apiBase}/students/${id}`)
    await fetchStudents()
  } catch (err) {
    console.error('Error al eliminar estudiante:', err)
    alert('No se pudo eliminar el estudiante.')
  }
}

// Cargar formulario de edición
const editStudent = (student) => {
  form.value = {
    id: student.id,
    enrollment: student.enrollment,
    firstName: student.firstName,
    lastName: student.lastName,
    email: student.email,
    careerId: student.career?.id || '',
    subjectIds: student.subjects?.map(s => s.id) || []
  }
  console.log('Editando estudiante:', student)
  console.log('Form cargado:', form.value)
  formTitle.value = 'Editar estudiante'
}

// Validar matrícula
const validateEnrollment = () => {
  const pattern = /^[A-Z]-\d{4}\/\d+$/
  if (form.value.enrollment && !pattern.test(form.value.enrollment)) {
    enrollmentError.value = 'Formato inválido. Use: B-6380/1'
  } else {
    enrollmentError.value = ''
  }
}

// Resetear formulario
const resetForm = () => {
  form.value = emptyForm()
  formTitle.value = 'Agregar nuevo estudiante'
  enrollmentError.value = ''
}

// Inicialización
onMounted(async () => {
  await fetchStudents()
  await fetchSubjects()
  await fetchCareers()
})
</script>

<style scoped>
.table {
  font-size: 0.95rem;
}
</style>
