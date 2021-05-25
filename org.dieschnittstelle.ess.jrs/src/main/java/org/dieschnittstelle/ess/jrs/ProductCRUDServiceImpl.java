package org.dieschnittstelle.ess.jrs;

import java.util.List;

import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
//import org.apache.logging.log4j.Logger;

/*
 * TODO JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	/**
	 * this accessor will be provided by the ServletContext, to which it is written by the ProductServletContextListener
	 */
	private GenericCRUDExecutor<AbstractProduct> productCRUD;

	/**
	 * here we will be passed the context parameters by the resteasy framework. Alternatively @Context
	 * can  be declared on the respective instance attributes. note that the request context is only
	 * declared for illustration purposes, but will not be further used here
	 *
	 * @param servletContext
	 */
	// Dependency Injektion ?????????????????????????????????????????????????????????????????????????????????????????????????
	// durch das @Context sagen wir dem Framework, das wir den servletContext brauchen
	// jetzt weiss das framework, das ich ein servletContext benötige
	// Liegt im webapp/src/main/java......./ser/ProductServletContextListener
	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		//logger.info("<constructor>: " + servletContext + "/" + request);
		// read out the dataAccessor
		this.productCRUD = (GenericCRUDExecutor<AbstractProduct>) servletContext.getAttribute("productCRUD");

		//logger.debug("read out the touchpointCRUD from the servlet context: " + this.productCRUD);
	}

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndividualisedProductItem updateProduct(long id,
			IndividualisedProductItem update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteProduct(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
