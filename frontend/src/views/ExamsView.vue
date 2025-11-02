<template>
  <div class="container mt-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li class="breadcrumb-item active">Gestión de Exámenes</li>
      </ol>
    </nav>

    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2><i class="fas fa-clipboard-list me-2"></i>Gestión de Exámenes</h2>
      <span class="badge bg-warning fs-6">{{ exams.length }} registrados</span>
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

    <!-- Tabla de exámenes -->
    <div class="table-responsive">
    <table class="table table-professional">
      <thead class="table-dark">
        <tr>
          <th>Estudiante</th>
          <th>Materia</th>
          <th>Profesor</th>
          <th>Fecha</th>
          <th>Nota</th>
          <th>Observaciones</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="!loading && exams.length === 0">
          <td colspan="7" class="text-center py-4 text-muted">
            No hay exámenes registrados. Agrega el primero usando el formulario de abajo.
          </td>
        </tr>
        <tr v-for="exam in exams" :key="exam.id">
          <td>{{ exam.student?.firstName }} {{ exam.student?.lastName }}</td>
          <td>{{ exam.subject?.name }}</td>
          <td>{{ exam.subject?.teacher?.firstName }} {{ exam.subject?.teacher?.lastName }}</td>
          <td>{{ formatDate(exam.examDate) }}</td>
          <td>
            <span class="badge" :class="getGradeBadgeClass(exam.grade)">
              {{ exam.grade }}/10
            </span>
          </td>
          <td>{{ exam.observations || 'Sin observaciones' }}</td>
          <td>
            <button class="btn btn-sm btn-primary me-2" @click="editExam(exam)">Editar</button>
            <button class="btn btn-sm btn-danger" @click="deleteExam(exam.id)">Eliminar</button>
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
        <!-- Formulario de examen -->
        <form @submit.prevent="submitForm" class="form-professional">
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Estudiante</label>
          <select v-model="form.studentId" class="form-select" required>
            <option value="">Seleccione un estudiante</option>
            <option v-for="student in students" :key="student.id" :value="student.id">
              {{ student.firstName }} {{ student.lastName }} - {{ student.career }}
            </option>
          </select>
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Materia</label>
          <select v-model="form.subjectId" class="form-select" required>
            <option value="">Seleccione una materia</option>
            <option v-for="subject in subjects" :key="subject.id" :value="subject.id">
              {{ subject.name }} - {{ subject.teacher?.firstName }} {{ subject.teacher?.lastName }}
            </option>
          </select>
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Fecha del Examen</label>
          <input v-model="form.examDate" type="date" class="form-control" required />
        </div>

        <div class="col-md-6 mb-3">
          <label class="form-label">Nota (0-10)</label>
          <input v-model.number="form.grade" type="number" min="0" max="10" step="0.1" class="form-control" required />
        </div>

        <div class="col-12 mb-3">
          <label class="form-label">Observaciones</label>
          <textarea v-model="form.observations" class="form-control" rows="3" placeholder="Observaciones opcionales"></textarea>
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

const apiBase = 'http://localhost:8080/api'

// Estados
const exams = ref([])
const students = ref([])
const subjects = ref([])
const loading = ref(false)
const error = ref('')

const emptyForm = () => ({
  id: null,
  studentId: '',
  subjectId: '',
  examDate: '',
  grade: '',
  observations: ''
})

const form = ref(emptyForm())
const formTitle = ref('Agregar nuevo examen')

// Funciones
const fetchExams = async () => {
  loading.value = true
  error.value = ''
  try {
    const res = await axios.get(`${apiBase}/exams`)
    exams.value = res.data || []
  } catch (err) {
    console.error('Error al cargar exámenes:', err)
    error.value = 'Error al cargar exámenes'
  } finally {
    loading.value = false
  }
}

const fetchStudents = async () => {
  try {
    const res = await axios.get(`${apiBase}/students`)
    students.value = res.data || []
  } catch (err) {
    console.error('Error al cargar estudiantes:', err)
  }
}

const fetchSubjects = async () => {
  try {
    const res = await axios.get(`${apiBase}/subjects`)
    subjects.value = res.data || []
  } catch (err) {
    console.error('Error al cargar materias:', err)
  }
}

const submitForm = async () => {
  try {
    const student = students.value.find(s => s.id === form.value.studentId)
    const subject = subjects.value.find(s => s.id === form.value.subjectId)

    const examToSend = {
      id: form.value.id,
      student: student,
      subject: subject,
      examDate: form.value.examDate,
      grade: form.value.grade,
      observations: form.value.observations
    }

    if (form.value.id) {
      await axios.put(`${apiBase}/exams/${form.value.id}`, examToSend)
    } else {
      await axios.post(`${apiBase}/exams`, examToSend)
    }

    await fetchExams()
    resetForm()
  } catch (err) {
    console.error('Error al guardar examen:', err)
    alert('Error al guardar el examen')
  }
}

const deleteExam = async (id) => {
  if (!confirm('¿Estás seguro de eliminar este examen?')) return

  try {
    await axios.delete(`${apiBase}/exams/${id}`)
    await fetchExams()
  } catch (err) {
    console.error('Error al eliminar examen:', err)
    alert('Error al eliminar el examen')
  }
}

const editExam = (exam) => {
  form.value = {
    id: exam.id,
    studentId: exam.student?.id || '',
    subjectId: exam.subject?.id || '',
    examDate: exam.examDate,
    grade: exam.grade,
    observations: exam.observations || ''
  }
  formTitle.value = 'Editar examen'
}

const resetForm = () => {
  form.value = emptyForm()
  formTitle.value = 'Agregar nuevo examen'
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

onMounted(async () => {
  await Promise.all([fetchExams(), fetchStudents(), fetchSubjects()])
})
</script>

<style scoped>
.table {
  font-size: 0.95rem;
}
</style>
