import type {RouteRecordRaw} from 'vue-router'
import {createRouter, createWebHistory} from 'vue-router'
import RegisterPage from './pages/RegisterPage.vue'
import MenuPage from './pages/MenuPage.vue'
import MinPricePage from './pages/MinPricePage.vue'
import ProfitReportPage from './pages/ProfitReportPage.vue'
import ErrorPage from "./components/ErrorPage.vue";
import {verifyAndCheckUser} from "./utils/auth-guard";

const routes: RouteRecordRaw[] = [

    {path: '/', redirect: '/menu'},
    {path: '/menu', component: MenuPage},
    {path: '/error', component: ErrorPage, meta: {hideNav: true}},
    {path: '/register', component: RegisterPage},
    {path: '/min-price', component: MinPricePage},
    {path: '/profit-report', component: ProfitReportPage},
    {path: '/:pathMatch(.*)*', redirect: '/'},
]
const base = import.meta.env.DEV ? '/' : '/dev/bot/ozon/helper/miniapp/'

const router = createRouter({
    history: createWebHistory(base),
    routes
})

// Guard
router.beforeEach(async (to) => {
    // Разрешаем ErrorPage и регистрацию без проверки
    if (to.path === '/error' || to.path === '/register') return true

    // Пропуск guard при локальной разработке
    if (import.meta.env.DEV) {
        console.warn('DEV mode: skip Telegram check')
        return true
    }

    // В продакшене проверяем через /check
    console.log('Try to verify and check user...')
    const result = await verifyAndCheckUser()

    if (result === 'unauthorized') return '/error'
    if (result === 'not_registered') return '/register'
    return true
})

export default router
