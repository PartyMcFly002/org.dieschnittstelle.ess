package org.dieschnittstelle.ess.jrs;

// Diese Klasse ist eine erweiterung einer von JRS oberklasse

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// gibt ein gemeinsamen pfadsegment an unter dem alle resourcen auf die zugegriffen werden soll,verfügbar sind und gibt an welche implementierung verwendet werden sollen
@ApplicationPath("/api")
public class WebAPIRoot extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet(Arrays.asList(new Class[]{TouchpointCRUDServiceImpl.class, TouchpointCRUDServiceImplAsync.class, ProductCRUDServiceImpl.class, JacksonJaxbJsonProvider.class}));
    }   // hier wird angegeben, welche implementierungen für die einzelnen respurcen werwendet werden sollen.
    // das ist die menge von klassen, die den funktionsumfang von der JRS-applikation bildet!
    // dadurch weiss Rest-Easy, dass unter dem pfandsement /api auch die touchpoints zur vefügung stehen sollen
    // daruch kann man sich das /api in dem @Path bei ITouchpointCRUDService sparen und nur /touchpoints angeben
}
