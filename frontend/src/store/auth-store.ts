import {defineStore} from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        verified: false,        // прошёл ли верификацию через Telegram.initData
        authorized: false,      // найден ли в БД
        unauthorized: false     // не найден в БД (для регистрации)
    }),
    actions: {
        setVerified(value: boolean) {
            this.verified = value
        },
        setAuthorized() {
            this.authorized = true
            this.unauthorized = false
        },
        setUnauthorized() {
            this.authorized = false
            this.unauthorized = true
        },
        reset() {
            this.verified = false
            this.authorized = false
            this.unauthorized = false
        }
    }
})