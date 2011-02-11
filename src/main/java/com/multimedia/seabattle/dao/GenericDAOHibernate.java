/*******************************************************************************
 * Copyright (c) 2011 demchuck.dima@gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     demchuck.dima@gmail.com - initial API and implementation
 ******************************************************************************/

package com.multimedia.seabattle.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.multimedia.seabattle.dao.util.HQLPartGenerator;

/**
 *
 * @param <T>
 * @author demchuck.dima@gmail.com
 */
public class GenericDAOHibernate<T, ID extends Serializable> implements IGenericDAO<T, ID> {

	protected final Logger logger = Logger.getLogger(getClass());

	protected SessionFactory sessionFactory;

	protected Class<T> persistentClass;

	protected String entityName;

	protected String entityAlias = "this";

	public void init(){
		StringBuilder sb = new StringBuilder();
		common.utils.MiscUtils.checkNotNull(sessionFactory, "sessionFactory", sb);
		common.utils.MiscUtils.checkNotNull(persistentClass, "persistentClass", sb);
		common.utils.MiscUtils.checkNotNull(entityName, "entityName", sb);
		if (sb.length()>0){
			throw new NullPointerException(sb.toString());
		}
	}

	public GenericDAOHibernate(java.lang.String entityName) {
		this.entityName = entityName;
	}


	public GenericDAOHibernate(java.lang.String entityName, java.lang.String persistentClass) {
		this(entityName);
		try {
			this.persistentClass = (Class<T>) Class.forName(persistentClass);
		} catch (ClassNotFoundException ex) {
			logger.error("can't find a persistentClass "+persistentClass, ex);
		}
	}

	public Class<T> getPersistentClass() {return persistentClass;}

	public String getEntityName() {return entityName;}

