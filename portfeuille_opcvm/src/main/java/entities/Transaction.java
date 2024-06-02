package entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date date_transaction;

	@Column(nullable = false)
	private String sens; 
	
	@Column(nullable = false)
	private double montant;

	@Column(name = "nbr_part", nullable = false)
	private int nbrPart;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne
	@JoinColumn(name = "portefeuille_id")
	private Portefeuille portefeuille;

	public Transaction() {
		super();
	}

	public Transaction(Long id, Date date_transaction, String sens, double montant, Client client,
			Portefeuille portefeuille, int nbrPart) {
		super();
		this.id = id;
		this.date_transaction = date_transaction;
		this.sens = sens;
		this.montant = montant;
		this.client = client;
		this.portefeuille = portefeuille;
		this.nbrPart = nbrPart;
	}

	public Transaction(Date date_transaction, String sens, double montant, Client client,
			Portefeuille portefeuille, int nbrPart) {
		super();
		this.date_transaction = date_transaction;
		this.sens = sens;
		this.montant = montant;
		this.client = client;
		this.portefeuille = portefeuille;
		this.nbrPart = nbrPart;
	}
	

	@Override
	public String toString() {
		return "Transaction{" + "id=" + id + ", date_transaction=" + date_transaction + ", sens='" + sens + '\''
				+ ", montant=" + montant + ", nbrPart=" + nbrPart + '\''
				+ '}';
	}
	
}