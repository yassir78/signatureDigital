package com.vote.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

@Entity
public class Votant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private String prenom;
	private int age;
	private String email;
	@Column( length = 100000 )
	private byte[] publicKey;
	@ColumnDefault("false")
	private boolean isVoter;

	public Votant(Long id, String nom, String prenom, int age, String email, byte[] publicKey, boolean isVoter) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.email = email;
		this.publicKey = publicKey;
		this.isVoter = isVoter;
	}

	public Votant(String nom, String prenom, int age, String email, byte[] publicKey) {
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.email = email;
		this.publicKey = publicKey;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	public Votant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isVoter() {
		return isVoter;
	}

	public void setVoter(boolean isVoter) {
		this.isVoter = isVoter;
	}

	public Votant(String nom, String prenom, int age, String email) {
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
