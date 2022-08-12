package com.bittsoftware.dscatalog.tests;

import java.time.Instant;

import com.bittsoftware.dscatalog.dto.ProductDTO;
import com.bittsoftware.dscatalog.entities.Category;
import com.bittsoftware.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.00, "https://img.com/img.png",
				Instant.parse("2022-08-12T07:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletrônicos"));
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
}
