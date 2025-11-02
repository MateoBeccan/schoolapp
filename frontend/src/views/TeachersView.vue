<template>
  <div class="container mt-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Docentes</li>
      </ol>
    </nav>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-chalkboard-teacher me-2"></i>Docentes</h2>
      <span class="badge bg-success fs-6">{{ teachers.length }} registrados</span>
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

    <table class="table table-bordered">
      <thead class="table-dark">
        <tr>
          <th>Matrícula</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Email</th>
          <th>Departamento</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="!loading && teachers.length === 0">
          <td colspan="6" class="text-center py-4 text-muted">
            No hay docentes registrados. Agrega el primero usando el formulario de abajo.
          </td>
        </tr>
        <tr v-for="teacher in teachers" :key="teacher.id">
          <td><strong>{{ teacher.enrollment }}</strong></td>
          <td>{{ teacher.firstName }}</td>
          <td>{{ teacher.lastName }}</td>
          <td>{{ teacher.email }}</td>
          <td>{{ teacher.department || 'Sin asignar' }}</td>
          <td>
            <button class="btn btn-sm btn-primary me-2" @click="editTeacher(teacher)">Editar</button>
            <button class="btn btn-sm btn-danger" @click="deleteTeacher(teacher.id)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <hr class="my-4" />
    <h3>{{ formTitle }}</h3>

    <form @submit.prevent="submitForm">
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Matrícula</label>
          <input 
            v-model="form.enrollment" 
            class="form-control" 
            pattern="^PROF-\d{3}$"
            placeholder="Ej: PROF-001"
            title="Formato: PROF-001"
            required 
          />
          <div class="form-text">Formato: PROF-001</div>
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

        <div class="col-md-12 mb-3">
          <label class="form-label">Departamento</label>
          <input v-model="form.department" class="form-control" required />
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

const teachers = ref([])

const form = ref({
  id: null,
  enrollment: '',
  firstName: '',
  lastName: '',
  email: '',
  department: ''
})

const loading = ref(false)
const error = ref('')

const formTitle = ref('Agregar nuevo docente')

const fetchTeachers = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await axios.get('http://localhost:8080/api/teachers')
    teachers.value = response.data || []
  } catch (err) {
    console.error('Error al cargar docentes:', err)
    error.value = 'Error al cargar docentes'
  } finally {
    loading.value = false
  }
}

const submitForm = async () => {
  const teacherToSend = { ...form.value }

  if (form.value.id) {
    await axios.put(`http://localhost:8080/api/teachers/${form.value.id}`, teacherToSend)
  } else {
    await axios.post('http://localhost:8080/api/teachers', teacherToSend)
  }

  await fetchTeachers()
  resetForm()
}

const editTeacher = (teacher) => {
  form.value = { ...teacher }
  formTitle.value = 'Editar docente'
}

const deleteTeacher = async (id) => {
  if (confirm('¿Eliminar este docente?')) {
    await axios.delete(`http://localhost:8080/api/teachers/${id}`)
    await fetchTeachers()
  }
}

const resetForm = () => {
  form.value = {
    id: null,
    enrollment: '',
    firstName: '',
    lastName: '',
    email: '',
    department: ''
  }
  formTitle.value = 'Agregar nuevo docente'
}

onMounted(fetchTeachers)
</script>
