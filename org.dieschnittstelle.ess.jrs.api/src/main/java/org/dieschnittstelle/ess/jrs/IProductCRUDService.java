package org.dieschnittstelle.ess.jrs;

import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/*
 * UE JRS2: 
 * deklarieren Sie hier Methoden fuer:
 * - die Erstellung eines Produkts
 * - das Auslesen aller Produkte
 * - das Auslesen eines Produkts
 * - die Aktualisierung eines Produkts
 * - das Loeschen eines Produkts
 * und machen Sie diese Methoden mittels JAX-RS Annotationen als WebService verfuegbar
 */


/*
 * TODO JRS3: aendern Sie Argument- und Rueckgabetypen der Methoden von IndividualisedProductItem auf AbstractProduct
 *  Anschließend müssen wir eine annotation auf abstract product setzten um das dadruch entstanndene Problem zu lösen
 * Abguckbar in induvi abstrct touchpoint  
 */

@Path("/products")	// da sich in allen URIs die /products befindet, könnne wir das ausfaktorisieren
@Consumes({MediaType.APPLICATION_JSON})		 // Ich erwate Jason format
@Produces({MediaType.APPLICATION_JSON})		// ich gebe jason format aus
public interface IProductCRUDService {

	@POST		// ohne @PATH, weil es automatisch den wert aus dem Body nimmt
	public IndividualisedProductItem createProduct(IndividualisedProductItem prod);

	@GET // ganze liste daher auch ohne spezielle PATH angabe
	public List<IndividualisedProductItem> readAllProducts();

	@PUT
	@Path("/{Id}")
	public IndividualisedProductItem updateProduct(@PathParam("Id") long id,
												   IndividualisedProductItem update); // keine ahnung was mit dem zweiten übergabeparameter ist. angeblich darf nur eine da sein

	@DELETE
	@Path("/{Id}")
	boolean deleteProduct(@PathParam("Id") long id);

	@GET
	@Path("/{Id}")
	public IndividualisedProductItem readProduct(@PathParam("Id") long id);
			
}
