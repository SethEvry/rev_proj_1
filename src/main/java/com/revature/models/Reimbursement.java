package com.revature.models;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class Reimbursement {

	private int id;
	private double amount;
	private Timestamp submitted;
	private Timestamp resolved;
	private String description;
	private int authorId;
	private int resolverId;
	private int statusId;
	private int typeId;

	public Reimbursement() {
		super();
		this.submitted = Timestamp.from(Instant.now());
		this.statusId = 1;
		// TODO Auto-generated constructor stub
	}
	public Reimbursement(Reimbursement re) {
		super();
		this.id = re.getId();
		this.amount = re.getAmount();
		this.submitted = re.getSubmitted();
		this.resolved = re.getResolved();
		this.description = re.getDescription();
		this.authorId = re.getAuthorId();
		this.resolverId = re.getResolverId();
		this.statusId = re.getStatusId();
		this.typeId = re.getTypeId();
	}

	public Reimbursement(int id, double amount, Timestamp submitted, Timestamp resolved, String description,
			int authorId, int resolverId, int statusId, int typeId) {
		super();
		this.id = id;
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.statusId = statusId;
		this.typeId = typeId;
	}

	public Reimbursement(double amount, Timestamp submitted, Timestamp resolved, String description, int authorId,
			int resolverId, int statusId, int typeId) {
		super();
		this.amount = amount;
		this.submitted = submitted;
		this.resolved = resolved;
		this.description = description;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.statusId = statusId;
		this.typeId = typeId;
	}

	public Reimbursement(double amount, String description, int typeId) {
		super();
		this.amount = amount;
		this.submitted = Timestamp.from(Instant.now());
		this.description = description;
		this.statusId = 1;
		this.typeId = typeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Timestamp submitted) {
		this.submitted = submitted;
	}

	public Timestamp getResolved() {
		return resolved;
	}

	public void setResolved(Timestamp resolved) {
		this.resolved = resolved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getResolverId() {
		return resolverId;
	}

	public void setResolverId(int resolverId) {
		this.resolverId = resolverId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, authorId, description, id, resolved, resolverId, statusId, submitted, typeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount) && authorId == other.authorId
				&& Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(resolved, other.resolved) && resolverId == other.resolverId
				&& statusId == other.statusId && Objects.equals(submitted, other.submitted) && typeId == other.typeId;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submitted=" + submitted + ", resolved=" + resolved
				+ ", description=" + description + ", authorId=" + authorId + ", resolverId=" + resolverId
				+ ", statusId=" + statusId + ", typeId=" + typeId + "]";
	}

}
