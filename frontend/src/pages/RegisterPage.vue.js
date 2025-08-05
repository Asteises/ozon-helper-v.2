import { ref } from 'vue';
import { useRouter } from 'vue-router';
const tg = window.Telegram?.WebApp;
const router = useRouter();
const clientId = ref('');
const apiKey = ref('');
const message = ref('');
const loading = ref(false);
const errors = ref({});
const validate = () => {
    errors.value = {};
    if (!clientId.value) {
        errors.value.clientId = 'Введите Client ID';
    }
    else if (!/^[a-zA-Z0-9-]+$/.test(clientId.value)) {
        errors.value.clientId = 'Некорректный формат Client ID';
    }
    if (!apiKey.value) {
        errors.value.apiKey = 'Введите API Key';
    }
    else if (apiKey.value.length < 32) {
        errors.value.apiKey = 'Некорректный API Key';
    }
    return Object.keys(errors.value).length === 0;
};
const saveData = async () => {
    if (!validate())
        return;
    loading.value = true;
    message.value = '';
    try {
        if (!tg?.initDataUnsafe?.user) {
            throw new Error('Пользователь Telegram не найден');
        }
        const user = tg?.initDataUnsafe?.user;
        const registrationUserData = {
            telegramUserId: user.id,
            username: user.username,
            firstName: user.firstName,
            lastName: user.lastName,
            ozonDataForm: {
                clientId: clientId.value,
                apiKey: apiKey.value
            },
            telegramInitData: tg?.initData
        };
        const response = await fetch('/dev/bot/ozon/helper/save', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(registrationUserData)
        });
        if (!response.ok)
            throw new Error('Ошибка при сохранении данных');
        await router.push('/menu');
    }
    catch (err) {
        message.value = err.message;
    }
    finally {
        loading.value = false;
    }
};
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "container" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h2, __VLS_intrinsicElements.h2)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.label, __VLS_intrinsicElements.label)({
    for: "clientId",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.input)({
    id: "clientId",
    value: (__VLS_ctx.clientId),
    type: "text",
    placeholder: "Введите Client ID",
});
if (__VLS_ctx.errors.clientId) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
        ...{ class: "error" },
    });
    (__VLS_ctx.errors.clientId);
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.label, __VLS_intrinsicElements.label)({
    for: "apiKey",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.input)({
    id: "apiKey",
    value: (__VLS_ctx.apiKey),
    type: "text",
    placeholder: "Введите API Key",
});
if (__VLS_ctx.errors.apiKey) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
        ...{ class: "error" },
    });
    (__VLS_ctx.errors.apiKey);
}
__VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
    ...{ onClick: (__VLS_ctx.saveData) },
    disabled: (__VLS_ctx.loading),
});
(__VLS_ctx.loading ? 'Сохранение...' : 'Сохранить');
if (__VLS_ctx.message) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({});
    (__VLS_ctx.message);
}
/** @type {__VLS_StyleScopedClasses['container']} */ ;
/** @type {__VLS_StyleScopedClasses['error']} */ ;
/** @type {__VLS_StyleScopedClasses['error']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            clientId: clientId,
            apiKey: apiKey,
            message: message,
            loading: loading,
            errors: errors,
            saveData: saveData,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
