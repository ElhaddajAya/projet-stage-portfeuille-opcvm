package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "societes_gestion")
public class SocieteGestion extends Societe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "site_web")
	private String siteWeb;

	public SocieteGestion() {
		super();
	}

	public SocieteGestion(String nom, String email, String telephone, String adressePostale, String siteWeb) {
		super(nom, email, telephone, adressePostale);
		this.siteWeb = siteWeb;
	}

	public SocieteGestion(Long id, String nom, String email, String telephone, String adressePostale, String siteWeb) {
		super(nom, email, telephone, adressePostale);
		this.id = id;
		this.siteWeb = siteWeb;
	}

}
