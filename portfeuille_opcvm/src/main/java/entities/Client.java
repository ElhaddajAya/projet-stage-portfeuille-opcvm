package entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "clients")
public class Client extends Societe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "forme_juridique")
	private String formeJuridique;

	@Column(name = "activite_principale")
	private String activitePrincipale;

	@Column(name = "estPhysique", nullable = false)
	private boolean estPhysique;

	@OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	public List<Transaction> transactions;

    @Transient
    private int totalParts;  // total de parts d'un client dans un portefeuille

    @Transient
    private double totalAmount;  // total montant -- -- -- -- -- 
	
    // "Personne" ou "Entreprise" => estPhysique?
    public String getNature() {
        return estPhysique ? "Entreprise" : "Personne";
    }
    
	public Client() {
		super();
	}

	public Client(String nom, String email, String telephone, String adressePostale, String formeJuridique,
			String activitePrincipale, boolean estPhysique) {
		super(nom, email, telephone, adressePostale);
		this.formeJuridique = formeJuridique;
		this.activitePrincipale = activitePrincipale;
		this.estPhysique = estPhysique;
	}

	public Client(Long id, String nom, String email, String telephone, String adressePostale, String formeJuridique,
			String activitePrincipale, boolean estPhysique) {
		super(nom, email, telephone, adressePostale);
		this.id = id;
		this.formeJuridique = formeJuridique;
		this.activitePrincipale = activitePrincipale;
		this.estPhysique = estPhysique;
	}

	@Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", formeJuridique='" + formeJuridique + '\'' +
                ", activitePrincipale='" + activitePrincipale + '\'' +
                ", estPhysique=" + estPhysique +
                '}';
    }
	
}