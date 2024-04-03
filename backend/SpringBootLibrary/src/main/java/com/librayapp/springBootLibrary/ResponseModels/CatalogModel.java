package com.librayapp.springBootLibrary.ResponseModels;

import com.librayapp.springBootLibrary.entity.Book;
import lombok.Data;

import java.util.List;

@Data
public class CatalogModel<T> {

    List<T> items;
    long totalItems;
    int totalPages;

    public CatalogModel(List<T> list, long items, int pages) {
        this.items = list;
        this.totalPages = pages;
        this.totalItems = items;
    }
}
