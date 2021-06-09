package org.dieschnittstelle.ess.jrs;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import org.apache.logging.log4j.Logger;

/*
 * TODO JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ProductCRUDServiceImpl.class);

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
	// wir müssen uns nicht darum kümmern dieses Objekt selbst peer konstructor zu instaziieren
	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		//logger.info("<constructor>: " + servletContext + "/" + request);
		// read out the dataAccessor
		this.productCRUD = (GenericCRUDExecutor<AbstractProduct>) servletContext.getAttribute("productCRUD");

		logger.debug("read out the productCRUD: " + this.productCRUD);
	}

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		return (IndividualisedProductItem) this.productCRUD.createObject(prod);
	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {
		List<IndividualisedProductItem> allProducts = (List) this.productCRUD.readAllObjects();		// Mithilfe des CRUD-executers arbeiten und die Produkte auslesen
																									// die methoden readAll stellt der CRUD zur verfügung ich muss nur noch casten!
		return allProducts;
	}

	@Override
	public IndividualisedProductItem updateProduct(long id,
			IndividualisedProductItem update) {

		//return this.productCRUD.updateObject();
		//this.productCRUD.updateObject(update);
		//return (IndividualisedProductItem) this.productCRUD.readObject(id);
		//return (IndividualisedProductItem) this.productCRUD.updateObject(this.productCRUD.readObject(id)); // falsch!

		//return this.productCRUD.updateObject(update.getId(),(IndividualisedProductItem)update);
		return (IndividualisedProductItem) this.productCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {

		logger.debug("DELETE Product with id: " + id);
		return this.productCRUD.deleteObject(id);			// EASY
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {

		IndividualisedProductItem it = (IndividualisedProductItem) this.productCRUD.readObject(id);

		if(it != null)
		{
			return it;
		}
		else
		{
			return null; //throw new NotFoundException("The product with id: " + id + " does not exist!!!");
			// Hier war der Fehler die ganze zeit. Wir sollten uns an Touchpoints orientieren aber dasd war in dem fall keine gute idee
			// Im Test wird am ende ein delete und dann wieder ein read aufgerufen auf ein product das nicht mehr existiert.
			// erwartet wurde eine leer liste!
		}
	}
	
}
