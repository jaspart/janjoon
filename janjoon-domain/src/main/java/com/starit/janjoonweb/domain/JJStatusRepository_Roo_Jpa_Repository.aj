// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJStatus;
import com.starit.janjoonweb.domain.JJStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJStatusRepository_Roo_Jpa_Repository {
    
    declare parents: JJStatusRepository extends JpaRepository<JJStatus, Long>;
    
    declare parents: JJStatusRepository extends JpaSpecificationExecutor<JJStatus>;
    
    declare @type: JJStatusRepository: @Repository;
    
}