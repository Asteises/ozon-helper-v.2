import { storeToRefs } from 'pinia';
import { useUiStore } from '../store/ui-store';
import { onMounted, onBeforeUnmount } from 'vue';
const uiStore = useUiStore();
const { isProfileDropdownOpen } = storeToRefs(uiStore);
function toggleDropdown() {
    uiStore.toggleProfileDropdown();
}
function handleClickOutside(event) {
    const dropdown = document.querySelector('.profile-container');
    if (dropdown && !dropdown.contains(event.target)) {
        uiStore.closeProfileDropdown();
    }
}
onMounted(() => {
    document.addEventListener('click', handleClickOutside);
});
onBeforeUnmount(() => {
    document.removeEventListener('click', handleClickOutside);
});
function logout() {
    uiStore.closeProfileDropdown();
}
debugger; /* PartiallyEnd: #3632/scriptSetup.vue */
const __VLS_ctx = {};
let __VLS_components;
let __VLS_directives;
/** @type {__VLS_StyleScopedClasses['menu-link']} */ ;
/** @type {__VLS_StyleScopedClasses['dropdown-item']} */ ;
// CSS variable injection 
// CSS variable injection end 
__VLS_asFunctionalElement(__VLS_intrinsicElements.header, __VLS_intrinsicElements.header)({
    ...{ class: "top-nav" },
});
const __VLS_0 = {}.RouterLink;
/** @type {[typeof __VLS_components.RouterLink, typeof __VLS_components.routerLink, typeof __VLS_components.RouterLink, typeof __VLS_components.routerLink, ]} */ ;
// @ts-ignore
const __VLS_1 = __VLS_asFunctionalComponent(__VLS_0, new __VLS_0({
    to: "/",
    ...{ class: "menu-link" },
}));
const __VLS_2 = __VLS_1({
    to: "/",
    ...{ class: "menu-link" },
}, ...__VLS_functionalComponentArgsRest(__VLS_1));
__VLS_3.slots.default;
var __VLS_3;
__VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
    ...{ onClick: (__VLS_ctx.toggleDropdown) },
    ...{ class: "profile-container" },
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.svg, __VLS_intrinsicElements.svg)({
    xmlns: "http://www.w3.org/2000/svg",
    ...{ class: "profile-icon" },
    viewBox: "0 0 24 24",
    fill: "none",
    stroke: "currentColor",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.circle)({
    cx: "12",
    cy: "7",
    r: "4",
});
__VLS_asFunctionalElement(__VLS_intrinsicElements.path)({
    d: "M5.5 21a8.38 8.38 0 0 1 13 0",
});
if (__VLS_ctx.isProfileDropdownOpen) {
    __VLS_asFunctionalElement(__VLS_intrinsicElements.div, __VLS_intrinsicElements.div)({
        ...{ class: "dropdown" },
    });
    const __VLS_4 = {}.RouterLink;
    /** @type {[typeof __VLS_components.RouterLink, typeof __VLS_components.routerLink, typeof __VLS_components.RouterLink, typeof __VLS_components.routerLink, ]} */ ;
    // @ts-ignore
    const __VLS_5 = __VLS_asFunctionalComponent(__VLS_4, new __VLS_4({
        to: "/profile",
        ...{ class: "dropdown-item" },
    }));
    const __VLS_6 = __VLS_5({
        to: "/profile",
        ...{ class: "dropdown-item" },
    }, ...__VLS_functionalComponentArgsRest(__VLS_5));
    __VLS_7.slots.default;
    var __VLS_7;
    __VLS_asFunctionalElement(__VLS_intrinsicElements.button, __VLS_intrinsicElements.button)({
        ...{ onClick: (__VLS_ctx.logout) },
        ...{ class: "dropdown-item" },
    });
}
/** @type {__VLS_StyleScopedClasses['top-nav']} */ ;
/** @type {__VLS_StyleScopedClasses['menu-link']} */ ;
/** @type {__VLS_StyleScopedClasses['profile-container']} */ ;
/** @type {__VLS_StyleScopedClasses['profile-icon']} */ ;
/** @type {__VLS_StyleScopedClasses['dropdown']} */ ;
/** @type {__VLS_StyleScopedClasses['dropdown-item']} */ ;
/** @type {__VLS_StyleScopedClasses['dropdown-item']} */ ;
var __VLS_dollars;
const __VLS_self = (await import('vue')).defineComponent({
    setup() {
        return {
            isProfileDropdownOpen: isProfileDropdownOpen,
            toggleDropdown: toggleDropdown,
            logout: logout,
        };
    },
});
export default (await import('vue')).defineComponent({
    setup() {
        return {};
    },
});
; /* PartiallyEnd: #4569/main.vue */
