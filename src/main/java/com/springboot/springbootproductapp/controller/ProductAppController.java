package com.springboot.springbootproductapp.controller;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.springbootproductapp.model.ApprovalQueue;
import com.springboot.springbootproductapp.model.ProductDetails;
import com.springboot.springbootproductapp.repository.ProductRepository;
import com.springboot.springbootproductapp.service.ProductAppService;

@RestController
@RequestMapping("/api")
public class ProductAppController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductAppService productService;

	@GetMapping("/products")
	public ResponseEntity<List<ProductDetails>> getAllProducts() {
		List<ProductDetails> productsList = new ArrayList<>();
		productRepository.findBystatusOrderBycreated_atDesc().forEach(productsList::add);
		return new ResponseEntity<List<ProductDetails>>(productsList, HttpStatus.OK);

	}

	@GetMapping("/products/search")
	public ResponseEntity<List<ProductDetails>> searchProducts(@RequestParam(required = false) String productName,
			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		List<ProductDetails> productsList = new ArrayList<>();

		productRepository
				.findByProductNameContainingAndPriceBetweenAndPostedDateBetween(productName != null ? productName : "",
						minPrice != null ? minPrice : Double.MIN_VALUE, maxPrice != null ? maxPrice : Double.MAX_VALUE,
						startDate != null ? startDate : new Date(0), endDate != null ? endDate : new Date())
				.forEach(productsList::add);
		return new ResponseEntity<List<ProductDetails>>(productsList, HttpStatus.OK);
	}

	@PostMapping("/products")
	public String createNewProduct(@RequestBody ProductDetails product) {
		String createdProduct = productService.createProduct(product);
		return createdProduct;

	}

	@PutMapping("/products/{productId}")
	public String updateProductById(@PathVariable long productId,
			@RequestBody ProductDetails product_details) {
			String updated = productService.updateProduct(productId, product_details);
			return updated;
	}

	
	@DeleteMapping("/products/{productId}")
	public String deleteProductById(@PathVariable long productId) {
		 String response = productService.deleteProduct(productId);
		 return response;
	}

	@GetMapping("/products/approval-queue")
	public ResponseEntity<List<ApprovalQueue>> getApprovalQueueProducts() {
		List<ApprovalQueue> queueProductsList = new ArrayList<>();
		productRepository.getProductsInApprovalQueue().forEach(queueProductsList::add);
		return new ResponseEntity<List<ApprovalQueue>>(queueProductsList, HttpStatus.OK);

	}
	
	@PutMapping("/products/approval-queue/{approvalId}/approve")
	public String approveApproval(@PathVariable Long approvalId) {
         String response = productService.approveApproval(approvalId);
         return response;
    }
	
	@PutMapping("/products/approval-queue/{approvalId}/reject")
	public String rejectApproval(@PathVariable Long approvalId) {
         String response = productService.rejectApproval(approvalId);
         return response;
    }
	

}
