package com.exileaccused.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "exile_record")
public class ExileRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String srNo;
    private String policeStation;
    private String nameOfAccused;
    private String address;
    private String mcoaSection;
    private String periodOfExile;
    private String dateOfExileOrder;
    private String dateOfExecutionOfExileOrder;
    private String endDateOfExilePeriod;
    private String mobileNumber;
 
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;  // 🆕 store image as bytes
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exile_data_id")
    @JsonBackReference
    private ExileData exileData;
    
	public ExileRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSrNo() {
		return srNo;
	}
	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
	public String getPoliceStation() {
		return policeStation;
	}
	public void setPoliceStation(String policeStation) {
		this.policeStation = policeStation;
	}
	public String getNameOfAccused() {
		return nameOfAccused;
	}
	public void setNameOfAccused(String nameOfAccused) {
		this.nameOfAccused = nameOfAccused;
	}
	public String getMcoaSection() {
		return mcoaSection;
	}
	public void setMcoaSection(String mcoaSection) {
		this.mcoaSection = mcoaSection;
	}
	public String getPeriodOfExile() {
		return periodOfExile;
	}
	public void setPeriodOfExile(String periodOfExile) {
		this.periodOfExile = periodOfExile;
	}
	public String getDateOfExileOrder() {
		return dateOfExileOrder;
	}
	public void setDateOfExileOrder(String dateOfExileOrder) {
		this.dateOfExileOrder = dateOfExileOrder;
	}
	public String getDateOfExecutionOfExileOrder() {
		return dateOfExecutionOfExileOrder;
	}
	public void setDateOfExecutionOfExileOrder(String dateOfExecutionOfExileOrder) {
		this.dateOfExecutionOfExileOrder = dateOfExecutionOfExileOrder;
	}
	public String getEndDateOfExilePeriod() {
		return endDateOfExilePeriod;
	}
	public void setEndDateOfExilePeriod(String endDateOfExilePeriod) {
		this.endDateOfExilePeriod = endDateOfExilePeriod;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
 
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ExileData getExileData() {
		return exileData;
	}
	public void setExileData(ExileData exileData) {
		this.exileData = exileData;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
    
    
    
    
    
}