	public SessionFactory getSessionFactory() {
		if (logger.isDebugEnabled()){
			logger.debug("session = "+Integer.toHexString(sessionFactory.getCurrentSession().hashCode()));
		}
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

		//TODO mb inject this every where
	/**
	 * appends an entity name with its alias to the sb
	 * @param sb string builder where to append
	 * @return sb
	 */
	protected StringBuilder appendEntityNameWithAlias(StringBuilder sb){
		return sb.append(entityName).append(" ").append(entityAlias);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int deleteById(ID id){
		StringBuilder hql = new StringBuilder("delete ");
		hql.append(entityName);
		hql.append(" where id = :id");
		//logger.debug("Entity " + entityName + " has been just deleted");
		try{
			return getSessionFactory().getCurrentSession().createQuery(hql.toString()).setParameter("id", id).executeUpdate();
		} catch (org.hibernate.exception.ConstraintViolationException e){
			return -1;
		}
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertyValue(String propertyName, Object propertyValue) {
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereColumnValue(propertyName, propertyValue, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());
		if (propertyName!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertyValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String propertyWhere, Object propertyValue,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		appendEntityNameWithAlias(baseHQL);
		HQLPartGenerator.getWhereColumnValue(propertyWhere, propertyValue, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyWhere!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		if (propertyNames!=null&&propertyAliases!=null&&propertyNames.length>0&&propertyNames.length==propertyAliases.length){
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		}
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertyValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String propertyWhere, Object propertyValue, String relation,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereColumnValueRelation(propertyWhere, propertyValue, relation, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyWhere!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		if (propertyNames!=null&&propertyAliases!=null&&propertyNames.length>0&&propertyNames.length==propertyAliases.length){
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		}
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[] propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		appendEntityNameWithAlias(baseHQL);
		HQLPartGenerator.getWhereManyColumns(propertiesWhere, propertyValues, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null&&propertyValues!=null)
			for (int i=0;i<propertiesWhere.length;i++){
				if (propertyValues[i]!=null)
					q = q.setParameter("prop"+String.valueOf(i), propertyValues[i]);
			}
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertiesValuePortionOrdered(List<String> propertyNames, List<String> propertyAliases,
			List<String> propertiesWhere, List<Object> propertyValues,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames, propertyAliases, baseHQL);
		baseHQL.append(" from ");
		appendEntityNameWithAlias(baseHQL);
		HQLPartGenerator.getWhereManyColumns(propertiesWhere, propertyValues, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null&&propertyValues!=null)
			for (int i=0;i<propertiesWhere.size();i++){
				if (propertyValues.get(i)!=null)
					q = q.setParameter("prop"+String.valueOf(i), propertyValues.get(i));
			}
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.size()==propertyNames.size())
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[] propertyValues, String[] relations,
			int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereManyColumnsRelations(propertiesWhere, propertyValues, relations, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null&&propertyValues!=null)
			for (int i=0;i<propertiesWhere.length;i++){
				if (propertyValues[i]!=null)
					//logger.info("prop"+String.valueOf(i)+"="+propertyValues[i]);
					q = q.setParameter("prop"+String.valueOf(i), propertyValues[i]);
			}
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getPortion(int firstResult, int resultCount){
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);

		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getAllShortOrdered(String[] properties, String[] orderBy, String[] orderHow) {
		List<T> l = null;
		//selecting using queries with alieses and result transform
		{
			//long l1 = System.currentTimeMillis();
			StringBuilder sb = new StringBuilder();
			HQLPartGenerator.getValuesListWithAliases(properties,properties,sb);
			sb.append(" from ");
			sb.append(entityName);
			HQLPartGenerator.getOrderBy(orderBy, orderHow, sb);

			Session s = getSessionFactory().getCurrentSession();
			Query q = s.createQuery(sb.toString());
			if (properties!=null)
				q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
				//q = q.setResultTransformer(new MyAliasToBeamTransformer(persistentClass));

			l = q.list();
			//long l2 = System.currentTimeMillis() - l1;
			//System.out.println("1-st = "+l2);
		}
		
		return l;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updateCollection(Collection<T> entities, String... propertyNames) {
		if (entities==null||entities.isEmpty())
			return 0;
		int rez = 0;
		Session sess = this.getSessionFactory().getCurrentSession();
		if (propertyNames==null||propertyNames.length==0){
			//Iterator<T> i;
			for (T item:entities){
				sess.saveOrUpdate(entityName, item);
				rez++;
			}
		} else {
			StringBuilder hql = new StringBuilder("update ");
			hql.append(entityName);
			HQLPartGenerator.getValuesListForUpdateProperties(propertyNames, hql);
			hql.append(" where id = :id");
			for (T item:entities){
				//TODO: add id to method parameters and mb test item on null somewhere else
				if (item!=null){
					sess.createQuery(hql.toString()).setProperties(item).executeUpdate();
				}
				rez++;
			}
		}
		return rez;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updatePropertyById(String propertyName, ID id) {
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		hql.append(" set ").append(propertyName);
		hql.append(" where id=?");
		return this.getSessionFactory().getCurrentSession().createQuery(hql.toString()).setParameter(0, id).executeUpdate();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updateObjectArrayShortById(String[] propertyNames, ID[] idValues, Object[]... propertyValues) {
		if (propertyNames==null||idValues==null||propertyValues==null||
				propertyNames.length==0||idValues.length==0||propertyValues.length!=propertyNames.length){
			return -1;
		}
		
		int rez = 0;
		//actually creating hql
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdate(propertyNames, hql);
		hql.append(" where id = :id");
		Query q = null;
		Session sess = this.getSessionFactory().getCurrentSession();
		for (int i=0;i<idValues.length;i++){
			if (idValues[i]!=null){
				q = sess.createQuery(hql.toString());
				//appending values
				q = q.setParameter("id", idValues[i]);
				//log.fine("id="+idValues[i]);
				for (int j=0;j<propertyNames.length;j++){
					q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX+j, propertyValues[j][i]);
					//log.fine(propertyNames[j]+"="+propertyValues[j][i]);
				}
				rez+= q.executeUpdate();
			}
		}

		return rez;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updateObjectArrayShortByProperty(String[] propertyNames, Object[] propertyValues, String property, Object[] values) {
		if (propertyNames==null||values==null||propertyValues==null||
				propertyNames.length==0||values.length==0||propertyValues.length!=propertyNames.length){
			return -1;
		}

		int rez = 0;
		//actually creating hql
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdate(propertyNames, hql);
		hql.append(" where ");
		hql.append(property);
		hql.append(" in (:property)");

		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString()).setParameterList("property", values);
		for (int i=0;i<propertyNames.length;i++){
			q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX+i, propertyValues[i]);
		}

		rez+= q.executeUpdate();

		return rez;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updateObjectArrayShortByProperty(String[] propertyNames, Object[] propertyValues,
			String[] property, Object[] values)
	{
		if (propertyNames==null||values==null||propertyValues==null||
				propertyNames.length==0||values.length==0||propertyValues.length!=propertyNames.length){
			return -1;
		}

		int rez = 0;
		//actually creating hql
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdateNumbers(propertyNames, hql);
		HQLPartGenerator.getWhereManyColumns(property, values, hql);

		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString());
		//appending values
		for (int j=0;j<propertyNames.length;j++){
			q = q.setParameter(j, propertyValues[j]);
		}

		HQLPartGenerator.appendPropertiesValue(values, q);

		rez+= q.executeUpdate();

		return rez;
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updatePropertiesById(String[] propertyNames, Object[] propertyValues, ID id) {
		if (propertyNames==null||id==null||propertyValues==null||
				propertyNames.length==0||propertyValues.length!=propertyNames.length){
			return -1;
			//throw new NullPointerException("updateObjectArrayShortById: One of arguments is null of has 0 length or propertyNames length not eq to propertyValues length");
		}

		//actually creating hql----------------------------------------
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdateNumbers(propertyNames, hql);
		hql.append(" where id = :id");
		//-------------------------------------------------------------
		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString()).setParameter("id", id);
		//appending values
		for (int j=0;j<propertyNames.length;j++){
			q = q.setParameter(j, propertyValues[j]);
		}
		return q.executeUpdate();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updatePropertiesById(String[] propertyNames, Object[] propertyValues, ID[] id) {
		if (propertyNames==null||id==null||propertyValues==null||
				propertyNames.length==0||propertyValues.length!=propertyNames.length){
			return -1;
			//throw new NullPointerException("updateObjectArrayShortById: One of arguments is null of has 0 length or propertyNames length not eq to propertyValues length");
		}

		//actually creating hql----------------------------------------
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		HQLPartGenerator.getValuesListForUpdateNumbers(propertyNames, hql);
		hql.append(" where id in (:id)");
		//-------------------------------------------------------------
		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString()).setParameterList("id", id);
		//appending values
		for (int j=0;j<propertyNames.length;j++){
			q = q.setParameter(j, propertyValues[j]);
		}
		return q.executeUpdate();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int updatePropertyById(String propertyName, Object value, ID id) {
		if (propertyName==null||id==null||value==null){
			return -1;
			//throw new NullPointerException("updateObjectArrayShortById: One of arguments is null of has 0 length or propertyNames length not eq to propertyValues length");
		}

		//actually creating hql----------------------------------------
		StringBuilder hql = new StringBuilder("update ");
		hql.append(entityName);
		hql.append(" set ");
		hql.append(propertyName);
		hql.append(" = ");
		hql.append(propertyName);
		hql.append(" + :prop where id = :id");
		//-------------------------------------------------------------
		Session sess = this.getSessionFactory().getCurrentSession();

		Query q = sess.createQuery(hql.toString()).setParameter("id", id).setParameter("prop", value);
		return q.executeUpdate();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertiesValuesPortionOrdered(String[] propertyNames, String[] propertyAliases,
			String[] propertiesWhere, Object[][] propertyValues, int firstResult, int resultCount,
			String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereManyColumnsManyValues(propertiesWhere, propertyValues, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (propertiesWhere!=null)
			q = HQLPartGenerator.appendWherePropertiesValues(propertyValues, q);
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public Long getRowCount(String propertyName, Object propertyValue) {
		StringBuilder hql = new StringBuilder("select count(*) from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propertyName, propertyValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		if (propertyName!=null&&propertyValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue);
		return (Long)q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Long getRowCount(String propertyName, List<Object> propertyValue) {
		StringBuilder hql = new StringBuilder("select count(*) from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValues(propertyName, propertyValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendWherePropertiesValue(propertyValue, q);

		return (Long)q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Long getRowCount(String[] propertyName, Object[] propertyValue) {
		StringBuilder hql = new StringBuilder("select count(*) from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereManyColumns(propertyName, propertyValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendPropertiesValue(propertyValue, q);
	
		return (Long)q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Long getRowCount(List<String> propertyName, List<Object> propertyValue) {
		StringBuilder hql = new StringBuilder("select count(*) from ");
		hql.append(entityName).append(" this");
		HQLPartGenerator.getWhereManyColumns(propertyName, propertyValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendPropertiesValue(propertyValue, q);

		return (Long)q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Long getRowCount(String[] propertyName, Object[][] propertyValue) {
		StringBuilder hql = new StringBuilder("select count(*) from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereManyColumnsManyValues(propertyName, propertyValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q = HQLPartGenerator.appendWherePropertiesValues(propertyValue, q);

		return (Long)q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Object getSinglePropertyU(String property, String propName, Object propValue){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propName, propValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		if (propName!=null&&propValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propValue);

		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propName, propValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q.setFirstResult(number);
		q.setMaxResults(1);
		if (propName!=null&&propValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propValue);

		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Object getSinglePropertyU(String property, String propName, Object propValue, int number, String[] orderBy, String[] orderHow){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propName, propValue, hql);
        HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q.setFirstResult(number);
		q.setMaxResults(1);
		if (propName!=null&&propValue!=null) q = q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propValue);

		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Object getSinglePropertyU(String property, String[] propName, Object[] propValue, int number, String[] orderBy, String[] orderHow){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		HQLPartGenerator.getWhereManyColumns(propName, propValue, hql);
        HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q.setFirstResult(number);
		q.setMaxResults(1);
		q = HQLPartGenerator.appendPropertiesValue(propValue, q);

		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public Object getSinglePropertyU(String property){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());

		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public List getSinglePropertyOrderRand(String property, String whereName, Object whereValue, int first, int max){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
		if (whereName!=null){
			hql.append(" where ");
			hql.append(whereName);
			if (whereValue==null){
				hql.append(" is null ");
			}else{
				hql.append(" = :prop ");
			}
		}
		hql.append(" order by rand()");
		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		if (max>0) q = q.setMaxResults(max);
		if (first>0) q = q.setFirstResult(first);
		if (whereName!=null&&whereValue!=null) q = q.setParameter("prop", whereValue);
		return q.list();
	}

	@Transactional(readOnly = true)
    public List getSingleProperty(String property, String[] whereName, Object[] whereValue, int first, int max, String[] orderBy, String[] orderHow){
		StringBuilder hql = new StringBuilder("select ");
		hql.append(property);
		hql.append(" from ");
		hql.append(entityName);
        HQLPartGenerator.getWhereManyColumns(whereName, whereValue, hql);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		if (max>0) q = q.setMaxResults(max);
		if (first>0) q = q.setFirstResult(first);
		q = HQLPartGenerator.appendPropertiesValue(whereValue, q);
		return q.list();
    }

	@Transactional(readOnly = true)
	public void deattach(T obj){getSessionFactory().getCurrentSession().evict(obj);}

	@Transactional(readOnly = true)
	public T getById(ID id) {return (T) getSessionFactory().getCurrentSession().get(entityName, id);}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void makePersistent(T entity) {getSessionFactory().getCurrentSession().saveOrUpdate(entityName, entity);}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int update(T entity, String... names) {
		Session sess = this.getSessionFactory().getCurrentSession();
		if (names==null||names.length==0){
			sess.saveOrUpdate(entityName, entity);
			return 1;
		} else {
			StringBuilder hql = new StringBuilder("update ");
			hql.append(entityName);
			HQLPartGenerator.getValuesListForUpdateProperties(names, hql);
			hql.append(" where id = :id");
			//TODO: add id to method parameters and mb test item on null somewhere else
			return sess.createQuery(hql.toString()).setProperties(entity).executeUpdate();
		}
	}


	@Transactional(readOnly = true)
	public void merge(T entity) {getSessionFactory().getCurrentSession().merge(entityName, entity);}

	@Transactional(readOnly = true)
	public void refresh(T entity) {getSessionFactory().getCurrentSession().refresh(entity);}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int deleteByPropertyValue(String propertyName, Object propertyValue) {
		if (propertyName==null) return -1;
		StringBuilder hql = new StringBuilder("delete ");
		hql.append(entityName);
		HQLPartGenerator.getWhereColumnValue(propertyName, propertyValue, hql);

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		return q.setParameter(HQLPartGenerator.PROP_NAME_PREFIX, propertyValue).executeUpdate();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public int deleteById(ID[] id) {
		StringBuilder hql = new StringBuilder("delete ");
		hql.append(entityName);
		hql.append(" where id in (:idList)");

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		return q.setParameterList("idList", id).executeUpdate();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertyValues(String[] properties, String propertyName, Object[] propertyValues) {
		StringBuilder hql = new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(properties, properties, hql);
		hql.append(" from ");
		hql.append(entityName);
		hql.append(" where ");
		hql.append(propertyName);
		hql.append(" in (:idList)");

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q = q.setParameterList("idList", propertyValues);
		if (properties!=null) q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertyValues(String[] properties, String propertyName, List<Object> propertyValues) {
		if (propertyName==null||propertyValues==null||propertyValues.isEmpty())
			return null;
		
		StringBuilder hql = new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(properties, properties, hql);
		hql.append(" from ");
		hql.append(entityName);
		hql.append(" where ");
		hql.append(propertyName);
		hql.append(" in (:idList)");

		Query q = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		q = q.setParameterList("idList", propertyValues);
		if (properties!=null) q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public Object getRowNumber(Object[] values, String[] orderBy, String[] orderHow,
			String[] propertyName, Object[] propertyValue)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append("select count(*)+1 from ");
		baseHQL.append(this.entityName);
		if (orderBy!=null&&values!=null&&orderHow!=null){
			if (propertyName!=null&&propertyValue!=null){
				HQLPartGenerator.getWhereManyColumns(propertyName, propertyValue, baseHQL);
				baseHQL.append(" and (");
			} else {
				baseHQL.append(" where (");
			}
			for (int i=0;i<orderBy.length;i++){
				for (int j=0;j<i;j++){
					baseHQL.append(orderBy[j]);
					if (values[j]==null){
						baseHQL.append(" is null");
					}else{
						baseHQL.append(" = :p");
						baseHQL.append(j);
					}
				}
				baseHQL.append(orderBy[i]);
				if ("desc".equalsIgnoreCase(orderHow[i])){
					if (values[i]==null){
						baseHQL.append(" > null");
					}else{
						baseHQL.append(" > :p");
						baseHQL.append(i);
					}
				} else {
					if (values[i]==null){
						baseHQL.append(" < null");
					}else{
						baseHQL.append(" < :p");
						baseHQL.append(i);
					}
				}
			}
			baseHQL.append(")");
		}
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		if (values!=null&&orderBy!=null)
			for (int i=0;i<values.length;i++)
				if (values[i]!=null)
					q = q.setParameter("p"+i, values[i]);
		q = HQLPartGenerator.appendPropertiesValue(propertyValue, q);

		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
    public void restartTransaction(){
        getSessionFactory().getCurrentSession().getTransaction().commit();
        getSessionFactory().getCurrentSession().flush();
        getSessionFactory().getCurrentSession().beginTransaction();
    }

	@Transactional(readOnly = true)
	public void flush(){
		getSessionFactory().getCurrentSession().flush();
	}

	@Transactional(readOnly = true)
	public ScrollableResults getScrollableResults(String property, String whereName, Object whereValue, String[] orderBy, String[] orderHow){
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append("select ");
		baseHQL.append(property);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		if (whereName!=null){
			baseHQL.append(" where ");
			baseHQL.append(whereName);
			if (whereValue==null){
				baseHQL.append(" is null");
			}else{
				baseHQL.append("= :param");
			}
		}
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		Query q = getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());
		if (whereName!=null&&whereValue!=null) q = q.setParameter("param", whereValue);
		return q.scroll();
	}

	@Transactional(readOnly = true)
	public List<T> getByPropertiesValuePortionOrdered(String[] propertyNames, String[] propertyAliases, String[] propertiesWhere, String[][] relations, Object[][] propertyValues, int firstResult, int resultCount, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		HQLPartGenerator.getValuesListWithAliases(propertyNames,propertyAliases, baseHQL);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereManyColumnsManyValuesRelations(propertiesWhere, relations, propertyValues, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		HQLPartGenerator.appendWhereManyColumnsManyValuesRelations(propertyValues, q);
		if (firstResult>0) q = q.setFirstResult(firstResult);
		if (resultCount>0) q = q.setMaxResults(resultCount);
		if (propertyNames!=null&&propertyAliases!=null&&propertyAliases.length==propertyNames.length)
			q = q.setResultTransformer(new AliasToBeanResultTransformer(persistentClass));
		return q.list();
	}

	@Transactional(readOnly = true)
	public Object getSinglePropertyU(String property, String[] propName, String[][] relations, Object[][] propValue, int number, String[] orderBy, String[] orderHow)
	{
		//forming HQL-----------------------------------------------------------
		StringBuilder baseHQL=new StringBuilder();
		baseHQL.append("select ");
		baseHQL.append(property);
		baseHQL.append(" from ");
		baseHQL.append(this.entityName);
		HQLPartGenerator.getWhereManyColumnsManyValuesRelations(propName, relations, propValue, baseHQL);
		HQLPartGenerator.getOrderBy(orderBy, orderHow, baseHQL);
		//----------------------------------------------------------------------
		Query q = this.getSessionFactory().getCurrentSession().createQuery(baseHQL.toString());

		HQLPartGenerator.appendWhereManyColumnsManyValuesRelations(propValue, q);
		q.setFirstResult(number);
		q.setMaxResults(1);
		return q.uniqueResult();
	}

	@Transactional(readOnly = true)
	public void enableFilter(String name, String[] param_names, Object[] param_values){
		Filter f = this.getSessionFactory().getCurrentSession().enableFilter(name);
		if (param_names==null||param_values==null)
			return;
		for (int i=0;i<param_names.length;i++){
			f = f.setParameter(param_names[i], param_values[i]);
		}
	}

	@Transactional(readOnly = true)
	public void disableFilter(String name){
		this.getSessionFactory().getCurrentSession().disableFilter(name);
	}

}
