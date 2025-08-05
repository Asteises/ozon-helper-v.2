import { defineStore } from 'pinia';
export const useUiStore = defineStore('ui', {
    state: () => ({
        isProfileDropdownOpen: false,
        isNavVisible: true
    }),
    actions: {
        toggleProfileDropdown() {
            this.isProfileDropdownOpen = !this.isProfileDropdownOpen;
        },
        closeProfileDropdown() {
            this.isProfileDropdownOpen = false;
        },
        openProfileDropdown() {
            this.isProfileDropdownOpen = true;
        },
        setNavVisible(visible) {
            this.isNavVisible = visible;
        }
    }
});
