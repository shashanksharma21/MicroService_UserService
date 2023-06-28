package com.user.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private String productId;
	private String productName;
	private String productDescription;
	private String productCompanyName;
	private Double productPrice;
	private Integer stockQuantity;
	private String userId;
	
}
