// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJTeststep;
import com.starit.janjoonweb.domain.JJTeststepRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJTeststepRepository_Roo_Jpa_Repository {
    
    declare parents: JJTeststepRepository extends JpaRepository<JJTeststep, Long>;
    
    declare parents: JJTeststepRepository extends JpaSpecificationExecutor<JJTeststep>;
    
    declare @type: JJTeststepRepository: @Repository;
    
}
