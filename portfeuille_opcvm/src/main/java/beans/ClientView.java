package beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import entities.Client;
import entities.Transaction;
import lombok.Data;
import services.ClientService;
import services.TransactionService;

@Data

@ManagedBean
@ViewScoped
public class ClientView {

	@Inject
	private ClientService clService;
	
	@Inject
	private TransactionService trService;

	private Long id;
	private String nom;
	private String email;
	private String telephone;
	private String adressePostale;
	private String formeJuridique;
	private String activitePrincipale;
	private boolean estPhysique;

	private Long clientId;
	private Client client;

	private List<Client> clientList;
	private List<Client> filteredClients;
	private List<String> formeList;
	
    private List<Transaction> distinctTransactionsForClient;


	@PostConstruct
	public void init() {
		clientList = clService.getAllClients();

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (params.containsKey("clientId")) {
			clientId = Long.valueOf(params.get("clientId"));
			// Fetch the client's data using the client ID
			client = clService.getClientById(clientId);
			// Populate the fields with the client's data
			nom = client.getNom();
			email = client.getEmail();
			telephone = client.getTelephone();
			adressePostale = client.getAdressePostale();
			activitePrincipale = client.getActivitePrincipale();
			formeJuridique = client.getFormeJuridique();
			estPhysique = client.isEstPhysique();
		}

		formeList = new ArrayList<>();
		formeList.add("Société à Responsabilité Limitée (SARL)");
		formeList.add("Société Anonyme (SA)");
		formeList.add("Société en Nom Collectif (SNC)");
		formeList.add("Société en Commandite Simple (SCS)");
		formeList.add("Société en Commandite par Actions (SCA)");
		formeList.add("Entreprise Individuelle (EI)");
		
		
		distinctTransactionsForClient = clService.getDistinctTransactionsForClient(clientId);


	}

	/************** Bean methods **************/
	/* Methode pour filtrer les colonnes du tableau */
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.isEmpty()) {
			return true;
		}

		Client client = (Client) value;
		return String.valueOf(client.getId()).toLowerCase().contains(filterText)
				|| client.getNom().toLowerCase().contains(filterText)
				|| client.getEmail().toLowerCase().contains(filterText)
				|| client.getTelephone().toLowerCase().contains(filterText)
				|| client.getAdressePostale().toLowerCase().contains(filterText);
	}

	/* Methode pour inserrer un client */
	public List<Client> save() {
		Client c = new Client();

		c.setNom(nom);
		c.setEmail(email);
		c.setTelephone(telephone);
		c.setFormeJuridique(formeJuridique);
		c.setActivitePrincipale(activitePrincipale);
		c.setAdressePostale(adressePostale);
		c.setEstPhysique(estPhysique);

		clService.addClient(c);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Client ajouté avec succès!"));

		// Rafraîchir la liste des portefeuilles
		return clientList = clService.getAllClients();
	}

	public void delete(Client client) {
		clService.deleteClient(client.getId());
		addMessage("Confirmed", "Record deleted");
		clientList = clService.getAllClients(); // Refresh the list
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void update() {
		System.out.println("Update method called");

		client.setNom(nom);
		client.setEmail(email);
		client.setTelephone(telephone);
		client.setAdressePostale(adressePostale);
		client.setFormeJuridique(formeJuridique);
		client.setActivitePrincipale(activitePrincipale);
		client.setEstPhysique(estPhysique);

		clService.updateClient(client);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Client mis à jour avec succès!"));
		clientList = clService.getAllClients();
	}

}