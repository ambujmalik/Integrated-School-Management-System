package com.ismp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "block_education_officer")
public class BlockEducationOfficer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "name")
    private String name;

    @Column(name = "office")
    private String office;

    @ManyToOne
    @JoinColumn(name = "district_officer_id")
    private DistrictOfficer districtOfficer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public DistrictOfficer getDistrictOfficer() {
		return districtOfficer;
	}

	public void setDistrictOfficer(DistrictOfficer districtOfficer) {
		this.districtOfficer = districtOfficer;
	}
}
