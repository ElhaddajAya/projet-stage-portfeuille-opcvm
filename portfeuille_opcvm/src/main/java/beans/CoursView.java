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

import entities.Cours;
import entities.Portefeuille;
import lombok.Data;
import services.CoursService;
import services.PortefeuilleService;

@Data

@ManagedBean
@ViewScoped
public class CoursView {
	
	@Inject
	private CoursService crService;
	
	@Inject
    private PortefeuilleService ptfService;

	/* Listes déroulantes */
	private List<Portefeuille> portefeuilleList;
	private List<String> typeOPCVMList;
	/* Valeurs séléctionnés */
	private Portefeuille selectedPortefeuille;
	
	private List<Cours> courtList;
	List<Portefeuille> filteredCourts;
	private Double cout;
	private String devise;
	private Date date;
	
	@PostConstruct
	public void init() {
		portefeuilleList = ptfService.getAllPortefeuilles();
		courtList = crService.getAllCours();
		
		typeOPCVMList = new ArrayList<>();
		typeOPCVMList.add("Actions");
		typeOPCVMList.add("Obligations");
		typeOPCVMList.add("Diversifiés");
	}
	
	public void save() {	
	    System.err.println("Méthode save() appelée!");

		Cours c = new Cours();
		c.setCout(cout);
		c.setDate(date);
		c.setPortefeuille(selectedPortefeuille);
		
		ptfService.addCourt(c);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Coût ajouté avec succès!"));

		courtList = ptfService.getAllCourts();
	}
	
	/* Methode de filtrage globale */
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
	    if (filter == null || filter.equals("")) {
	        return true; // Aucun filtre, afficher toutes les lignes
	    }

	    Cours court = (Cours) value;
	    String filterText = (filter.toString()).toLowerCase(locale);

	    return court.getPortefeuille().getId().toString().toLowerCase(locale).contains(filterText)
	            || court.getPortefeuille().getLibelle().toLowerCase(locale).contains(filterText)
	            || String.valueOf(court.getCout()).equals(filterText)
	            || court.getPortefeuille().getTypeOpcvm().toLowerCase().contains(filterText)
	            || court.getDate().toString().toLowerCase(locale).contains(filterText);
	}

	public void delete(Long id) {
		crService.deleteCours(id);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Coût supprimé avec succès!"));

		courtList = ptfService.getAllCourts();
	}
	
    public void updateDevise() {
        if (selectedPortefeuille != null) {
            devise = selectedPortefeuille.getDevise();
        } else {
            devise = "NULL";
        }
    }

}
