import BookModel from "./BookModel";

class ShelfCurrentLoans {

    book: BookModel;
    daysLeft: number;

    constructor(boook1: BookModel, days_left: number) {
        this.book = boook1;
        this.daysLeft = days_left;
    }
}

export default ShelfCurrentLoans;