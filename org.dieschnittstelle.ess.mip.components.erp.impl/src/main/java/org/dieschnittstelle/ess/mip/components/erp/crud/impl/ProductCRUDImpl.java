package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;
import javax.transaction.Transactional;
import javax.enterprise.context.ApplicationScoped;


import java.util.List;





@ApplicationScoped
@Transactional
public class ProductCRUDImpl implements ProductCRUD {

    	/*@Inject
	@EntityManagerProvider.ERPDataAccessor
	private EntityManager em;*/


    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {
        return null;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        return null;
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return null;
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return null;
    }

    @Override
    public boolean deleteProduct(long productID) {
        return false;
    }
}
