class AddBookRequest {

    title: string;
    author: string;
    description: string;
    copies: number;
    category: string;
    img?: string;

    constructor(title: string, author: string, description: string, copies: number, catergory: string, img?: string) {
        this.title = title;
        this.category = catergory;
        this.description = description;
        this.copies = copies;
        this.img = img;
        this.author = author;
    }
}

export default AddBookRequest;