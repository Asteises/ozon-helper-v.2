import type {CheckUserData} from '../types/tg-user'
import {useAuthStore} from "../store/auth-store";

declare global {
    interface Window {
        Telegram: any
    }
}

export async function verifyAndCheckUser(): Promise<void> {

    const authStore = useAuthStore()
    const tg = window.Telegram?.WebApp

    console.log("Income Telegram WebApp: ", tg)

    if (!tg?.initDataUnsafe?.user) {
        console.log("Пользователь не прошел верификацию")
        authStore.reset()
        return
    }

    const user = tg.initDataUnsafe.user

    console.log("Income Telegram User: ", user)

    const requestPayload: CheckUserData = {
        telegramUserId: user.id,
        telegramInitData: tg.initData
    }

    try {
        const response = await fetch('/api/user/check', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(requestPayload)
        })

        console.log("Server Response: ", response)

        if (response.status === 200) { // пользователь провалидирован и зарегистрирован
            // Пользователь найден в БД
            authStore.setVerified(true)
            authStore.setAuthorized()
        } else if (response.status === 404) { // пользователь провалидирован, но не найден в БД
            authStore.setVerified(true)
            authStore.setUnauthorized()
        } else {
            // Ошибка валидации initData
            authStore.reset()
        }
    } catch (err) {
        console.error('Ошибка запроса /check:', err)
        authStore.reset()
    }
}
