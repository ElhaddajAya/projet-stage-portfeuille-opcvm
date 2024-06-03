package beans;
import java.io.Serializable;
import java.util.ArrayList;
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
import entities.SocieteGestion;
import lombok.Data;
import services.PortefeuilleService;

@Data
@ManagedBean
@ViewScoped
public class PortefeuilleView implements Serializable {

	@Inject
	private PortefeuilleService ptfService;

	/**** Attributes *****/
	private Long id;
	private String libelle;
	private int nbrPart;

	private List<Portefeuille> portefeuilleList;
	private List<Portefeuille> filteredPortefeuilles;
	private Portefeuille selectedPortefeuille;

	/* Listes déroulantes */
	private List<String> typeInvestList;
	private List<String> typeOPCVMList;
	private List<String> deviseList;
	private List<String> objectifList;
	private List<String> horizonList;
	private List<String> profileRisqueList;
	private List<String> formeJuridList;
	private List<Client> clientList;
	private List<SocieteGestion> societeGestionList;
	private List<Cours> courtList;

    private List<Client> distinctClientsForPortefeuille;
	
	private Long ptfId;
	private Portefeuille ptf;
	
	/* Valeurs sélectionnées */
	private String selectedTypeInvest;
	private String selectedTypeOPCVM;
	private String selectedObjectif;
	private String selectedHorizon;
	private String selectedProfileRisque;
	private String selectedFormeJurid;
	private Client selectedClient;
	private SocieteGestion selectedSocieteGest;
	private String selectedDevise;

	/**** Post-onstructor ******/
	@PostConstruct
	public void init() { /* Initialisation des listes déroulantes */
		typeInvestList = new ArrayList<>();
		typeInvestList.add("Professionnel et CGP");
		typeInvestList.add("Institutionnel");
		typeInvestList.add("Privé");

		typeOPCVMList = new ArrayList<>();
		typeOPCVMList.add("Actions");
		typeOPCVMList.add("Monétaire");
		typeOPCVMList.add("Obligataire");
		typeOPCVMList.add("Diversifiés");

		objectifList = new ArrayList<>();
		objectifList.add("Croissance");
		objectifList.add("Revenu");
		objectifList.add("Equilibre");

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
		
		horizonList = new ArrayList<>();
		horizonList.add("Court terme (0-1an)");
		horizonList.add("Moyen terme (1-5ans)");
		horizonList.add("Long terme (5ans+)");

		profileRisqueList = new ArrayList<>();
		profileRisqueList.add("Dynamique");
		profileRisqueList.add("Prudent");
		profileRisqueList.add("Equillibré");
		
		formeJuridList = new ArrayList<>();
		formeJuridList.add("Société à Responsabilité Limitée (SARL)");
		formeJuridList.add("Société Anonyme (SA)");
		formeJuridList.add("Société en Nom Collectif (SNC)");
		formeJuridList.add("Société en Commandite Simple (SCS)");
		formeJuridList.add("Société en Commandite par Actions (SCA)");
		formeJuridList.add("Entreprise Individuelle (EI)");

		/* Réinitialiser les listes */
		clientList = ptfService.getAllClients();
		societeGestionList = ptfService.getAllSocieteGestion();
		portefeuilleList = ptfService.getAllPortefeuilles();
		courtList = ptfService.getAllCourts();
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	    if (params.containsKey("ptfId")) {
	        ptfId = Long.valueOf(params.get("ptfId"));
	        // Fetch the ptf's data using the client ID
	        ptf = ptfService.getPortefeuilleById(ptfId);
	        // Populate the fields with the ptf's data
	        libelle = ptf.getLibelle();
	        nbrPart = ptf.getNbrPart(); 
	        selectedDevise = ptf.getDevise();
	        selectedTypeOPCVM = ptf.getTypeOpcvm();
	        selectedSocieteGest = ptf.getSocieteGestion();
	        selectedTypeInvest = ptf.getTypeInvest();
	        selectedObjectif = ptf.getObjectif();
	        selectedHorizon = ptf.getHorizon();
	        selectedProfileRisque = ptf.getProfileRisque();
	        selectedDevise = ptf.getDevise();
	        
	        // Peupler la liste distinctClientsForPortefeuille avec les clients distincts pour ce portefeuille
	        distinctClientsForPortefeuille = ptfService.getDistinctClientsForPortefeuille(ptfId);
	        
	        // Mettre à jour les totaux des parties et des montants pour chaque client
	        for (Client client : distinctClientsForPortefeuille) {
	            client.setTotalParts(0); // Initialiser le total des parts à zéro
	            client.setTotalAmount(0.0); // Initialiser le total des montants à zéro
	        }
	        
	        // Récupérer les informations sur les transactions des clients pour ce portefeuille et mettre à jour les totaux
	        List<Client> clientTransactionInfoList = ptfService.getClientTransactionInfoForPortefeuille(ptfId);
	        for (Client client : clientTransactionInfoList) {
	            for (Client distinctClient : distinctClientsForPortefeuille) {
	                if (client.getId().equals(distinctClient.getId())) {
	                    distinctClient.setTotalParts(client.getTotalParts());
	                    distinctClient.setTotalAmount(client.getTotalAmount());
	                    break;
	                }
	            }
	        }
	    }
	}

