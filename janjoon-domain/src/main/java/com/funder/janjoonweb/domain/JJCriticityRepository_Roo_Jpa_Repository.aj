// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJCriticity;
import com.funder.janjoonweb.domain.JJCriticityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJCriticityRepository_Roo_Jpa_Repository {
    
    declare parents: JJCriticityRepository extends JpaRepository<JJCriticity, Long>;
    
    declare parents: JJCriticityRepository extends JpaSpecificationExecutor<JJCriticity>;
    
    declare @type: JJCriticityRepository: @Repository;
    
}
