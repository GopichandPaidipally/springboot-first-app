package com.springboot.springbootproductapp.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.springbootproductapp.model.ApprovalQueue;
import com.springboot.springbootproductapp.model.ProductDetails;
import com.springboot.springbootproductapp.repository.ApprovalQueueRespository;
import com.springboot.springbootproductapp.repository.ProductRepository;

@Service
public class ProductAppService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ApprovalQueueRespository approvalQueueRespository;

	public String createProduct(ProductDetails product) {
		double price = product.getPrice();
		String response;

		if (price > 5000 && price <= 10000) {

			ApprovalQueue approvalQueue = new ApprovalQueue();

			approvalQueue.setProduct_id(product.getProductId());
			approvalQueue.setName(product.getName());
			approvalQueue.setCreated_at(new Date());
			approvalQueue.setStatus("pending");
			approvalQueue.setPrice(product.getPrice());
			productRepository.save(approvalQueue);

			response = "created record in approval queue because price is more than 5000";

		} else if (price <= 5000) {

			product.setCreated_at(new Date());
			productRepository.save(product);
			response = "created record in Product because price is less than 5000";

		} else {
			// If price exceeds $10000, reject the creation
			// throw new IllegalArgumentException("Price should not exceed $10000.");
			response = "Price should not exceed $10000";
		}

		return response;
	}

	public String updateProduct(long productId, ProductDetails product_details) {
		String response = null;
		Optional<ProductDetails> existingProduct = productRepository.findById(productId);

		if (!existingProduct.isEmpty()) {

			double previousPrice = existingProduct.get().getPrice();
			double newPrice = product_details.getPrice();

			if (newPrice > 0.5 * previousPrice) {
				// If the new price is more than 50% of the previous price, set the product to
				// the approval queue
				ApprovalQueue approvalQueue = new ApprovalQueue();

				approvalQueue.setProduct_id(productId);
				approvalQueue.setName(product_details.getName());
				approvalQueue.setCreated_at(new Date());
				approvalQueue.setStatus("pending");
				approvalQueue.setPrice(product_details.getPrice());
				productRepository.save(approvalQueue);
				response = "Pushed Product to Approval queue";

			} else {

				// If the new price doesn't exceed 50% of the previous price, update the product
				existingProduct.get().setName(product_details.getName());
				existingProduct.get().setStatus(product_details.getStatus());
				existingProduct.get().setPrice(product_details.getPrice());
				productRepository.save(existingProduct);
				response = "product updated successfully";
			}
		} else {
			response = "Product Not found";
		}
		return response;
	}

	public String deleteProduct(long productId) {
		Optional<ProductDetails> existingProduct = productRepository.findById(productId);
		String response = null;
		ApprovalQueue approvalQueue = new ApprovalQueue();

		if (!existingProduct.isEmpty()) {
			approvalQueue.setProduct_id(productId);
			approvalQueue.setName(existingProduct.get().getName());
			approvalQueue.setCreated_at(new Date());
			approvalQueue.setStatus("pending");
			approvalQueue.setPrice(existingProduct.get().getPrice());

			productRepository.save(approvalQueue);
			productRepository.deleteById(productId);
			response = "Product deleted successfully";

		} else {

			response= "Product Not found";
		}
		return response;
		
	}

	public String approveApproval(Long approvalId) {

		ApprovalQueue existingApproval = approvalQueueRespository.findById(approvalId)
				.orElseThrow(() -> new IllegalArgumentException("Approval not found"));

		existingApproval.setStatus("approved");
		approvalQueueRespository.save(existingApproval);
		
		return "Approved successfully";

	}

	public String rejectApproval(Long approvalId) {

		String response = "";
		ApprovalQueue existingApproval = approvalQueueRespository.findById(approvalId)
				.orElseThrow(() -> new IllegalArgumentException("Approval not found"));

			existingApproval.setStatus("rejected");
			approvalQueueRespository.save(existingApproval);
		
		return "Rejected successfully";
	}

}
