package com.algaworks.algamoney.api.repository.launch;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.algaworks.algamoney.api.model.Launch;
import com.algaworks.algamoney.api.repository.filter.LaunchFilter;

import org.apache.commons.lang3.StringUtils;

public class LaunchRepositoryImpl implements LaunchRepositoryQuery{
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Launch> filter(LaunchFilter launchFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Launch> criteria = builder.createQuery(Launch.class);
		Root<Launch> root = criteria.from(Launch.class);
		
		Predicate[] predicate = createPredicates(builder, launchFilter, root);
		criteria.where(predicate);
		
		TypedQuery<Launch> query = manager.createQuery(criteria);
		return query.getResultList(); //if the list of predicates equal zero, return list of all the launch, else return list of predicates
	}
	
	private Predicate[] createPredicates(CriteriaBuilder builder, LaunchFilter launchFilter, Root<Launch> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		//where description like '%'+asaf'%'
		
		if(!StringUtils.isEmpty(launchFilter.getDescription())) {
			predicates.add(builder.like(
					builder.lower(root.get("description")), "%" + launchFilter.getDescription() + "%"));
		}
		
		if(launchFilter.getDueDateOf() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dueDate"), launchFilter.getDueDateOf()));
		}
		
		if(launchFilter.getDueDateUntil() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dueDate"), launchFilter.getDueDateUntil()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
