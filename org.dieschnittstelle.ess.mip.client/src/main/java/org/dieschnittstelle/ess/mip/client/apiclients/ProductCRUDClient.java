package org.dieschnittstelle.ess.mip.client.apiclients;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.mip.client.Constants;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;

import javax.inject.Inject;
import javax.persistence.Entity;

public class ProductCRUDClient implements ProductCRUD {

	private ProductCRUD serviceProxy;


	public ProductCRUDClient() throws Exception {
		// TODO: obtain a proxy specifying the service interface. Let all subsequent methods use the proxy.
		this.serviceProxy = ServiceProxyFactory.getInstance().getProxy(ProductCRUD.class);
	}

	//@Override
	public AbstractProduct createProduct(AbstractProduct prod) {

//		// TODO: KOMMENTIEREN SIE DIE FOLGENDE ZUWEISUNG VON IDs UND DIE RETURN-ANWEISUNG AUS
//		prod.setId(Constants.nextId());
//		return prod;

		// TODO: KOMMENTIEREN SIE DEN FOLGENDEN CODE, INKLUSIVE DER ID ZUWEISUNG, EIN
		AbstractProduct created = serviceProxy.createProduct(prod);
		// as a side-effect we set the id of the created product on the argument before returning
		prod.setId(created.getId());
		return created;
	}

	//@Override
	public List<AbstractProduct> readAllProducts() {
		return serviceProxy.readAllProducts();
		//return null;
	}

	//@Override
	public AbstractProduct updateProduct(AbstractProduct update) {
		return serviceProxy.updateProduct(update);
		//return null;
	}

	//@Override
	public AbstractProduct readProduct(long productID) {
		return serviceProxy.readProduct(productID);
		//return null;
	}

	@Override
	public boolean deleteProduct(long productID) {
		return serviceProxy.deleteProduct(productID);
		//return false;
	}

}
