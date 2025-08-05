export declare const useUserStore: import("pinia").StoreDefinition<"user", {
    clientId: string;
    apiKey: string;
    telegramUser: any | null;
}, {}, {
    setCredentials(clientId: string, apiKey: string): void;
    setTelegramUser(user: any): void;
    clearUser(): void;
}>;
