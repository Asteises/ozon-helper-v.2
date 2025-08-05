import { defineStore } from 'pinia'

interface UiState {
    isProfileDropdownOpen: boolean
    isNavVisible: boolean
}

export const useUiStore = defineStore('ui', {
    state: (): UiState => ({
        isProfileDropdownOpen: false,
        isNavVisible: true
    }),
    actions: {
        toggleProfileDropdown(): void {
            this.isProfileDropdownOpen = !this.isProfileDropdownOpen
        },
        closeProfileDropdown(): void {
            this.isProfileDropdownOpen = false
        },
        openProfileDropdown(): void {
            this.isProfileDropdownOpen = true
        },
        setNavVisible(visible: boolean): void {
            this.isNavVisible = visible
        }
    }
})