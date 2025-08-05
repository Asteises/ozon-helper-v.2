export async function verifyAndCheckUser() {
    const tg = window.Telegram?.WebApp;
    if (!tg?.initDataUnsafe?.user)
        return 'unauthorized';
    const user = tg.initDataUnsafe.user;
    const requestPayload = {
        telegramUserId: user.id,
        telegramInitData: tg.initData
    };
    try {
        const response = await fetch('/dev/bot/ozon/helper/check', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestPayload)
        });
        if (response.status === 200) {
            return 'ok'; // пользователь зарегистрирован
        }
        else if (response.status === 404) {
            return 'not_registered'; // пользователь не найден
        }
        else if (response.status === 401) {
            return 'unauthorized'; // ошибка валидации initData
        }
        else {
            return 'unauthorized'; // fallback на всякий случай
        }
    }
    catch (err) {
        console.error('Ошибка запроса /check:', err);
        return 'unauthorized';
    }
}
