package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import beans.AllTransactionView;
import beans.PortefeuilleView;
import entities.Client;
import entities.Portefeuille;

@FacesConverter("portefeuilleConverter")
public class PortefeuilleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		AllTransactionView allTransView = context.getApplication().evaluateExpressionGet(context,
				"#{allTransactionView}", AllTransactionView.class);
		for (Portefeuille p : allTransView.getPortefeuilleList()) {
			if (String.valueOf(p.getId()).equals(value)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Portefeuille) {
			return String.valueOf(((Portefeuille) value).getId());
		}
		return null;
	}

}
