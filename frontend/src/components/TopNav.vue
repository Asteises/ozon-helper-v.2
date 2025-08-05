<template>
  <header class="top-nav">
    <!-- Левая ссылка -->
    <router-link to="/" class="menu-link">Menu</router-link>

    <!-- Правая иконка профиля -->
    <div class="profile-container" @click="toggleDropdown">
      <svg xmlns="http://www.w3.org/2000/svg" class="profile-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
        <circle cx="12" cy="7" r="4" />
        <path d="M5.5 21a8.38 8.38 0 0 1 13 0" />
      </svg>

      <!-- Выпадающее меню -->
      <div v-if="isProfileDropdownOpen" class="dropdown">
        <router-link to="/profile" class="dropdown-item">Профиль</router-link>
        <button class="dropdown-item" @click.stop="logout">Выйти</button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { storeToRefs } from 'pinia'
import { useUiStore } from '../store/ui-store'
import { onMounted, onBeforeUnmount } from 'vue'

const uiStore = useUiStore()
const { isProfileDropdownOpen } = storeToRefs(uiStore)

function toggleDropdown() {
  uiStore.toggleProfileDropdown()
}

function handleClickOutside(event) {
  const dropdown = document.querySelector('.profile-container')
  if (dropdown && !dropdown.contains(event.target)) {
    uiStore.closeProfileDropdown()
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})

function logout() {
  uiStore.closeProfileDropdown()
}
</script>

<style scoped>
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 48px;
  background-color: var(--tg-theme-bg-color, #1c1c1c);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
  z-index: 1000;
  font-size: 1rem;
}

.menu-link {
  font-weight: 600;
  color: var(--tg-theme-link-color, #3390ec);
  text-decoration: none;
}

.menu-link:hover {
  opacity: 0.8;
}

.profile-container {
  position: relative;
  cursor: pointer;
}

.profile-icon {
  width: 24px;
  height: 24px;
  stroke: var(--tg-theme-text-color, #fff);
}

.dropdown {
  position: absolute;
  right: 0;
  top: 40px;
  background-color: var(--tg-theme-secondary-bg-color, #2a2a2a);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  min-width: 140px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.dropdown-item {
  padding: 10px 14px;
  font-size: 0.9rem;
  text-align: left;
  color: var(--tg-theme-text-color, #fff);
  background: none;
  border: none;
  cursor: pointer;
  text-decoration: none;
}

.dropdown-item:hover {
  background-color: rgba(255, 255, 255, 0.05);
}
</style>