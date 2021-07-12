package org.dieschnittstelle.ess.mip.components.crm.crud.impl;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.dieschnittstelle.ess.mip.components.crm.api.CrmException;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.mip.components.crm.crud.api.TouchpointCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import static org.dieschnittstelle.ess.utils.Utils.show;

@Logged
@ApplicationScoped
@Transactional
public class TouchpointCRUDImpl implements TouchpointCRUD {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(TouchpointCRUDImpl.class);

	@Inject
	@EntityManagerProvider.CRMDataAccessor
	private EntityManager em;

	/*
	 * TODO ADD1: run CreateTouchpointsAccessingCRUD in the client project with the @TransactionAttribute commented in - what happens?
	 */
	@Override
	public AbstractTouchpoint createTouchpoint(AbstractTouchpoint touchpoint) throws CrmException {

		/*
		 * TODO ADD1: swap true/false
		 */		
		if (/*true*/false) {
			throw new RuntimeException(new CrmException("Exception provoked by implementation!"));
		} else {
			em.persist(touchpoint);
			return touchpoint;
		}

	}

	@Override
	public AbstractTouchpoint readTouchpoint(long id) {
		AbstractTouchpoint touchpoint = em.find(AbstractTouchpoint.class, id);

		return touchpoint;
	}

	@Override
	public AbstractTouchpoint updateTouchpoint(AbstractTouchpoint touchpoint) {
		touchpoint = em.merge(touchpoint);
		return touchpoint;
	}

	@Override
	public boolean deleteTouchpoint(int id) {
		em.remove(em.find(AbstractTouchpoint.class, id));

		return true;
	}

	@Override
	public List<AbstractTouchpoint> readAllTouchpoints() {

		show("entity manager is: " + this.em);

		Query query = em.createQuery("SELECT t FROM AbstractTouchpoint t");

		List<AbstractTouchpoint> tps = (List<AbstractTouchpoint>) query
				.getResultList();

		return tps;
	}

}
