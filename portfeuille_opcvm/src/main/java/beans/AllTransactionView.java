package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import entities.Client;
import entities.Court;
import entities.Portefeuille;
import entities.Transaction;
import lombok.Data;
import services.PortefeuilleService;
import services.TransactionService;

@Data

@ManagedBean
@ViewScoped
public class AllTransactionView {

	@Inject
	private TransactionService trService;

	@Inject
	private PortefeuilleService ptfService;

	private Court dernierCout;
	
	private int nbrPart;
	private int valeur;
	private double montant;

	private List<Transaction> transactionList;
	private List<Transaction> filteredTransactions;

	/* Listes déroulantes */
	private List<Portefeuille> portefeuilleList;
	private List<Client> clientList;
	private List<String> deviseList;

	/* Valeurs séléctionnés */
	private Portefeuille selectedPortefeuille;
	private Client selectedClient;
	private String selectedSens;
	private String selectedDevise;

	@PostConstruct
	public void init() {
		portefeuilleList = new ArrayList<>();
		portefeuilleList = trService.getAllPortefeuilles();

		clientList = new ArrayList<>();
		clientList = trService.getAllClients();

		deviseList = new ArrayList<>();
		deviseList.add("MAD");
		deviseList.add("EUR");
		deviseList.add("USD");
		deviseList.add("CAD");
		deviseList.add("CHF");
		deviseList.add("GPB");
		deviseList.add("JPY");
		deviseList.add("AUD");
		deviseList.add("CNY");
		deviseList.add("INR");

		transactionList = new ArrayList<>();
		transactionList = trService.getAllTransactions();
	}

	/**** Bean methods ****/
	/* Methode pour filtrer les colonnes du tableau */
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.isEmpty()) {
			return true;
		}

		Transaction tr = (Transaction) value;
		return String.valueOf(tr.getId()).toLowerCase().contains(filterText)
				|| String.valueOf(tr.getSens()).toLowerCase().contains(filterText)
				|| tr.getClient().getNom().toLowerCase().contains(filterText)
				|| String.valueOf(tr.getDate_transaction()).contains(filterText)
				|| tr.getDevise().toLowerCase().contains(filterText)
				|| String.valueOf(tr.getMontant()).contains(filterText)
				|| String.valueOf(tr.getValeur()).contains(filterText)
				|| String.valueOf(tr.getNbrPart()).toLowerCase().contains(filterText);
	}

	public List<Transaction> save() {
		Transaction tr = new Transaction();
		tr.setClient(selectedClient);
		tr.setPortefeuille(selectedPortefeuille);
		Date date_transaction = new Date();
		tr.setDate_transaction(date_transaction);
		tr.setDevise(selectedDevise);
		tr.setMontant(montant);
		tr.setNbrPart(nbrPart);
		tr.setSens(selectedSens);
		tr.setValeur(valeur);

		trService.addTransaction(tr);

		// mettre à jour le nombre total de parts du portefeuille séléctionnés
		selectedPortefeuille.setNbrPart(selectedPortefeuille.getNbrPart() + nbrPart);

		ptfService.updatePortefeuille(selectedPortefeuille);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Transaction éffectuée avec succès!"));

		return transactionList = trService.getAllTransactions();
	}

	public void deleteTransaction(Transaction tr) {
		trService.deleteTransaction(tr);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Transaction supprimée avec succès!"));
        transactionList = trService.getAllTransactions();
	}

}
