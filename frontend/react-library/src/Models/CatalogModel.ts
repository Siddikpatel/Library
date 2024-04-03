import BookModel from "./BookModel";

export default class CatalogModel<T> {

    items: T[];
    totalItems: number;
    totalPages: number;

    constructor(list: T[], size: number, pages: number) {
        this.items = list;
        this.totalItems = size;
        this.totalPages = pages;
    }
}