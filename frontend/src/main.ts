import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { verifyAndCheckUser } from './utils/auth-guard'
import './assets/styles.css'


const app = createApp(App)

// Инициализируем Pinia
const pinia = createPinia()
app.use(pinia)

// Инициализируем Router
app.use(router)

// Глобальная проверка пользователя
router.beforeEach(async (to) => {
    // Страницы, куда можно без проверки (иначе зациклится)
    if (to.path === '/register' || to.path === '/error') return true

    // Проверка через /check
    const result = await verifyAndCheckUser()

    if (result === 'unauthorized') {
        return '/error' // Ошибка валидации initData
    }
    if (result === 'not_registered') {
        return '/register' // Пользователь не зарегистрирован
    }
    return true // Всё ок — продолжаем переход
})

app.mount('#app')
