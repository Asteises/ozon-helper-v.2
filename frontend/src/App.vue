<template>
  <div>
    <TopNav v-if="!route.meta.hideNav" />
    <main :style="{ paddingTop: !route.meta.hideNav ? '56px' : '0' }">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import TopNav from '@/components/TopNav.vue'

const route = useRoute()
</script>
