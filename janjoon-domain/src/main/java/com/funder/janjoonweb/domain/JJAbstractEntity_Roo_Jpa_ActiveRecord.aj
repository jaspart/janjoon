// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJAbstractEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJAbstractEntity_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager JJAbstractEntity.entityManager;
    
    public static final List<String> JJAbstractEntity.fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "description", "creationDate", "createdBy", "updatedDate", "updatedBy", "enabled");
    
    public static final EntityManager JJAbstractEntity.entityManager() {
        EntityManager em = new JJAbstractEntity() {
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long JJAbstractEntity.countJJAbstractEntitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM JJAbstractEntity o", Long.class).getSingleResult();
    }
    
    public static List<JJAbstractEntity> JJAbstractEntity.findAllJJAbstractEntitys() {
        return entityManager().createQuery("SELECT o FROM JJAbstractEntity o", JJAbstractEntity.class).getResultList();
    }
    
    public static List<JJAbstractEntity> JJAbstractEntity.findAllJJAbstractEntitys(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM JJAbstractEntity o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, JJAbstractEntity.class).getResultList();
    }
    
    public static JJAbstractEntity JJAbstractEntity.findJJAbstractEntity(Long id) {
        if (id == null) return null;
        return entityManager().find(JJAbstractEntity.class, id);
    }
    
    public static List<JJAbstractEntity> JJAbstractEntity.findJJAbstractEntityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM JJAbstractEntity o", JJAbstractEntity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<JJAbstractEntity> JJAbstractEntity.findJJAbstractEntityEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM JJAbstractEntity o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, JJAbstractEntity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void JJAbstractEntity.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void JJAbstractEntity.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            JJAbstractEntity attached = JJAbstractEntity.findJJAbstractEntity(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void JJAbstractEntity.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void JJAbstractEntity.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public JJAbstractEntity JJAbstractEntity.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        JJAbstractEntity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
