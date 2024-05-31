package entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data

@MappedSuperclass
public abstract class Societe {

	@Column(nullable = false)
	private String nom;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String telephone;

	@Column(name = "adresse_postale", nullable = false)
	private String adressePostale;

	public Societe() {
		super();
	}


	public Societe(String nom, String email, String telephone, String adressePostale) {
		super();
		this.nom = nom;
		this.email = email;
		this.telephone = telephone;
		this.adressePostale = adressePostale;
	}



}
