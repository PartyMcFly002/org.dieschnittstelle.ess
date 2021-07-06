package org.dieschnittstelle.ess.mip.components.erp.crud.api;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/*
 * TODO MIP+JPA1/2/5:
 * this interface shall be implemented using a RequestScoped CDI bean with an EntityManager.
 * See TouchpointCRUDImpl for an example bean with a similar scope of functionality
 */
@Path("/products") // ich denke das sollte der ähnluche pfad sein wie in den andern übungen
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface ProductCRUD {

	@POST
	public AbstractProduct createProduct(AbstractProduct prod);

	@GET
	public List<AbstractProduct> readAllProducts();

	@PUT
	@Path("/{id}")
	public AbstractProduct updateProduct(@PathParam("id") AbstractProduct update);

	@GET
	@Path("/{id}")
	public AbstractProduct readProduct(@PathParam("id") long productID);

	@DELETE
	@Path("/{id}")
	public boolean deleteProduct(@PathParam("id") long productID);

}
