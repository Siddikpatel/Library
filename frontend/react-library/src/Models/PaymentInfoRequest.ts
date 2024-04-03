export default class PaymentInfoRequest {

    amount: number;
    receiptEmail: string | undefined;
    currency: string;

    constructor(amount: number, currency: string, receiptEmail: string | undefined) {
        this.amount = amount;
        this.currency = currency;
        this.receiptEmail = receiptEmail;
    }
}