package org.dieschnittstelle.ess.jrs;

import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

// DAS ist die schnittstelle!!!!!!!!!!!!!!!!
// Diese ganzen annotation sind zur laufzeit der JVM bekannt
// Wir gebene durch die annotationen zusatzinformationen an das interface für das JRS Framework
@Path("/touchpoints")	// da sich in allen URIs die /touchpoints befindet, könnne wir das ausfaktorisieren
@Consumes({MediaType.APPLICATION_JSON})		 // Ich erwate Jason format
@Produces({MediaType.APPLICATION_JSON})		// ich gebe jason format aus
public interface ITouchpointCRUDService {
	
	@GET
	List<StationaryTouchpoint> readAllTouchpoints();

	@GET
	@Path("/{touchpointId}")
	StationaryTouchpoint readTouchpoint(@PathParam("touchpointId") long id);
	@POST	// hier ist kein @PathParam nötig und die es wird automatisch aus dem Body das tp element genommen
	StationaryTouchpoint createTouchpoint(StationaryTouchpoint touchpoint);
	
	@DELETE
	@Path("/{touchpointId}")
	boolean deleteTouchpoint(@PathParam("touchpointId") long id);
		
	/*
	 * TODO JRS1: add a new annotated method for using the updateTouchpoint functionality of TouchpointCRUDExecutor and implement it
	 * hinzugefügt am 29.06.2021
	 */

	@PUT
	@Path("/{touchpointId}")
	public StationaryTouchpoint updateTouchpoint(@PathParam("touchpointId") long id,StationaryTouchpoint touchpoint);

}
