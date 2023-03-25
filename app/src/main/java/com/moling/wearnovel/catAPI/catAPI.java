package com.moling.wearnovel.catAPI;

import static com.moling.wearnovel.utils.unmarshal.unmarshal.*;

import com.moling.wearnovel.catAPI.book.api;
import com.moling.wearnovel.catAPI.bookshelf.bookshelf_cat;
import com.moling.wearnovel.utils.templates.bookshelfIndex.bookshelfIndex;
import com.moling.wearnovel.utils.templates.bookshelf.bookshelf;

import java.util.ArrayList;
import java.util.Objects;

public class catAPI {
    public static String GET_BOOK_SHELF_INFORMATION() {
        return bookshelf_cat.GET_BOOK_SHELF_INFORMATION();
    }
    public static String GET_BOOK_SHELF_INDEXES_INFORMATION(String shelf_id) {
        return bookshelf_cat.GET_BOOK_SHELF_INDEXES_INFORMATION(shelf_id);
    }
    public static String GET_BOOK_INFORMATION(String bid) {
        return api.GET_BOOK_INFORMATION(bid);
    }
    public static String GET_DIVISION_LIST_BY_BOOKID(String bid) {
        return api.GET_DIVISION_LIST_BY_BOOKID(bid);
    }
    public static String GET_KET_BY_CHAPTER_ID(String chapter_id) {
        return api.GET_KET_BY_CHAPTER_ID(chapter_id);
    }
    public static String GET_CHAPTER_CONTENT(String chapter_id, String chapter_key) {
        return api.GET_CHAPTER_CONTENT(chapter_id, chapter_key);
    }

    public static bookshelfIndex GetBooksOnBookshelf() {
        String bookshelf_information = GET_BOOK_SHELF_INFORMATION();
        if (Objects.equals(bookshelf_information, null)) {
            return null;
        }
        bookshelf book_shelf = UnmarshalBookshelf(bookshelf_information);
        bookshelfIndex return_index = new bookshelfIndex(
                0,
                new ArrayList<>()
        );
        for (int i = 0; i < (long) book_shelf.getData().size(); i++) {
            bookshelfIndex book_shelf_index = UnmarshalBookshelfIndex(GET_BOOK_SHELF_INDEXES_INFORMATION(book_shelf.getData().get(i).getShelf_id()));
            if(return_index.getCode() == 0) {
                return_index = book_shelf_index;
            } else {
                for (int j = 0; j < (long) book_shelf_index.getData().size(); j++) {
                    return_index.getData().add(book_shelf_index.getData().get(j));
                }
            }
        }
        return return_index;
    }
}
