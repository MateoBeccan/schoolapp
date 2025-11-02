<template>
  <div class="container mt-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Materias</li>
      </ol>
    </nav>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-book me-2"></i>Materias</h2>
      <span class="badge bg-info fs-6">{{ subjects.length }} registradas</span>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="text-center py-4">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Cargando...</span>
      </div>
    </div>

    <!-- Error -->
    <div v-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <!-- Tabla de materias -->
    <table class="table table-bordered">
      <thead class="table-dark">
        <tr>
          <th>Nombre</th>
          <th>Descripción</th>
          <th>Créditos</th>
          <th>Docente</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="!loading && subjects.length === 0">
          <td colspan="5" class="text-center py-4 text-muted">
            No hay materias registradas. Agrega la primera usando el formulario de abajo.
          </td>
        </tr>
        <tr v-for="subject in subjects" :key="subject.id">
          <td>{{ subject.name }}</td>
          <td>{{ subject.description }}</td>
          <td>{{ subject.credits }}</td>
          <td>{{ subject.teacher?.firstName }} {{ subject.teacher?.lastName }}</td>
          <td>
            <button class="btn btn-sm btn-primary me-2" @click="editSubject(subject)">Editar</button>
            <button class="btn btn-sm btn-danger" @click="deleteSubject(subject.id)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <hr class="my-4" />
    <h3>{{ formTitle }}</h3>

    <!-- Formulario de materia -->
    <form @submit.prevent="submitForm">
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Nombre</label>
          <input v-model="form.name" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Descripción</label>
          <input v-model="form.description" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Créditos</label>
          <input v-model.number="form.credits" type="number" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Docente</label>
          <select v-model="form.teacherId" class="form-select" required>
            <option disabled value="">Seleccione un docente</option>
            <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.id">
              {{ teacher.firstName }} {{ teacher.lastName }}
            </option>
          </select>
        </div>

        <div class="col-12">
          <button type="submit" class="btn btn-success me-2">Guardar</button>
          <button type="button" class="btn btn-secondary" @click="resetForm">Cancelar</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

// Base API
const apiBase = 'http://localhost:8080/api'

// Datos
const subjects = ref([])
const teachers = ref([])
const loading = ref(false)
const error = ref('')

const emptyForm = () => ({
  id: null,
  name: '',
  description: '',
  credits: 0,
  teacherId: ''
})

const form = ref(emptyForm())
const formTitle = ref('Agregar nueva materia')

// Cargar materias
const fetchSubjects = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await axios.get(`${apiBase}/subjects`)
    subjects.value = res.data || []
  } catch (err) {
    console.error('Error al cargar materias:', err)
    error.value = 'Error al cargar materias'
  } finally {
    loading.value = false
  }
}

// Cargar docentes
const fetchTeachers = async () => {
  try {
    const res = await axios.get(`${apiBase}/teachers`)
    teachers.value = res.data
  } catch (err) {
    console.error('Error al cargar docentes:', err)
    alert('Error al cargar docentes.')
  }
}

// Guardar o actualizar materia
const submitForm = async () => {
  try {
    const subjectToSend = {
      id: form.value.id,
      name: form.value.name,
      description: form.value.description,
      credits: form.value.credits,
      teacherId: parseInt(form.value.teacherId)
    }

    if (form.value.id) {
      await axios.put(`${apiBase}/subjects/${form.value.id}`, subjectToSend)
    } else {
      await axios.post(`${apiBase}/subjects`, subjectToSend)
    }

    await fetchSubjects()
    resetForm()
  } catch (err) {
    console.error('Error al guardar materia:', err)
    alert('No se pudo guardar la materia.')
  }
}

// Eliminar materia
const deleteSubject = async (id) => {
  if (!confirm('¿Eliminar esta materia?')) return

  try {
    await axios.delete(`${apiBase}/subjects/${id}`)
    await fetchSubjects()
  } catch (err) {
    console.error('Error al eliminar materia:', err)
    alert('No se pudo eliminar la materia.')
  }
}

// Editar materia
const editSubject = (subject) => {
  form.value = {
    id: subject.id,
    name: subject.name,
    description: subject.description,
    credits: subject.credits,
    teacherId: subject.teacher?.id || ''
  }
  formTitle.value = 'Editar materia'
}

// Resetear formulario
const resetForm = () => {
  form.value = emptyForm()
  formTitle.value = 'Agregar nueva materia'
}

// Inicializar
onMounted(async () => {
  await fetchSubjects()
  await fetchTeachers()
})
</script>

<style scoped>
.table {
  font-size: 0.95rem;
}
</style>
