// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJCompany;
import com.starit.janjoonweb.domain.JJCompanyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJCompanyRepository_Roo_Jpa_Repository {
    
    declare parents: JJCompanyRepository extends JpaRepository<JJCompany, Long>;
    
    declare parents: JJCompanyRepository extends JpaSpecificationExecutor<JJCompany>;
    
    declare @type: JJCompanyRepository: @Repository;
    
}
