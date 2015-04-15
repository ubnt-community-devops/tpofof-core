package com.tpofof.core.data.dao.hibernate;

import lombok.Cleanup;

import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class AbstractHibernateTransactionRunnable<ReturnT>  {
	
	protected abstract Session getSession();
	
	protected abstract ReturnT hibernateAction();
	
	public ReturnT run() {
		ReturnT r = null;
		Transaction t = null;
		@Cleanup Session s = getSession();
		try {
			t = s.getTransaction();
			t.begin();
			r = hibernateAction();
			s.flush();
			t.commit();
		} catch (RuntimeException e) {
			if (t != null) {
				t.rollback();
			}
			e.printStackTrace();
			// TODO: log error
		}
		return r;
	}

}
