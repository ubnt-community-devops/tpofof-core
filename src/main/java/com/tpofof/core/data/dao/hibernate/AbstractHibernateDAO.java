package com.tpofof.core.data.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.IPersistentModelDAO;
import com.tpofof.core.data.dao.ResultsSet;

//@Component
public abstract class AbstractHibernateDAO<ModelT extends IPersistentModel<ModelT,PrimaryKeyT>, PrimaryKeyT> implements IPersistentModelDAO<ModelT, PrimaryKeyT, Criteria> {
	
//	@Autowired 
	private SessionFactory sessionFactory;
	
	protected abstract Class<ModelT> getModelClass();
	
	protected Session getSession() {
		return sessionFactory.openSession();
	}
	
	public ModelT insert(final ModelT model) {
		final Session session = getSession();
		return new AbstractHibernateTransactionRunnable<ModelT>() {
			@Override
			protected Session getSession() {
				return session;
			}
			@Override
			protected ModelT hibernateAction() {
				session.save(model);
				return model;
			}
		}.run();
	}
	
	@SuppressWarnings("unchecked")
	public ModelT find(final PrimaryKeyT id) {
        final Session session = getSession();
		return new AbstractHibernateTransactionRunnable<ModelT>() {
			@Override
			protected Session getSession() {
				return session;
			}
			@Override
			protected ModelT hibernateAction() {
				Criteria c = getSession().createCriteria(getModelClass())
						.add(Restrictions.idEq(id));
				return (ModelT) c.uniqueResult();
			}
		}.run();
	}
	
	public long count() {
		final Session session = getSession();
		return new AbstractHibernateTransactionRunnable<Long>() {
			@Override
			protected Session getSession() {
				return null;
			}
			@Override
			protected Long hibernateAction() {
				return count(session.createCriteria(getModelClass()));
			}
		}.run();
	}
	
	public long count(Criteria criteria) {
		Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return count != null ? count : -1;
	}

	public ResultsSet<ModelT> find(final int limit, final int offset) {
		final Session session = getSession();
		return new AbstractHibernateTransactionRunnable<ResultsSet<ModelT>>() {
			@Override
			protected Session getSession() {
				return session;
			}

			@Override
			protected ResultsSet<ModelT> hibernateAction() {
				Criteria c = getSession().createCriteria(getModelClass());
				c.setMaxResults(limit);
				c.setFirstResult(offset);
				return find(c, limit, offset);
			}
		}.run();
	}

	@SuppressWarnings("unchecked")
	public ResultsSet<ModelT> find(Criteria criteria, int limit, int offset) {
        return ResultsSet.<ModelT>builder()
        		.limit(limit)
        		.offset(offset)
        		.total(count(criteria))
        		.results(criteria.list())
        		.build();
	}

	public ModelT update(final ModelT model) {
		final Session session = getSession();
		return new AbstractHibernateTransactionRunnable<ModelT>() {
			@Override
			protected Session getSession() {
				return session;
			}
			@Override
			protected ModelT hibernateAction() {
				session.update(model);
				return model;
			}
		}.run();
	}

	@Transactional
	public boolean delete(final PrimaryKeyT id) {
		final Session session = getSession();
		final ModelT model = find(id);
		return new AbstractHibernateTransactionRunnable<Boolean>() {
			@Override
			protected Session getSession() {
				return session;
			}
			@Override
			protected Boolean hibernateAction() {
				session.delete(model);
				return true;
			}
		}.run();
	}
}
