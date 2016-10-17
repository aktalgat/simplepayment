package com.talgat.simplepayment.database;

import java.io.Serializable;

public class Payment implements Serializable {

	private long _id;
	private int type;
	private long category;
	private double sum;
	private String comment;
	private long pdate;
	
	public long getId() {
		return _id;
	}
	public void setId(long _id) {
		this._id = _id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getCategory() {
		return category;
	}
	public void setCategory(long category) {
		this.category = category;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getPdate() {
		return pdate;
	}
	public void setPdate(long pdate) {
		this.pdate = pdate;
	}
	
	@Override
	public String toString() {
		return "Payment [_id=" + _id + ", type=" + type + ", category="
				+ category + ", sum=" + sum + ", comment=" + comment
				+ ", pdate=" + pdate + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Payment p = (Payment) o;
		return this._id == p.getId();
	}

	@Override
	public int hashCode() {
		return (int)(this.getId()^(this.getId()>>>32));
	}
}
