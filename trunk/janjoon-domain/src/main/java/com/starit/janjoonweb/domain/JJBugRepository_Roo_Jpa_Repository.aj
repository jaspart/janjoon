// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJBug;
import com.starit.janjoonweb.domain.JJBugRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJBugRepository_Roo_Jpa_Repository {
    
    declare parents: JJBugRepository extends JpaRepository<JJBug, Long>;
    
    declare parents: JJBugRepository extends JpaSpecificationExecutor<JJBug>;
    
    declare @type: JJBugRepository: @Repository;
    
}
