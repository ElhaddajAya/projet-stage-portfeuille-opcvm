package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import beans.AllTransactionView;
import beans.PortefeuilleView;
import beans.TransactionView;
import entities.Client;
import entities.Transaction;

@FacesConverter(forClass = Client.class)
public class ClientConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        AllTransactionView allTransactionView = context.getApplication().evaluateExpressionGet(context, "#{allTransactionView}", AllTransactionView.class);
        for (Client client : allTransactionView.getClientList()) {
            if (String.valueOf(client.getId()).equals(value)) {
                return client;
            }
        }
        TransactionView transactionView = context.getApplication().evaluateExpressionGet(context, "#{transactionView}", TransactionView.class);
        for(Client client : transactionView.getClientList()) {
        	if (String.valueOf(client.getId()).equals(value)) {
                return client;
            }
        }
        PortefeuilleView portefeuilleView = context.getApplication().evaluateExpressionGet(context, "#{portefeuilleView}", PortefeuilleView.class);
        for(Client client : portefeuilleView.getClientList()) 	{
        	if (String.valueOf(client.getId()).equals(value)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Client) {
            return String.valueOf(((Client) value).getId());
        }
        return null;
    }
}
