class AdminMessageRequestModel {

    id: number;
    response: string;

    constructor(id: number, res: string) {
        this.id = id;
        this.response = res;
    }
}

export default AdminMessageRequestModel;