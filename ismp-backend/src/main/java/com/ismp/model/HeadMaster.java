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
@Table(name = "head_master")
public class HeadMaster {

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

	@Column(nullable = false)
    private String name;

    @Column(name = "office_room", nullable = false)
    private String officeRoom;

    @ManyToOne
    @JoinColumn(name = "block_officer_id")
    private BlockEducationOfficer blockOfficer;

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

	public String getOfficeRoom() {
		return officeRoom;
	}

	public void setOfficeRoom(String officeRoom) {
		this.officeRoom = officeRoom;
	}

	public BlockEducationOfficer getBlockOfficer() {
		return blockOfficer;
	}

	public void setBlockOfficer(BlockEducationOfficer blockOfficer) {
		this.blockOfficer = blockOfficer;
	}
}
