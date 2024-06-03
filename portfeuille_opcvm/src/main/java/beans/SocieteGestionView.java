package beans;

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

import entities.Portefeuille;
import entities.SocieteGestion;
import lombok.Data;
import services.SocieteGestionService;

@Data
@ManagedBean
@ViewScoped
public class SocieteGestionView {

	@Inject
	private SocieteGestionService sgService;

	/*************** Attributes ************/
	private String id;
	private String nom;
	private String telephone;
	private String email;
	private String adressePostale;
	private String siteWeb;
	
	private long sgId;
	private SocieteGestion sg;
	private List<Portefeuille> portefeuilles;
	private List<String> typeInvestList;
	
	private List<String> typeOPCVMList;
	private List<String> objectifList;
	private List<String> horizonList;
	private List<String> profileRisqueList;
	
	private List<SocieteGestion> filteredSocietesGestion;
	private List<SocieteGestion> societeGestionList;

	/***************** Post-condtructor ****************/
	@PostConstruct
	public void init() {
		societeGestionList = sgService.getAllSocieteGestion();
		
		typeOPCVMList = new ArrayList<>();
		typeOPCVMList.add("Actions");
		typeOPCVMList.add("Monétaire");
		typeOPCVMList.add("Obligataire");
		typeOPCVMList.add("Diversifiés");
		
		typeInvestList = new ArrayList<>();
		typeInvestList.add("Professionnel et CGP");
		typeInvestList.add("Institutionnel");
		typeInvestList.add("Privé");
		
		objectifList = new ArrayList<>();
		objectifList.add("Croissance");
		objectifList.add("Revenu");
		objectifList.add("Equilibre");

		horizonList = new ArrayList<>();
		horizonList.add("Court terme (0-1an)");
		horizonList.add("Moyen terme (1-5ans)");
		horizonList.add("Long terme (5ans+)");

		profileRisqueList = new ArrayList<>();
		profileRisqueList.add("Dynamique");
		profileRisqueList.add("Prudent");
		profileRisqueList.add("Equillibré");
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	    if (params.containsKey("sgId")) {
	        sgId = Long.valueOf(params.get("sgId"));
	        // Fetch the client's data using the societeGestion ID
	        sg = sgService.getSocieteGestionById(sgId);
	        // Populate the fields with the societeGestion's data
	        nom = sg.getNom();
	        email = sg.getEmail();
	        telephone = sg.getTelephone();
	        adressePostale = sg.getAdressePostale();
	        siteWeb = sg.getSiteWeb();
	        
	        portefeuilles = new ArrayList<>();
	        portefeuilles = sgService.getPortefeuillesBySocieteGestion(sgId);
	    }
	}

	/**************** Bean methods *****************/
	/* Methode pour filtrer les colonnes du tableau */
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
	    String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
	    if (filterText == null || filterText.isEmpty()) {
	        return true;
	    }

	    SocieteGestion sg = (SocieteGestion) value;
	    return String.valueOf(sg.getId()).toLowerCase().contains(filterText)
	    		|| sg.getNom().toLowerCase().contains(filterText)
	            || sg.getEmail().toLowerCase().contains(filterText)
	            || sg.getAdressePostale().toLowerCase().contains(filterText)
	            || sg.getTelephone().toLowerCase().contains(filterText)
	            || sg.getSiteWeb().toLowerCase().contains(filterText);
	}

	/* Methode pour inserrer une societe de gestion */
	public List<SocieteGestion> save() {
		SocieteGestion sg = new SocieteGestion();
		
		sg.setNom(nom);
		sg.setEmail(email);
		sg.setTelephone(telephone);
		sg.setAdressePostale(adressePostale);
		sg.setSiteWeb(siteWeb);
		
		sgService.addSocieteGestion(sg);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Société ajoutée avec succès!"));
	    
	    // Rafraîchir la liste des portefeuilles
	    return societeGestionList = sgService.getAllSocieteGestion();
	}
	
	public void delete(SocieteGestion societe) {
		sgService.deleteSociete(societe.getId());
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Société supprimée avec succès!"));
		societeGestionList = sgService.getAllSocieteGestion(); 
	}
	
	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void update() {
        System.out.println("Update method called");

	    sg.setNom(nom);
	    sg.setEmail(email);
	    sg.setTelephone(telephone);
	    sg.setAdressePostale(adressePostale);
	    sg.setSiteWeb(siteWeb);

	    sgService.updateSocieteGestion(sg);;
	    FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Société mise à jour avec succès!"));
	    societeGestionList = sgService.getAllSocieteGestion();
	}
}
