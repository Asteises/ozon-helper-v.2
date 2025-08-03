<template>
  <div class="container">
    <h2>Введите данные Ozon</h2>

    <label for="clientId">Client ID</label>
    <input id="clientId" v-model="clientId" type="text" placeholder="Введите Client ID" />

    <label for="apiKey">Seller API Key</label>
    <input id="apiKey" v-model="apiKey" type="text" placeholder="Введите API Key" />

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

const clientId = ref<string>('')
const apiKey = ref<string>('')
const message = ref<string>('')
const loading = ref<boolean>(false)

const router = useRouter()
const userStore = useUserStore()

const saveData = async () => {
  loading.value = true
  message.value = ''

  try {
    const response = await fetch('/save', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ clientId: clientId.value, apiKey: apiKey.value })
    })

    if (!response.ok) throw new Error('Ошибка при сохранении данных')

    userStore.setCredentials(clientId.value, apiKey.value)
    router.push('/menu')
  } catch (err: any) {
    message.value = err.message
  } finally {
    loading.value = false
  }
}
</script>
