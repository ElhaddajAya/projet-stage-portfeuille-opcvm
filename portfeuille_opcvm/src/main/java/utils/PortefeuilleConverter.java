package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import beans.AllTransactionView;
import beans.CoursView;
import entities.Portefeuille;

@FacesConverter("portefeuilleConverter")
public class PortefeuilleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			Long id = Long.valueOf(value);
			// Essayer de récupérer le portefeuille depuis CoursView
			CoursView coursView = context.getApplication().evaluateExpressionGet(context, "#{coursView}",
					CoursView.class);
			if (coursView != null && coursView.getPortefeuilleList() != null) {
				for (Portefeuille p : coursView.getPortefeuilleList()) {
					if (p.getId().equals(id)) {
						return p;
					}
				}
			}
			// Si non trouvé dans CoursView, essayer de récupérer depuis AllTransactionView
			AllTransactionView allTransactionView = context.getApplication().evaluateExpressionGet(context,
					"#{allTransactionView}", AllTransactionView.class);
			if (allTransactionView != null && allTransactionView.getPortefeuilleList() != null) {
				for (Portefeuille p : allTransactionView.getPortefeuilleList()) {
					if (p.getId().equals(id)) {
						return p;
					}
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("Erreur de conversion : " + e.getMessage());
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Portefeuille) {
			Portefeuille portefeuille = (Portefeuille) value;
			return portefeuille.getId() != null ? portefeuille.getId().toString() : "";
		}
		return "";
	}
}