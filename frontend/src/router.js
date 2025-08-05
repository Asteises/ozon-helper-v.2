import { createRouter, createWebHistory } from 'vue-router';
import RegisterPage from './pages/RegisterPage.vue';
import MenuPage from './pages/MenuPage.vue';
import MinPricePage from './pages/MinPricePage.vue';
import ProfitReportPage from './pages/ProfitReportPage.vue';
import ErrorPage from "./components/ErrorPage.vue";
const routes = [
    { path: '/error', component: ErrorPage, meta: { hideNav: true } },
    { path: '/:pathMatch(.*)*', redirect: '/' },
    { path: '/', component: MenuPage },
    { path: '/register', component: RegisterPage },
    { path: '/min-price', component: MinPricePage },
    { path: '/profit-report', component: ProfitReportPage }
];
export default createRouter({
    history: createWebHistory('/dev/bot/ozon/helper/miniapp/'),
    routes
});
