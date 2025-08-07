import type {RouteRecordRaw} from 'vue-router'
import {createRouter, createWebHistory} from 'vue-router'
import RegisterPage from './pages/RegisterPage.vue'
import MenuPage from './pages/MenuPage.vue'
import MinPricePage from './pages/MinPricePage.vue'
import ProfitReportPage from './pages/ProfitReportPage.vue'
import ErrorPage from "./components/ErrorPage.vue";
import {verifyAndCheckUser} from "./utils/auth-guard";
import {useAuthStore} from './store/auth-store'

const routes: RouteRecordRaw[] = [

    {path: '/', redirect: '/menu'},
    {path: '/menu', component: MenuPage},
    {path: '/error', component: ErrorPage, meta: {hideNav: true}},
    {path: '/register', component: RegisterPage},
    {path: '/min-price', component: MinPricePage},
    {path: '/profit-report', component: ProfitReportPage},
    {path: '/:pathMatch(.*)*', redirect: '/'},
]
const base = import.meta.env.DEV ? '/' : '/'

const router = createRouter({
    history: createWebHistory(base),
    routes
})

// Guard
router.beforeEach(async (to) => {
    const authStore = useAuthStore()

    // Пропуск guard при локальной разработке
    if (import.meta.env.DEV) {
        console.warn('DEV mode: skip Telegram check')
        return true
    }

    // Разрешаем доступ к ErrorPage
    if (to.path === '/error') return true

    // Если идём на /menu — выполняем проверку
    if (to.path === '/menu') {
        await verifyAndCheckUser()

        // Если не прошёл верификацию — отправляем на /error
        if (!authStore.verified) return '/error'

        // Если верифицирован, но не зарегистрирован — перенаправляем на регистрацию
        if (authStore.unauthorized) return '/register'

        // Если верифицирован и зарегистрирован — остаёмся на /menu
        return true
    }

    if (!authStore.verified) return '/error'

    // - если unauthorized (нет в БД) — доступна только /register
    if (authStore.unauthorized && to.path !== '/register') {
        return '/register'
    }

    // Всё ок
    return true
})

export default router
