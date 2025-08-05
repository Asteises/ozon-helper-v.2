import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {

    state: () => ({
        clientId: '' as string,
        apiKey: '' as string,
        telegramUser: null as any | null
    }),
    actions: {
        setCredentials(clientId: string, apiKey: string) {
            this.clientId = clientId
            this.apiKey = apiKey
        },
        setTelegramUser(user: any) {
            this.telegramUser = user
        },
        clearUser() {
            this.clientId = ''
            this.apiKey = ''
            this.telegramUser = null
        }
    }
})
