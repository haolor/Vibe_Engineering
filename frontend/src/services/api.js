import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
})

// Set default headers
api.defaults.headers.common['Content-Type'] = 'application/json'

// Add token to requests if available
const token = localStorage.getItem('token')
if (token) {
  api.defaults.headers.common['Authorization'] = `Token ${token}`
}

// Interceptor để tự động thêm token khi có
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Token ${token}`
  }
  // Nếu là FormData, không set Content-Type (để browser tự set với boundary)
  if (config.data instanceof FormData) {
    delete config.headers['Content-Type']
  }
  return config
})

// Interceptor để log lỗi chi tiết từ server
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      console.error('--- API ERROR DETAILS ---')
      console.error('URL:', error.config.method.toUpperCase(), error.config.url)
      console.error('Status:', error.response.status)
      console.error('Error Data:', JSON.stringify(error.response.data, null, 2))
      console.error('--------------------------')
    }
    return Promise.reject(error)
  }
)

export default api

