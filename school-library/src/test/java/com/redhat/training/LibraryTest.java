package com.redhat.training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redhat.training.books.Book;
import com.redhat.training.books.BookNotAvailableException;
import com.redhat.training.inventory.InMemoryInventory;


public class LibraryTest {

    InMemoryInventory inventory;  // inventory 接口声明
    Library library;

    @BeforeEach
    public void setUp() {
        inventory = new InMemoryInventory();
        library = new Library(inventory);
    }

    // Add tests here...
    @Test
    public void checkingOutDecreasesNumberOfBookCopiesFromInventory() 
            throws BookNotAvailableException {
    // 定义无返回值的函数在捕获异常时抛出错误
    // 
        // Given
        inventory.add(new Book("book1"));
        inventory.add(new Book("book1"));

        // When
        library.checkOut("someStudentID", "book1");

        // Then
        assertEquals(1, inventory.countCopies("book1"));

    }    

    @Test
    public void checkingOutUnavailableBookThrowsException() 
            throws BookNotAvailableException {
        // Given
        inventory.add(new Book("book1"));
        inventory.add(new Book("book1"));

        library.checkOut("student1", "book1");
        library.checkOut("student2", "book1");
        // 定义测试的条件：初始化并构建两个 Book 对象，并使用 checkOut 方法模拟 student1 与
        // student2 用户借阅 2 本 book1。

        // When
        final BookNotAvailableException exception = assertThrows(
            BookNotAvailableException.class,
            () -> {
                library.checkOut("student3", "book1");
            }
        );
        // 测试过程：当 student3 用户借阅 book1 时将报错无法借阅 book1

        // Then
        assertTrue(exception.getMessage().matches("Book book1 is not available"));
    }
}
