// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJWorflow;
import com.starit.janjoonweb.domain.JJWorflowRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJWorflowRepository_Roo_Jpa_Repository {
    
    declare parents: JJWorflowRepository extends JpaRepository<JJWorflow, Long>;
    
    declare parents: JJWorflowRepository extends JpaSpecificationExecutor<JJWorflow>;
    
    declare @type: JJWorflowRepository: @Repository;
    
}
