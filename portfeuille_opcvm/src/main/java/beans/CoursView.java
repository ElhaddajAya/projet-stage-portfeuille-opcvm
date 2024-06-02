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
import lombok.Data;
import services.CoursService;
import services.PortefeuilleService;
import utils.PortefeuilleConverter;

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
	
	private List<Court> courtList;
	List<Portefeuille> filteredCourts;
	private Double cout;
	
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

		Court c = new Court();
		c.setCout(cout);
		c.setDate(new Date());
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

	    Court court = (Court) value;
	    String filterText = (filter.toString()).toLowerCase(locale);

	    return court.getPortefeuille().getId().toString().toLowerCase(locale).contains(filterText)
	            || court.getPortefeuille().getLibelle().toLowerCase(locale).contains(filterText)
	            || String.valueOf(court.getCout()).equals(filterText)
	            || court.getDate().toString().toLowerCase(locale).contains(filterText);
	}

	public void delete(Long id) {
		crService.deleteCours(id);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Coût supprimé avec succès!"));

		courtList = ptfService.getAllCourts();
	}

}
