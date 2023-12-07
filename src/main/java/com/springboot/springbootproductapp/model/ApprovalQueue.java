package com.springboot.springbootproductapp.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table
public class ApprovalQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long approvalId;
	@Column
	private String name;
	@Column
	private double price;

	@Column
	private String status;

	@Column
	private long product_id;

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = true)
	private Date created_at;

	public ApprovalQueue() {

	}

	public ApprovalQueue(String name, double price, String status,long product_id) {
		super();
		this.name = name;
		this.price = price;
		this.status = status;
		this.product_id=product_id;
	}

	public long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(long approvalId) {
		this.approvalId = approvalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

}
