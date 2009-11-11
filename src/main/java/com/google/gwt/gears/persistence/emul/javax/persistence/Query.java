package javax.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface Query
{

   public List getResultList();

   public Object getSingleResult();

   public int executeUpdate();

   public Query setMaxResults(int maxResult);

   public Query setFirstResult(int startPosition);

   public Query setHint(String hintName, Object value);

   public Query setParameter(String name, Object value);

   public Query setParameter(String name, Date value, TemporalType temporalType);

   public Query setParameter(String name, Calendar value, TemporalType temporalType);

   public Query setParameter(int position, Object value);

   public Query setParameter(int position, Date value, TemporalType temporalType);

   public Query setParameter(int position, Calendar value, TemporalType temporalType);

   public Query setFlushMode(FlushModeType flushMode);

}