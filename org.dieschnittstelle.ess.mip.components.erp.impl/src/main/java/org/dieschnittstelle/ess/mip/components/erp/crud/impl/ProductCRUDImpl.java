package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.enterprise.context.ApplicationScoped;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.dieschnittstelle.ess.utils.Utils.show;


@ApplicationScoped
@Transactional
public class ProductCRUDImpl implements ProductCRUD {

    @Inject
	@EntityManagerProvider.ERPDataAccessor
	private EntityManager em;


    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {

        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        show("Wer des EntityManager ist:" + em);
        IndividualisedProductItem productItem = new IndividualisedProductItem();
        productItem.setName("Just a test");
        //return new ArrayList<>();
        return Arrays.asList(productItem);
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
