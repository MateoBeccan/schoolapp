import { ref } from 'vue'
import axios from 'axios'

export function useApi(baseUrl) {
  const loading = ref(false)
  const error = ref('')
  const data = ref([])

  const fetchData = async (endpoint = '') => {
    loading.value = true
    error.value = ''
    try {
      const response = await axios.get(`${baseUrl}${endpoint}`)
      data.value = response.data || []
      return response.data
    } catch (err) {
      console.error(`Error fetching data from ${baseUrl}${endpoint}:`, err)
      error.value = `Error al cargar datos: ${err.message}`
      throw err
    } finally {
      loading.value = false
    }
  }

  const createItem = async (item) => {
    try {
      const response = await axios.post(baseUrl, item)
      return response.data
    } catch (err) {
      console.error('Error creating item:', err)
      throw err
    }
  }

  const updateItem = async (id, item) => {
    try {
      const response = await axios.put(`${baseUrl}/${id}`, item)
      return response.data
    } catch (err) {
      console.error('Error updating item:', err)
      throw err
    }
  }

  const deleteItem = async (id) => {
    try {
      await axios.delete(`${baseUrl}/${id}`)
      return true
    } catch (err) {
      console.error('Error deleting item:', err)
      throw err
    }
  }

  return {
    loading,
    error,
    data,
    fetchData,
    createItem,
    updateItem,
    deleteItem
  }
}