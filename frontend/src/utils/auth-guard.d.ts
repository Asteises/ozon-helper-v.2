declare global {
    interface Window {
        Telegram: any;
    }
}
export declare function verifyAndCheckUser(): Promise<'ok' | 'unauthorized' | 'not_registered'>;
