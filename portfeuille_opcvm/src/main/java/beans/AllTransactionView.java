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
import entities.Cours;
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

	private Cours dernierCout;
	
	private int nbrPart;
	private double montant;
	private double vl;
	private String devise;
	private Date date;
	
	private List<Transaction> transactionList;
	private List<Transaction> filteredTransactions;

	/* Listes déroulantes */
	private List<Portefeuille> portefeuilleList;
	private List<String> deviseList;
	private List<Client> clientList;

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
				|| String.valueOf(tr.getMontant()).contains(filterText)
				|| String.valueOf(tr.getNbrPart()).toLowerCase().contains(filterText);
	}

	public List<Transaction> save() {
	    // Vérifier si les clients et portefeuilles sélectionnés sont valides
	    if (selectedClient == null || !clientList.contains(selectedClient)) {
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Client sélectionné invalide"));
	        return null;
	    }
	    if (selectedPortefeuille == null || !portefeuilleList.contains(selectedPortefeuille)) {
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Portefeuille sélectionné invalide"));
	        return null;
	    }

	    Transaction tr = new Transaction();
	    tr.setClient(selectedClient);
	    tr.setPortefeuille(selectedPortefeuille);
	    Date date_transaction = new Date();
	    tr.setDate_transaction(date_transaction);
	    tr.setMontant(montant);
	    tr.setNbrPart(nbrPart);
	    tr.setSens(selectedSens);

	    trService.addTransaction(tr);

	    // mettre à jour le nombre total de parts du portefeuille séléctionnés
	    selectedPortefeuille.setNbrPart(selectedPortefeuille.getNbrPart() + nbrPart);

	    ptfService.updatePortefeuille(selectedPortefeuille);
	    FacesContext.getCurrentInstance().addMessage(null,
	        new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Transaction éffectuée avec succès!"));

	    // Réinitialiser les valeurs
	    selectedClient = null;
	    selectedPortefeuille = null;
	    montant = 0;
	    nbrPart = 0;

	    return transactionList = trService.getAllTransactions();
	}

	public void deleteTransaction(Transaction tr) {
		trService.deleteTransaction(tr);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Transaction supprimée avec succès!"));
        transactionList = trService.getAllTransactions();
	}
	
    public void updateDeviseAndCout() {
        if (selectedPortefeuille != null) {
            devise = selectedPortefeuille.getDevise();
            vl = ptfService.getLatestCoutForPortefeuille(selectedPortefeuille.getId());
            
            System.err.println("Devise " + devise + " + Valeur liquidative " + vl);
        } else {
            devise = "NULL";
            vl = 0;
            
            System.err.println("Devise " + devise + " + Valeur liquidative " + vl);
        }
    }

}
