package com.redhat.shopping.cart;

import com.redhat.shopping.catalog.Catalog;
import com.redhat.shopping.catalog.Product;
import com.redhat.shopping.catalog.ProductNotFoundInCatalogException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CartService {

    private final Map<Integer, CartItem> products = new HashMap<>();
    private int totalItems = 0;

    @Inject
    Catalog catalog;

    private void recalculate() {
        this.totalItems = 0;

        this.products.forEach((id, cartItem) -> {
            this.totalItems += cartItem.getQuantity();
        });
    }

    // 向购物车中添加商品
    public void addProduct(int productId, int quantity) throws ProductNotFoundInCatalogException {
        Product product = this.catalog.ofId(productId);

        if (!this.products.containsKey(productId)) {
            this.products.put(product.id(), new CartItem(product.id(), product.price()));
        }

        this.products.get(product.id()).increaseQuantityBy(quantity);
        // 统计单项产品在购物车中的数量

        this.recalculate();
        // recalculate 方法根据各项产品的数量统计购物车中的产品总数
    }

    public void clear() {
        this.products.clear();
        this.recalculate();
    }

    public CartView cartContent() {
        return new CartView(this.products.values(), this.totalItems());
    }

    public void removeProduct(int productId) throws ProductNotFoundInCatalogException, ProductNotInCartException {
        Product product = this.catalog.ofId(productId);

        if (!this.products.containsKey(product.id())) {
            throw ProductNotInCartException.ofId(product.id());
        }

        this.products.remove(product.id());

        this.recalculate();
    }

    public int totalItems() {
        return this.totalItems;
    }

    public int totalProducts() {
        return this.products.size();
    }
}
