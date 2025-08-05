export interface OzonDataForm {
    clientId: string;
    apiKey: string;
}
export interface RegisterUserData {
    role?: string;
    status?: string;
    telegramUserId: number;
    username?: string;
    firstName?: string;
    lastName?: string;
    registeredAt?: string;
    ozonDataForm: OzonDataForm;
    telegramInitData: any;
}
export interface CheckUserData {
    telegramUserId: number;
    telegramInitData: any;
}
