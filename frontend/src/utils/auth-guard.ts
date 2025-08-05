import type {CheckUserData} from '../types/tg-user'

declare global {
    interface Window {
        Telegram: any
    }
}

export async function verifyAndCheckUser(): Promise<'ok' | 'unauthorized' | 'not_registered'> {
    const tg = window.Telegram?.WebApp

    console.log("Income Telegram WebApp: ", tg)

    if (!tg?.initDataUnsafe?.user) return 'unauthorized'

    const user = tg.initDataUnsafe.user

    console.log("Income Telegram User: ", user)

    const requestPayload: CheckUserData = {
        telegramUserId: user.id,
        telegramInitData: tg.initData
    }

    try {
        const response = await fetch('/dev/bot/ozon/helper/check', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(requestPayload)
        })

        console.log("Server Response: ", response)

        if (response.status === 200) {
            return 'ok'               // пользователь зарегистрирован
        } else if (response.status === 404) {
            return 'not_registered'   // пользователь не найден
        } else if (response.status === 401) {
            return 'unauthorized'     // ошибка валидации initData
        } else {
            return 'unauthorized'     // fallback на всякий случай
        }
    } catch (err) {
        console.error('Ошибка запроса /check:', err)
        return 'unauthorized'
    }
}
