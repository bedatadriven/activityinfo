package org.activityinfo.server.database.hibernate;

import java.util.Map;

import javax.persistence.EntityManager;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;

import static com.google.common.base.Preconditions.checkState;


public class HibernateSessionScope implements Scope {

	private final ThreadLocal<Map<Key<?>, Object>> values
		= new ThreadLocal<Map<Key<?>, Object>>();

	public void enter() {
		checkState(values.get() == null, "A hibernate session block is already in progress");
		values.set(Maps.<Key<?>, Object>newHashMap());
	}

	public void exit() {
		checkState(values.get() != null, "No hibernate session block in progress");
		
		// close session
		if(values.get().containsKey(Key.get(EntityManager.class))) {
			EntityManager em = (EntityManager) values.get().get(Key.get(EntityManager.class));
			em.close();
		}
		
		values.remove();
	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
		return new Provider<T>() {
			public T get() {
				Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);

				@SuppressWarnings("unchecked")
				T current = (T) scopedObjects.get(key);
				if (current == null && !scopedObjects.containsKey(key)) {
					current = unscoped.get();
					scopedObjects.put(key, current);
				}
				return current;
			}
		};
	}

	private <T> Map<Key<?>, Object> getScopedObjectMap(Key<T> key) {
		Map<Key<?>, Object> scopedObjects = values.get();
		if (scopedObjects == null) {
			throw new OutOfScopeException("Cannot access " + key
					+ " outside of a hibernate session block");
		}
		return scopedObjects;
	}	
}
