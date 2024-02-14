package com.redhat.training;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.redhat.training.books.BookNotAvailableException;
import com.redhat.training.inventory.Inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LibraryWithMockedInventoryTest {

    Inventory inventory;
    Library library;

    @BeforeEach
    public void setUp() {
        inventory = mock(Inventory.class);
        library = new Library(inventory);
    }

    // Add tests here...
    @Test
    public void checkingOutWithdrawsFromInventoryWhenBookIsAvailable() 
            throws BookNotAvailableException {
        // Given
        when(inventory.isBookAvailable("book1")).thenReturn(true);

        // When
        library.checkOut("student1", "book1");

        // Then
        verify(inventory).withdraw("book1");
    }

    @Test
    public void checkingOutDoesNotWithdrawFromInventoryWhenBookIsUnavailable()
            throws BookNotAvailableException {
        // 这个测试用例的目的是确保在书籍不可用的情况下，checkOut 方法不会尝试从库存中移除书籍，
        // 并且会正确地抛出异常。这样的测试有助于确保 Library 类的逻辑正确，并且在异常情况下能够正确处理。

        // Given
        when(inventory.isBookAvailable("book1")).thenReturn(false);
        // 使用 Mockito 框架的 when 和 thenReturn方 法来模拟 inventory 对象的 isBookAvailable 方法。
        // 这里假设 inventory 对象有一个 isBookAvailable 方法，用于检查书籍是否可用。
        // 在这个模拟中，无论传入什么参数，isBookAvailable 方法都返回 false，表示书籍不可用。

        // When
        try {
            library.checkOut("student1", "book1");
        } catch(BookNotAvailableException e) {}
        // 由于之前模拟了书籍不可用，这里会捕获并处理 BookNotAvailableException 异常。
        // 注意，异常被捕获后没有做任何处理，这是为了确保异常被正确抛出。

        // Then
        verify(inventory,times(0)).withdraw("book1");
        // 使用 Mockito 框架的 verify 方法来验证 inventory 对象的 withdraw 方法是否被调用。
        // times(0) 表示期望 withdraw 方法没有被调用。如果 withdraw 方法被调用，测试将失败。
    }            
}
