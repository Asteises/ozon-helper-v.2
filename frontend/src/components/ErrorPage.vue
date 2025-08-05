<template>
  <div class="container">
    <!-- Иконка ошибки -->
    <div class="error-icon">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
        <circle cx="12" cy="12" r="10" fill="#ff4d4f"/>
        <line x1="12" y1="7" x2="12" y2="13" stroke="#fff" stroke-width="2" stroke-linecap="round"/>
        <circle cx="12" cy="17" r="1.2" fill="#fff"/>
      </svg>
    </div>

    <!-- Текст ошибки -->
    <h2>Ошибка авторизации</h2>
    <p class="error-text">
      Ваши данные не прошли проверку.<br/>
      Попробуйте открыть Mini App снова через Telegram.
    </p>

    <!-- Кнопка -->
    <button @click="goToBot">Перейти к боту</button>
  </div>
</template>

<script setup lang="ts">
function goToBot() {
  // Переход к боту через deeplink
  window.location.href = 'https://t.me/safe_wholesale_bot'
}
</script>

<style scoped>
.error-container {
  text-align: center;
  padding: 30px 20px;
}

.error-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.error-icon svg {
  width: 60px;
  height: 60px;
}

/* Текст */
.error-text {
  margin-bottom: 20px;
  font-size: 1rem;
  line-height: 1.4;
  color: var(--tg-theme-text-color, #000);
}

button:hover {
  filter: brightness(0.9);
}
</style>
