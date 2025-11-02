import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// Importar Bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'

// Importar estilos profesionales
import './styles.css'

const app = createApp(App)

app.use(router)

app.mount('#app')
