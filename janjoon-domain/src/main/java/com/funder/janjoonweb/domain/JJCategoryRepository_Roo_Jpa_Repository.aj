// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJCategory;
import com.funder.janjoonweb.domain.JJCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJCategoryRepository_Roo_Jpa_Repository {
    
    declare parents: JJCategoryRepository extends JpaRepository<JJCategory, Long>;
    
    declare parents: JJCategoryRepository extends JpaSpecificationExecutor<JJCategory>;
    
    declare @type: JJCategoryRepository: @Repository;
    
}
