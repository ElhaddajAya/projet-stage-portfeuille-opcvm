package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import beans.PortefeuilleView;
import entities.SocieteGestion;

@FacesConverter(forClass = SocieteGestion.class)
public class SocieteGestionConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        PortefeuilleView portefeuilleView = context.getApplication().evaluateExpressionGet(context, "#{portefeuilleView}", PortefeuilleView.class);
        for (SocieteGestion societeGestion : portefeuilleView.getSocieteGestionList()) {
            if (String.valueOf(societeGestion.getId()).equals(value)) {
                return societeGestion;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof SocieteGestion) {
            return String.valueOf(((SocieteGestion) value).getId());
        }
        return null;
    }
}
