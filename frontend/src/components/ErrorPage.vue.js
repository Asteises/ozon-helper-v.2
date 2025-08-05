function goToBot() {
    // Переход к боту через deeplink
    window.location.href = 'https://t.me/safe_wholesale_bot';
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['error-icon']} */ ;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "container" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ class: "error-icon" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.svg, __VLS_intrinsicElements.svg)({
    xmlns: "http://www.w3.org/2000/svg",
    viewBox: "0 0 24 24",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.circle)({
    cx: "12",
    cy: "12",
    r: "10",
    fill: "#ff4d4f",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.line)({
    x1: "12",
    y1: "7",
    x2: "12",
    y2: "13",
    stroke: "#fff",
    'stroke-width': "2",
    'stroke-linecap': "round",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.circle)({
    cx: "12",
    cy: "17",
    r: "1.2",
    fill: "#fff",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.h2, __VLS_intrinsicElements.h2)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.p, __VLS_intrinsicElements.p)({
    ...{ class: "error-text" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.br)({});
__VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
    ...{ onClick: (__VLS_ctx.goToBot) },
});
/** @type {__VLS_StyleScopedClasses['container']} */ ;
/** @type {__VLS_StyleScopedClasses['error-icon']} */ ;
/** @type {__VLS_StyleScopedClasses['error-text']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            goToBot: goToBot,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
