import { defineStore } from 'pinia';
export const useUserStore = defineStore('user', {
    state: () => ({
        clientId: '',
        apiKey: '',
        telegramUser: null
    }),
    actions: {
        setCredentials(clientId, apiKey) {
            this.clientId = clientId;
            this.apiKey = apiKey;
        },
        setTelegramUser(user) {
            this.telegramUser = user;
        },
        clearUser() {
            this.clientId = '';
            this.apiKey = '';
            this.telegramUser = null;
        }
    }
});
