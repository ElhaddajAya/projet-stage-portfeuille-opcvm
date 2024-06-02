package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data

@Entity
@Table(name = "portefeuilles")
public class Portefeuille {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String libelle;

	@Column(name = "nbr_part", nullable = false)
	private int nbrPart; // Le nombre total de parts dans le portefeuille

	@OneToOne
	@JoinColumn(name = "societe_gestion_id")
	private SocieteGestion societeGestion;

	@Column(name = "type_opcvm", nullable = false)
	private String typeOpcvm;

	@Column(nullable = false)
	private String objectif;

	@Column(nullable = false)
	private String horizon;

	@Column(nullable = false)
	private String devise;
	
	@Column(name = "profile_risque", nullable = false)
	private String profileRisque;

	@Column(name = "type_invest", nullable = false)
	private String typeInvest;

	@OneToMany(mappedBy = "portefeuille", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<Transaction> transactions;
	
    @OneToMany(mappedBy = "portefeuille", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Cours> cours;
	
	public Portefeuille() {
		super();
		this.transactions = new ArrayList<>();
	    this.cours = new ArrayList<>();
	}

	public Portefeuille(Long id, String libelle, int nbrPart, SocieteGestion societeGestion, String typeOpcvm,
			String objectif, String horizon, String profileRisque, String typeInvest, String devise) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.nbrPart = nbrPart;
		this.societeGestion = societeGestion;
		this.typeOpcvm = typeOpcvm;
		this.objectif = objectif;
		this.horizon = horizon;
		this.profileRisque = profileRisque;
		this.typeInvest = typeInvest;
		this.devise = devise;
	}

	public Portefeuille(String libelle, int nbrPart, SocieteGestion societeGestion, String typeOpcvm, String objectif,
			String horizon, String profileRisque, String typeInvest, String devise) {
		super();
		this.libelle = libelle;
		this.nbrPart = nbrPart;
		this.societeGestion = societeGestion;
		this.typeOpcvm = typeOpcvm;
		this.objectif = objectif;
		this.horizon = horizon;
		this.profileRisque = profileRisque;
		this.typeInvest = typeInvest;
		this.devise = devise;
	}

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}