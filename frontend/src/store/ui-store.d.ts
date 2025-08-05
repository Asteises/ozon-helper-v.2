interface UiState {
    isProfileDropdownOpen: boolean;
    isNavVisible: boolean;
}
export declare const useUiStore: import("pinia").StoreDefinition<"ui", UiState, {}, {
    toggleProfileDropdown(): void;
    closeProfileDropdown(): void;
    openProfileDropdown(): void;
    setNavVisible(visible: boolean): void;
}>;
export {};
