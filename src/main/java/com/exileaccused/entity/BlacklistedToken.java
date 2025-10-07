package com.exileaccused.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 512, nullable = false)
    private String token;
    private Date blacklistedAt = new Date();

    public BlacklistedToken() {}
    public BlacklistedToken(String token) { this.token = token; }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getBlacklistedAt() {
		return blacklistedAt;
	}
	public void setBlacklistedAt(Date blacklistedAt) {
		this.blacklistedAt = blacklistedAt;
	}

    
}