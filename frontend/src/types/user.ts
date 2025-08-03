export interface OzonDataForm {
    clientId: string;
    apiKey: string;
}

export interface RegisterUserData {
    role?: string; // Enum на бэке (задаётся сервером)
    status?: string; // Enum на бэке (задаётся сервером)
    telegramUserId: number;
    username?: string;
    firstName?: string;
    lastName?: string;
    registeredAt?: string; // ISO строка
    ozonDataForm: OzonDataForm;
}