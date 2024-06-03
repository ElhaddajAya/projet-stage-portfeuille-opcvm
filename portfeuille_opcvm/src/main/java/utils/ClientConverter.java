package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import beans.AllTransactionView;
import beans.CoursView;
import beans.PortefeuilleView;
import beans.TransactionView;
import entities.Client;
import entities.Portefeuille;

@FacesConverter("clientConverter")
public class ClientConverter implements Converter {

	@Inject
    private TransactionView transactionView;

    @Inject
    private AllTransactionView allTransactionView;

    @Inject
    private PortefeuilleView portefeuilleView;
    
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			Long id = Long.valueOf(value);
			// Essayer de récupérer leclient depuis TransactionView
			TransactionView trView = context.getApplication().evaluateExpressionGet(context, "#{transactionView}",
					TransactionView.class);
			if (trView != null && trView.getClientList() != null) {
				for (Client p : trView.getClientList()) {
					if (p.getId().equals(id)) {
						return p;
					}
				}
			}
			// Si non trouvé dans CoursView, essayer de récupérer depuis AllTransactionView
			AllTransactionView allTransactionView = context.getApplication().evaluateExpressionGet(context,
					"#{allTransactionView}", AllTransactionView.class);
			if (allTransactionView != null && allTransactionView.getPortefeuilleList() != null) {
				for (Client c : allTransactionView.getClientList()) {
					if (c.getId().equals(id)) {
						return c;
					}
				}
			}
			// Si non trouvé dans Portefeuille, essayer de récupérer depuis
			// AllTransactionView
			PortefeuilleView pView = context.getApplication().evaluateExpressionGet(context, "#{portefeuilleView}",
					PortefeuilleView.class);
			if (pView != null && pView.getPortefeuilleList() != null) {
				for (Client c : pView.getClientList()) {
					if (c.getId().equals(id)) {
						return c;
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
		if (value instanceof Client) {
			Client client = (Client) value;
			return client.getId() != null ? client.getId().toString() : "";
		}
		return "";
	}
}
