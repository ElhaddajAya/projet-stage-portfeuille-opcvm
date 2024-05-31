package beans;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import entities.Court;
import entities.Portefeuille;
import lombok.Data;
import services.PortefeuilleService;

@Data

@ManagedBean
@ViewScoped
public class PerformanceView {
	
	private Long portefeuilleId;
    private LineChartModel lineModel;

    @Inject
    private PortefeuilleService ptfService;

    @PostConstruct
    public void init() {
        if (portefeuilleId != null) {
            Portefeuille portefeuille = ptfService.getPortefeuilleById(portefeuilleId);
            if (portefeuille != null) {
                lineModel = createLineModel();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Portefeuille non trouvé"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "ID du portefeuille non spécifié"));
        }
    }
    
	// Méthode pour créer le modèle de graphique linéaire
    public LineChartModel createLineModel() {
	    lineModel = new LineChartModel();

	    // Récupérer l'ID du portefeuille à partir des paramètres de la requête
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    String portefeuilleIdParam = externalContext.getRequestParameterMap().get("portefeuilleId");
	    Long portefeuilleId = Long.parseLong(portefeuilleIdParam);

	    // Récupérer les données de coût par date pour le portefeuille spécifique
	    Portefeuille portefeuille = ptfService.getPortefeuilleById(portefeuilleId);
	    List<Court> costDataList = ptfService.getCourtsByPortefeuille(portefeuille);

	    // Créer une série de données pour le graphique
	    LineChartSeries costSeries = new LineChartSeries();
	    costSeries.setLabel("Coût du portefeuille " + portefeuille.getLibelle());

	    // Ajouter les données à la série de données
	    for (Court data : costDataList) {
	        Date date = data.getDate();
	        double cost = data.getCout();
	        costSeries.set(date, cost);
	    }

	    // Ajouter la série de données au modèle de graphique
	    lineModel.addSeries(costSeries);

	    // Définir les options du modèle de graphique
	    lineModel.setTitle("Coût du portefeuille " + portefeuille.getLibelle());
	    lineModel.setLegendPosition("e");
	    lineModel.setShowPointLabels(true);

	    // Définir les labels des axes
	    Axis xAxis = lineModel.getAxis(AxisType.X);
	    xAxis.setLabel("Date");

	    Axis yAxis = lineModel.getAxis(AxisType.Y);
	    yAxis.setLabel("Coût");
	    yAxis.setMin(0);

		return lineModel;
	}

}
