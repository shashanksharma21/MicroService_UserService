package com.user.service.external.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.user.service.entities.Product;

@FeignClient(url = "http://localhost:8082/api/v1/products", name = "PRODUCT-SERVICE")
public interface ProductService {

	// GET PRODUCT BY USERID
	@GetMapping("/get/user/{userId}")
	public ResponseEntity<List<Product>> getProductByUserId(@PathVariable String userId);

}
