package com.exileaccused.entity;

 

 
 
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
 

@Entity
@Table(name = "exile_data")
 
public class ExileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadedBy;
    
    
    @OneToMany(mappedBy = "exileData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExileRecord> records;
    

	public ExileData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

 

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public List<ExileRecord> getRecords() {
		return records;
	}

	public void setRecords(List<ExileRecord> records) {
		this.records = records;
	}
    
    
    
}

