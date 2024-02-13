package com.redhat.training.books;

// BookNotAvailableException 子类继承 Exception 超类 
public class BookNotAvailableException extends Exception {

    private static final long serialVersionUID = -5692072907291015767L;

    public BookNotAvailableException(String isbn) {
        super(String.format("Book %s is not available", isbn));
        // super 关键字调用超类中的方法
    }

}
