package com.springboot.springbootproductapp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.springbootproductapp.model.ApprovalQueue;
import com.springboot.springbootproductapp.model.ProductDetails;

public interface ProductRepository extends JpaRepository<ProductDetails, Long> {

    @Query("SELECT p FROM ProductDetails p WHERE p.status = 'active' ORDER BY p.created_at DESC")
	List<ProductDetails> findBystatusOrderBycreated_atDesc();

    @Query("SELECT p FROM ProductDetails p " +
            "WHERE (:productName IS NULL OR p.name LIKE %:productName%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:startDate IS NULL OR date(p.created_at) >= :startDate) " +
            "AND (:endDate IS NULL OR date(p.created_at) <= :endDate)")
    List<ProductDetails> findByProductNameContainingAndPriceBetweenAndPostedDateBetween(
            String productName,
            double minPrice,
            double maxPrice,
            Date startDate,
            Date endDate
    );

	void save(ApprovalQueue aq);
	
	@Query("SELECT a FROM ApprovalQueue a where a.status ='pending' ORDER BY a.created_at DESC")
	List<ApprovalQueue> getProductsInApprovalQueue();

	void save(Optional<ProductDetails> existingProduct);


}
