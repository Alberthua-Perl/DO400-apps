package com.redhat.training.inventory;

import com.redhat.training.books.Book;

// Inventory 接口的定义
public interface Inventory {
    public void add(Book book);

    public int count();

    public boolean isBookAvailable(String isbn);

    public Book withdraw(String isbn);
}
