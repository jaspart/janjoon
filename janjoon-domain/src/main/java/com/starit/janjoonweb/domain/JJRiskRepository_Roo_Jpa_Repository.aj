// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJRisk;
import com.starit.janjoonweb.domain.JJRiskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJRiskRepository_Roo_Jpa_Repository{

declare parents:JJRiskRepository extends JpaRepository<JJRisk,Long>;

declare parents:JJRiskRepository extends JpaSpecificationExecutor<JJRisk>;

declare @type:JJRiskRepository:@Repository;

}
