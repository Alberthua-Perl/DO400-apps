package com.redhat.shopping.integration.blackbox;

import com.redhat.shopping.cart.AddToCartCommand;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class ShoppingCartTest {

    private int randomQuantity() {
        return (new Random()).nextInt(10) + 1;
    }

    private void addProductToTheCartWithIdAndRandomQuantity(int productId) {
        AddToCartCommand productToAdd = new AddToCartCommand(
            productId,
            this.randomQuantity()
        );

        given()
            .contentType("application/json")
            .body(productToAdd)
            .put("/cart");
    }

    @BeforeEach
    public void clearCart() {
        delete("/cart");
    }

    // @todo: add integration tests

    @Test
    // 测试场景1：从购物车中删除商品列表中不存在的商品
    // 使用 RestAssured 库编写的 JUnit 测试用例，旨在验证当尝试从购物车中删除一个不存在的商品时，
    // API 应该返回 HTTP 状态码 400（Bad Request）。
    // 这个测试用例通常用于 RESTful API 的集成测试，以确保 API 的行为符合预期。
    public void RemovingNonExistingProductInCatalogReturns400() {
        // 此方法没有参数，它使用了 RestAssured 的链式调用来构建 HTTP 请求。
        given()  // RestAssured 中的方法，用于设置测试的前置条件。此例中，没有设置任何条件，只是作为一个起点。
            .pathParam("id", 9999)  // 测试将尝试删除 ID 为 9999 的商品
        .when()  // RestAssured 中的方法，用于指定要执行的 HTTP 操作。此例中，指定了要执行 DELETE 请求。
            .delete("/cart/products/{id}")  // 构建并执行 DELETE 请求
        .then()
            .statusCode(400);  // 验证 HTTP 响应的状态码是否为 400。如果响应码是 400，测试将通过，反之为失败。
    }

    @Test
    // 测试场景2：商品还没有在购物车中将其从购物车中移除
    public void removingNonAddedProductToTheCartReturns404() {
        given()
            .pathParam("id", 1)
        .when()
            .delete("/cart/products/{id}")
        .then()
            .statusCode(404);
    }

    @Test
    // 测试场景3：商品还没有在购物车中将其从购物车中移除
    public void removingTheOnlyProductInCartReturns204() {
        this.addProductToTheCartWithIdAndRandomQuantity(1);

        given()
            .pathParam("id", 1)
        .when()
            .delete("/cart/products/{id}")
        .then()
            .statusCode(204);
    }

    @Test
    // 测试场景4：当购物车中不仅仅存在一件商品时并将其删除
    public void
     removingProductFromCartContainingMultipleAndDifferentProductsReturns200() {
        this.addProductToTheCartWithIdAndRandomQuantity(1);
        this.addProductToTheCartWithIdAndRandomQuantity(2);

        given()
            .pathParam("id", 1)
        .when()
            .delete("/cart/products/{id}")
        .then()
            .statusCode(200);
    }
}