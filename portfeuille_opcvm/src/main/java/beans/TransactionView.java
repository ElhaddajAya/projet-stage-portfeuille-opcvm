package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
public class TransactionView {
	
	@Inject
	private TransactionService trService;

	@Inject
	private PortefeuilleService ptfService;
	
	/************ Attributes ***********/
	private Long portefeuilleId;
	private String libelle;
	private String devise;
	
	private Client selectedClient;
	private String selectedSens;

	private double montant;
	private int nbrPart;
	private Portefeuille portefeuille;
	private double vl;
	
	private List<Transaction> transactionList;
	private List<Transaction> filteredTransactions;
	
	/* Listes déroulantes */
	private List<Client> clientList;
	private List<String> deviseList;
	
	@PostConstruct
    public void init() {
        clientList = trService.getAllClients();
        deviseList = List.of("MAD", "EUR", "USD", "CAD", "CHF", "GPB", "JPY", "AUD", "CNY", "INR");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("portefeuilleId")) {
            try {
                portefeuilleId = Long.valueOf(params.get("portefeuilleId"));
                portefeuille = ptfService.getPortefeuilleById(portefeuilleId);
                libelle = trService.getPortefeuilleLibelleById(portefeuilleId);
                devise = portefeuille.getDevise();
                transactionList = trService.getAllTransactionsForPortefeuille(portefeuilleId);

                Cours latestCourt = ptfService.getLatestCourtForPortefeuille(portefeuilleId);
                if (latestCourt != null) {
                    vl = latestCourt.getCout();
                }
            } catch (NumberFormatException | NullPointerException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Portefeuille ID invalide"));
            }
        }
    }
	
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
	    		|| tr.getPortefeuille().getDevise().toLowerCase().contains(filterText)
	            || String.valueOf(tr.getNbrPart()).toLowerCase().contains(filterText);
	}
	
	public List<Transaction> save() {	
	    // Création de la transaction
	    Transaction transaction = new Transaction();
	    transaction.setMontant(montant);
	    transaction.setSens(selectedSens);
	    transaction.setNbrPart(nbrPart);
	    transaction.setClient(selectedClient);
	    transaction.setPortefeuille(portefeuille);
	    Date date_transaction = new Date();
		transaction.setDate_transaction(date_transaction);

	    // Récupération de tous les clients associés au portefeuille
	    List<Transaction> transactions = trService.getAllTransactionsForPortefeuille(portefeuille.getId());
	    int totalParts = trService.getTotalPartsForPortefeuille(portefeuille.getId());
	    
	    // Calculer le nombre total de parts du portefeuille en ajoutant les nouvelles parts
	    totalParts += nbrPart;

	    // Mise à jour du nombre total de parts du portefeuille
	    portefeuille.setNbrPart(totalParts);
	    ptfService.updatePortefeuille(portefeuille);

	    trService.addTransaction(transaction);
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Transaction effectuée avec succès!"));
	    
	    return transactionList = trService.getAllTransactionsForPortefeuille(portefeuilleId);
	}
	
	public void deleteTransaction(Transaction tr) {
		trService.deleteTransaction(tr);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Transaction supprimée avec succès!"));
	    transactionList = trService.getAllTransactionsForPortefeuille(portefeuilleId);
	}
	
	public void calculateMontant() {
        montant = nbrPart * vl;
    }
	
}
