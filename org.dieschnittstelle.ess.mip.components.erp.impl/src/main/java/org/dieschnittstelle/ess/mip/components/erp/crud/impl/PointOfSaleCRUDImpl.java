package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

@ApplicationScoped
@Logged
@Transactional
/*
 * note that beans that are used between services and the dependencies to which might either
 * be satisified by local implementation or by implementations that use a rest client need
 * to provide a local interface in addition to the remote one. This is mainly due to the following issues
 * - using a single interface with jax-rs annotations and @RegisterRestClient registration results
 * in an implementation ambiguity for monolithic deployments as the dynamically created rest client
 * implementation will be an a alternative to the 'real' implementation)
 * - disambiguation requires introduction of a custom CDI qualifier
 * - usage of the qualifier at the level of the implementation class as a class level identifier results,
 * for whatever reason, in interceptors not being considered anymore if the bean is accessed as a rest resource
 * implementation. In particular, this completely breaks transactionality via @Transactional for these cases
 *
 * The problem both occurs on monolithic wildfly and tomee deployments, but CDI troubleshooting did not reveal any particular solutions to overcome the issue.
 * Usage of two different interfaces helps as ambiguity does not occur, then. One could check whether usage of a producer, like for the entity manager, could help, as well.
 * It would also be nice if @RegisterRestClient could be dealt with at the level of config files
 * rather than by annotation only as usage of it requires additional setup for wildfly in spite of the
 * fact that we do not need it here... well, it is only necessary to require the microprofile-config layer
 */
@Alternative
@Priority(Interceptor.Priority.APPLICATION+10)
public class PointOfSaleCRUDImpl implements PointOfSaleCRUD {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(PointOfSaleCRUDImpl.class);

	@Inject
	@EntityManagerProvider.ERPDataAccessor
	private EntityManager em;
	
	/*
	 * TODO ADD1: comment in/out @TransactionAttribute
	 */
	@Override
	//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PointOfSale createPointOfSale(PointOfSale pos) {
		logger.info("createPointOfSale(): " + pos);

		em.persist(pos);

		return pos;
	}

	@Override
	public PointOfSale readPointOfSale(long posId) {
		return em.find(PointOfSale.class,posId);
	}

	@Override
	public boolean deletePointOfSale(long posId) {
		em.remove(em.find(PointOfSale.class,posId));
		return true;
	}

	@Override
	public List<PointOfSale> readAllPointsOfSale() {
		return em.createQuery("SELECT p FROM PointOfSale AS p").getResultList();
	}

}
