class MessageModel {
    
    title: string;
    question: string;
    id?: number;
    userEmail?: string;
    adminEmail?: string;
    closed?: boolean;
    response?: string;

    constructor(title: string, question: string, id?: number, userEmail?: string, adminEmail?: string, closed?: boolean, response?: string) {
        this.title = title;
        this.question = question;
        this.id = id;
        this.userEmail = userEmail;
        this.adminEmail = adminEmail;
        this.closed = closed;
        this.response = response;
    }
}

export default MessageModel;