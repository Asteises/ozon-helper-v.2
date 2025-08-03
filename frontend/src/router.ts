import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import RegisterPage from './pages/RegisterPage.vue'
import MenuPage from './pages/MenuPage.vue'
import MinPricePage from './pages/MinPricePage.vue'
import ProfitReportPage from './pages/ProfitReportPage.vue'

const routes: RouteRecordRaw[] = [
    { path: '/', component: RegisterPage },
    { path: '/menu', component: MenuPage },
    { path: '/min-price', component: MinPricePage },
    { path: '/profit-report', component: ProfitReportPage }
]

export default createRouter({
    history: createWebHistory('/dev/bot/ozon/helper/miniapp/'),
    routes
})
