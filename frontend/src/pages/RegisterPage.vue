<template>
  <div class="container">
    <h2>Введите данные Ozon</h2>

    <label for="clientId">Client ID</label>
    <input id="clientId" v-model="clientId" type="text" placeholder="Введите Client ID"/>
    <p v-if="errors.clientId" class="error">{{ errors.clientId }}</p>

    <label for="apiKey">Seller API Key</label>
    <input id="apiKey" v-model="apiKey" type="text" placeholder="Введите API Key"/>
    <p v-if="errors.apiKey" class="error">{{ errors.apiKey }}</p>

    <button @click="saveData" :disabled="loading">
      {{ loading ? 'Сохранение...' : 'Сохранить' }}
    </button>

    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import type {RegisterUserData} from "../types/tg-user.ts";

declare global {
  interface Window {
    Telegram: any;
  }
}

const tg = window.Telegram?.WebApp;
const router = useRouter();

const clientId = ref('')
const apiKey = ref('')
const message = ref('')
const loading = ref(false)

const errors = ref<{ clientId?: string; apiKey?: string }>({})

const validate = (): boolean => {
  errors.value = {}

  if (!clientId.value) {
    errors.value.clientId = 'Введите Client ID'
  } else if (!/^[a-zA-Z0-9-]+$/.test(clientId.value)) {
    errors.value.clientId = 'Client ID должен содержать только цифры и латинские буквы.'
  }
  if (!apiKey.value) {
    errors.value.apiKey = 'Введите API Key'
  } else if (apiKey.value.length < 32) {
    errors.value.apiKey = 'API Key должен быть не менее 32 символов.'
  }

  return Object.keys(errors.value).length === 0
}

const saveData = async () => {
  if (!validate()) return

  loading.value = true
  message.value = ''

  try {
    if (!tg?.initDataUnsafe?.user) {
      throw new Error('Пользователь Telegram не найден')
    }

    const user = tg?.initDataUnsafe?.user

    const registrationUserData: RegisterUserData = {
      telegramUserId: user.id,
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      ozonDataForm: {
        clientId: clientId.value,
        apiKey: apiKey.value
      },
      telegramInitData: tg?.initData
    }

    const response = await fetch('/api/user/save', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(registrationUserData)
    })

    if (!response.ok) throw new Error('Ошибка при сохранении данных')

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
