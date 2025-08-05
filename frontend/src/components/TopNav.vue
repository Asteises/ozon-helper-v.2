<template>
  <nav v-if="uiState.isNavVisible" class="top-nav">
    <!-- Левое меню (бургер) -->
    <div class="menu-left">
      <button class="burger-btn" @click="toggleBurgerMenu" aria-label="Открыть меню">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <line x1="3" y1="6" x2="21" y2="6"></line>
          <line x1="3" y1="12" x2="21" y2="12"></line>
          <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
      </button>
      <transition name="slide-down">
        <ul v-if="uiState.isBurgerMenuOpen" class="dropdown-menu left">
          <li @click="navigateTo('/start')">Начать работу</li>
          <li @click="navigateTo('/faq')">FAQ</li>
        </ul>
      </transition>
    </div>

    <!-- Заголовок -->
    <div class="nav-title">
      <slot></slot>
    </div>

    <!-- Правое меню (личный кабинет) -->
    <div class="menu-right">
      <button class="profile-btn" @click="toggleProfileDropdown" aria-label="Личный кабинет">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="7" r="4"></circle>
          <path d="M5.5 21c1.5-3 4.5-5 6.5-5s5 2 6.5 5"></path>
        </svg>
      </button>
      <transition name="slide-down">
        <ul v-if="uiState.isProfileDropdownOpen" class="dropdown-menu right">
          <li @click="navigateTo('/profile')">Профиль</li>
          <li @click="logout">Выход</li>
        </ul>
      </transition>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useUiStore } from '../store/ui-store'

const router = useRouter()
const uiState = useUiStore()

// Методы для работы с Pinia
const toggleBurgerMenu = () => {
  uiState.toggleBurgerMenu()
  uiState.closeProfileDropdown()
}

const toggleProfileDropdown = () => {
  uiState.toggleProfileDropdown()
  uiState.closeBurgerMenu()
}

// Закрытие при клике вне
const closeMenusOnClickOutside = (e: MouseEvent) => {
  const target = e.target as HTMLElement
  if (!target.closest('.menu-left') && !target.closest('.menu-right')) {
    uiState.closeBurgerMenu()
    uiState.closeProfileDropdown()
  }
}

onMounted(() => {
  document.addEventListener('click', closeMenusOnClickOutside)

  // Автоматическое закрытие при смене роута
  router.afterEach(() => {
    uiState.closeBurgerMenu()
    uiState.closeProfileDropdown()
  })
})

onBeforeUnmount(() => {
  document.removeEventListener('click', closeMenusOnClickOutside)
})

// Навигация
const navigateTo = (path: string) => {
  router.push(path)
  uiState.closeBurgerMenu()
  uiState.closeProfileDropdown()
}

// Выход
const logout = () => {
  console.log('Выход пользователя')
  router.push('/login')
  uiState.closeProfileDropdown()
}
</script>

<style scoped>
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 56px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
  z-index: 1000;
  padding: 0 10px;
  box-sizing: border-box;
}

.burger-btn,
.profile-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 6px 10px;
  color: #2b2b2b;
  display: flex;
  align-items: center;
  justify-content: center;
}

.burger-btn svg,
.profile-btn svg {
  width: 24px;
  height: 24px;
  stroke: #2b2b2b;
}

.nav-title {
  flex: 1;
  text-align: center;
  font-weight: bold;
  font-size: 16px;
}

.dropdown-menu {
  position: absolute;
  top: 56px;
  background: #ffffff;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.15);
  list-style: none;
  padding: 5px 0;
  margin: 0;
  min-width: 150px;
}

.dropdown-menu.left {
  left: 10px;
}

.dropdown-menu.right {
  right: 10px;
}

.dropdown-menu li {
  padding: 10px 15px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s;
}

.dropdown-menu li:hover {
  background: #f2f2f2;
}

/* Анимация slide-down */
.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.25s ease-out;
}

.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.slide-down-enter-to,
.slide-down-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>
