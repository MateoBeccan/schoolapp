<template>
  <div class="container mt-4">
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb breadcrumb-professional">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Carreras</li>
      </ol>
    </nav>
    
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-graduation-cap me-2"></i>Gestión de Carreras</h2>
      <button class="btn btn-primary btn-professional" @click="showModal = true">
        <i class="fas fa-plus me-1"></i>Nueva Carrera
      </button>
    </div>

    <div class="card card-professional">
      <div class="card-body">
        <div class="table-responsive">
          <table class="table table-professional">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Descripción</th>
                <th>Duración (años)</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="career in careers" :key="career.id">
                <td>{{ career.name }}</td>
                <td>{{ career.description }}</td>
                <td>{{ career.durationYears }}</td>
                <td>
                  <button class="btn btn-sm btn-outline-primary me-2" @click="editCareer(career)">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-danger" @click="deleteCareer(career.id)">
                    <i class="fas fa-trash"></i>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" :class="{ show: showModal }" :style="{ display: showModal ? 'block' : 'none' }" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ editingCareer ? 'Editar' : 'Nueva' }} Carrera</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <form @submit.prevent="saveCareer">
            <div class="modal-body">
              <div class="mb-3">
                <label class="form-label">Nombre</label>
                <input v-model="form.name" type="text" class="form-control" required>
              </div>
              <div class="mb-3">
                <label class="form-label">Descripción</label>
                <textarea v-model="form.description" class="form-control" rows="3"></textarea>
              </div>
              <div class="mb-3">
                <label class="form-label">Duración (años)</label>
                <input v-model="form.durationYears" type="number" class="form-control" min="1" max="10" required>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">Cancelar</button>
              <button type="submit" class="btn btn-primary">Guardar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal-backdrop fade show"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const apiBase = 'http://localhost:8080/api'
const careers = ref([])
const showModal = ref(false)
const editingCareer = ref(null)
const form = ref({ name: '', description: '', durationYears: 1 })

const loadCareers = async () => {
  try {
    const response = await axios.get(`${apiBase}/careers`)
    careers.value = response.data
  } catch (error) {
    console.error('Error loading careers:', error)
  }
}

const saveCareer = async () => {
  try {
    console.log('Enviando carrera:', form.value)
    if (editingCareer.value) {
      const response = await axios.put(`${apiBase}/careers/${editingCareer.value.id}`, form.value)
      console.log('Carrera actualizada:', response.data)
    } else {
      const response = await axios.post(`${apiBase}/careers`, form.value)
      console.log('Carrera creada:', response.data)
    }
    await loadCareers()
    closeModal()
  } catch (error) {
    console.error('Error saving career:', error)
    alert('Error al guardar la carrera: ' + (error.response?.data?.message || error.message))
  }
}

const editCareer = (career) => {
  editingCareer.value = career
  form.value = { ...career }
  showModal.value = true
}

const deleteCareer = async (id) => {
  if (confirm('¿Está seguro de eliminar esta carrera?')) {
    try {
      await axios.delete(`${apiBase}/careers/${id}`)
      await loadCareers()
    } catch (error) {
      console.error('Error deleting career:', error)
    }
  }
}

const closeModal = () => {
  showModal.value = false
  editingCareer.value = null
  form.value = { name: '', description: '', durationYears: 1 }
}

onMounted(loadCareers)
</script>