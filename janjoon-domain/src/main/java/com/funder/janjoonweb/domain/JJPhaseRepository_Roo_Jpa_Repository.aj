// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJPhase;
import com.funder.janjoonweb.domain.JJPhaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJPhaseRepository_Roo_Jpa_Repository {
    
    declare parents: JJPhaseRepository extends JpaRepository<JJPhase, Long>;
    
    declare parents: JJPhaseRepository extends JpaSpecificationExecutor<JJPhase>;
    
    declare @type: JJPhaseRepository: @Repository;
    
}
