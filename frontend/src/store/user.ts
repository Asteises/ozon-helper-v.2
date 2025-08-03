import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        clientId: '' as string,
        apiKey: '' as string
    }),
    actions: {
        setCredentials(clientId: string, apiKey: string) {
            this.clientId = clientId
            this.apiKey = apiKey
        }
    }
})
