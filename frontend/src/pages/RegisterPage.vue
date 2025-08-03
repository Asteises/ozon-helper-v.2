<template>
  <div class="container">
    <h2>Введите данные Ozon</h2>

    <label for="clientId">Client ID</label>
    <input id="clientId" v-model="clientId" type="text" placeholder="Введите Client ID" />
    <p v-if="errors.clientId" class="error">{{ errors.clientId }}</p>

    <label for="apiKey">Seller API Key</label>
    <input id="apiKey" v-model="apiKey" type="text" placeholder="Введите API Key" />
    <p v-if="errors.apiKey" class="error">{{ errors.apiKey }}</p>

    <button @click="saveData" :disabled="loading">
      {{ loading ? 'Сохранение...' : 'Сохранить' }}
    </button>

    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import type { RegisterUserData } from '../types/user'

declare global {
  interface Window {
    Telegram: any;
  }
}

const tg = window.Telegram?.WebApp;
if (tg) {
  tg?.ready();
  console.log("Telegram object:", window.Telegram);
  console.log("WebApp:", tg);
  console.log("initData:", tg?.initData);
  console.log("initDataUnsafe:", tg?.initDataUnsafe);
  console.log("User:", tg?.initDataUnsafe?.user);
}

console.log("initData:", tg?.initData);
const user = tg?.initDataUnsafe?.user;

console.log("User from Telegram:", user);

const clientId = ref<string>('')
const apiKey = ref<string>('')
const message = ref<string>('')
const loading = ref<boolean>(false)

const router = useRouter()
const userStore = useUserStore()

const errors = ref<{ clientId?: string; apiKey?: string }>({})

const validate = (): boolean => {
  errors.value = {}

  if (!/^\d{6,}$/.test(clientId.value)) {
    errors.value.clientId = 'Client ID должен содержать только цифры и минимум 6 символов'
  }

  if (!apiKey.value.trim()) {
    errors.value.apiKey = 'API Key не может быть пустым'
  }

  return Object.keys(errors.value).length === 0
}

const saveData = async () => {

  if (!validate()) {
    return
  }

  loading.value = true
  message.value = ''

  try {

    if (!user) {
      throw new Error('Пользователь Telegram не найден')
    }

    const registerData: RegisterUserData = {
      telegramUserId: user.id,
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      ozonDataForm: {
        clientId: clientId.value,
        apiKey: apiKey.value
      }
    }

    const response = await fetch('/dev/bot/ozon/helper/save', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(registerData)
    })

    if (!response.ok) throw new Error('Ошибка при сохранении данных')

    userStore.setCredentials(clientId.value, apiKey.value)
    await router.push('/menu')
  } catch (err: any) {
    message.value = err.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.error {
  color: red;
  font-size: 0.9rem;
  margin-top: -4px;
  margin-bottom: 8px;
}
</style>
