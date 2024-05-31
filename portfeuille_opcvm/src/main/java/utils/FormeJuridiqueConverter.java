package utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("formeJuridiqueConverter")
public class FormeJuridiqueConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        // La conversion de la chaîne en un objet FormeJuridique n'est pas nécessaire car la forme juridique est représentée par une chaîne
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        // Convertir la chaîne en une chaîne (pas de conversion nécessaire)
        if (value != null) {
            return value.toString();
        }
        return null;
    }

}
