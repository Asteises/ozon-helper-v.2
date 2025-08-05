import { defineStore } from 'pinia'

interface UiState {
    isProfileDropdownOpen: boolean
    isBurgerMenuOpen: boolean
    isNavVisible: boolean
}

export const useUiStore = defineStore('ui', {
    state: (): UiState => ({
        isProfileDropdownOpen: false,
        isBurgerMenuOpen: false,
        isNavVisible: true
    }),
    actions: {
        // Профиль
        toggleProfileDropdown(): void {
            this.isProfileDropdownOpen = !this.isProfileDropdownOpen
        },
        closeProfileDropdown(): void {
            this.isProfileDropdownOpen = false
        },
        openProfileDropdown(): void {
            this.isProfileDropdownOpen = true
        },

        // Бургер
        toggleBurgerMenu(): void {
            this.isBurgerMenuOpen = !this.isBurgerMenuOpen
        },
        closeBurgerMenu(): void {
            this.isBurgerMenuOpen = false
        },
        openBurgerMenu(): void {
            this.isBurgerMenuOpen = true
        },

        // Навбар
        setNavVisible(visible: boolean): void {
            this.isNavVisible = visible
        }
    }
})