// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJConfiguration;
import com.starit.janjoonweb.domain.JJConfigurationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJConfigurationRepository_Roo_Jpa_Repository {
    
    declare parents: JJConfigurationRepository extends JpaRepository<JJConfiguration, Long>;
    
    declare parents: JJConfigurationRepository extends JpaSpecificationExecutor<JJConfiguration>;
    
    declare @type: JJConfigurationRepository: @Repository;
    
}
