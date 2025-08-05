<template>
  <div class="container">
    <TopNav/>
    <h1>Добро пожаловать в Ozon Helper</h1>
    <p>Выберите действие в меню:</p>

    <button @click="getData" :disabled="loading">
      {{ loading ? 'Загрузка товаров...' : 'Получить все товары' }}
    </button>
  </div>
</template>

<script setup lang="ts">
import TopNav from '@/components/TopNav.vue'
import {ref} from "vue";
import {CheckUserData} from "../types/tg-user";

const loading = ref(false)

declare global {
  interface Window {
    Telegram: any;
  }
}

const tg = window.Telegram?.WebApp;

console.log("Income Telegram WebbApp: ", tg)

const getData = async () => {
  loading.value = true;

  try {
    if (tg.initDataUnsafe?.user) {
      const user = tg.initDataUnsafe.user;
      const requestPayload: CheckUserData = {
        telegramUserId: user.id,
        telegramInitData: user.initData
      }
      // const response = await fetch('dev/bot/ozon/helper/api/product/list', {
      //   method: 'POST',
      //   headers: {'Content-Type': 'application/json'},
      //   body: JSON.stringify(requestPayload)
      // });

      const response = await fetch('dev/bot/ozon/helper/api/product/list/test', {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
      });

      if (!response.ok) {
        console.log('Ошибка при получении списка товаров')
      } else {
        console.log('Income product list: ', response.json())
      }
    }
  } catch (error) {
    console.log('Error while get product list: ', error)
  }
}
</script>
