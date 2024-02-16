package com.redhat.shopping.integration.whitebox;

import com.redhat.shopping.cart.CartService;
import com.redhat.shopping.catalog.ProductNotFoundInCatalogException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest  // Quarkus 框架中的集成测试注解
public class ShoppingCartTest {

    @Inject
    CartService cartService;

    @BeforeEach
    void clearCart() {
        this.cartService.clear();
        // 清空购物车
        // 注意：this 关键字对对象的引用与 new 操作符声明与初始化新对象的区别
    }
    // @todo: add integration tests

	@Test
    // 测试场景1：添加一个在商品目录中不存在的商品至购物车中
	void addingNonExistingProductInCatalogRaisesAnException() {
    	assertThrows(
        	ProductNotFoundInCatalogException.class,
        	() -> this.cartService.addProduct(9999, 10)
    	);
	}

    @Test
    // 测试场景2：添加一个新商品至空的购物车中
    void addingNonExistingProductInCartTheTotalItemsMatchTheInitialQuantity()
            throws ProductNotFoundInCatalogException {
        this.cartService.addProduct(1, 10);
        assertEquals(10, this.cartService.totalItems());
    }

    @Test
    // 测试场景3：向购物车中添加更多的已在购物车中的商品
    void addingProductThatIsInTheCartTheTotalItemsMatchTheSumOfQuantities() 
            throws ProductNotFoundInCatalogException {
        
        this.cartService.addProduct(1, 10);
        this.cartService.addProduct(1, 100);

        assertEquals(110, this.cartService.totalItems());
    }
}
