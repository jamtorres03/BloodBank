package com.bloodbank.model;

import java.util.Date;

public class RequestModel {

	private int requestNo;
	private String uuid;
	private String bloodType;
	private String location;
	private double longitude;
	private double latitude;
	private int status;
	private UserModel requestedBy;
	private UserModel acceptedBy;
	private Date requestDate;
	private Date acceptDate;
	
	public int getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(int requestNo) {
		this.requestNo = requestNo;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public UserModel getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(UserModel requestedBy) {
		this.requestedBy = requestedBy;
	}
	public UserModel getAcceptedBy() {
		return acceptedBy;
	}
	public void setAcceptedBy(UserModel acceptedBy) {
		this.acceptedBy = acceptedBy;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public Date getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
}
