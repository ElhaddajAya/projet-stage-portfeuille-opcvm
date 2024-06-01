package beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import entities.Court;
import entities.Portefeuille;
import lombok.Data;
import services.PortefeuilleService;

@Data

@ManagedBean
@ViewScoped
public class ChartView implements Serializable {

    @Inject
    private PortefeuilleService portefeuilleService;

    private LineChartModel lineModel;
    
    private Long portefeuilleId;
    private Portefeuille portefeuille;
    private String libelle;
    
    @PostConstruct
    public void init() {
        // Récupérer l'identifiant du portefeuille à partir des paramètres de la requête
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("portefeuilleId")) {
            portefeuilleId = Long.valueOf(params.get("portefeuilleId"));
            
            Portefeuille p = portefeuilleService.getPortefeuilleById(portefeuilleId);
            libelle = p.getLibelle();
        }

        createLineModel();
    }

    private void createLineModel() {
        lineModel = new LineChartModel();
        Portefeuille portefeuille = portefeuilleService.getPortefeuilleById(portefeuilleId);

        if (portefeuille == null) {
            System.out.println("Le portefeuille est null");
            return;
        }

        LineChartSeries series = new LineChartSeries();
        series.setLabel(portefeuille.getLibelle());

        List<Court> courts = portefeuille.getCours();
        if (courts == null || courts.isEmpty()) {
            System.out.println("Les données des cours sont vides");
            return;
        }

        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Court court : courts) {
            System.out.println("Ajout du point de données : " + court.getDate() + " - " + court.getCout());
            series.set(court.getDate(), court.getCout());

            // Mettre à jour les valeurs minimales et maximales pour l'axe Y
            if (court.getCout() < minY) {
                minY = court.getCout();
            }
            if (court.getCout() > maxY) {
                maxY = court.getCout();
            }
        }

        lineModel.addSeries(series);
        lineModel.setTitle("Coût du Portefeuille "+ portefeuille.getLibelle() + " en Fonction de Date");
        lineModel.setLegendPosition("e");
        lineModel.setShowPointLabels(true);

        DateAxis xAxis = new DateAxis("Dates");
        xAxis.setTickFormat("%Y-%m-%d");
        xAxis.setTickCount(10); // Définir le nombre de marques pour éviter l'encombrement des étiquettes
        lineModel.getAxes().put(AxisType.X, xAxis);

        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Coût");
        yAxis.setMin(minY);
        yAxis.setMax(maxY);
        yAxis.setTickFormat("%.0f"); // Afficher des nombres entiers sur l'axe Y avec une échelle logarithmique
    }

}