	/***** Bean methods *****/
	public List<Portefeuille> save() {
		Portefeuille ptf = new Portefeuille();

		ptf.setLibelle(libelle);
		ptf.setNbrPart(0); // Initialiser le nombre de parts à zéro
		ptf.setTypeOpcvm(selectedTypeOPCVM);
		ptf.setSocieteGestion(selectedSocieteGest);
		ptf.setTypeInvest(selectedTypeInvest);
		ptf.setObjectif(selectedObjectif);
		ptf.setDevise(selectedDevise);
		ptf.setHorizon(selectedHorizon);
		ptf.setProfileRisque(selectedProfileRisque);

		ptfService.addPortefeuille(ptf); // Save portefeuille
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Portefeuille ajouté avec succès!"));

		// Rafraîchir la liste des portefeuilles
		return portefeuilleList = ptfService.getAllPortefeuilles();
	}

	/* Methode pour filtrer les colonnes du tableau */
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (filterText == null || filterText.isEmpty()) {
			return true;
		}

		Portefeuille portefeuille = (Portefeuille) value;
		return String.valueOf(portefeuille.getId()).toLowerCase().contains(filterText)
				|| portefeuille.getLibelle().toLowerCase().contains(filterText)
				|| portefeuille.getTypeOpcvm().toLowerCase().contains(filterText)
				|| portefeuille.getDevise().toLowerCase().contains(filterText)
				|| String.valueOf(portefeuille.getNbrPart()).contains(filterText)
				|| portefeuille.getSocieteGestion().getNom().toLowerCase().contains(filterText)
				|| String.valueOf(portefeuille.getNbrPart()).toLowerCase().contains(filterText);
	}

	public void deletePortefeuille(Portefeuille ptf) {
		ptfService.deletePortefeuille(ptf);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Portefeuille supprimé avec succès!"));
	    
	    // Rafraîchir la liste des portefeuilles
	    portefeuilleList = ptfService.getAllPortefeuilles();
	}
	
	public void update() {
        System.out.println("Update method called");

	    ptf.setLibelle(libelle);
	    ptf.setTypeOpcvm(selectedTypeOPCVM);
	    ptf.setSocieteGestion(selectedSocieteGest);
	    ptf.setTypeInvest(selectedTypeInvest);
	    ptf.setObjectif(selectedObjectif);
	    ptf.setHorizon(selectedHorizon);
	    ptf.setDevise(selectedDevise);
	    ptf.setProfileRisque(selectedProfileRisque);

	    ptfService.updatePortefeuille(ptf);
	    FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Portefeuille mis à jour avec succès!"));
	    portefeuilleList = ptfService.getAllPortefeuilles();
	}

	public Double getTotalMontants() {
	    Double coutPart = ptfService.getLatestCoutForPortefeuille(ptfId);
	    if (coutPart != null) {
	        return nbrPart * coutPart;
	    } else {
	        return 0.0; // Ou une valeur par défaut appropriée si le coût de la part est null
	    }
	}
	
}