// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJRequirement;
import com.starit.janjoonweb.domain.JJRequirementRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJRequirementRepository_Roo_Jpa_Repository{

declare parents:JJRequirementRepository extends JpaRepository<JJRequirement,Long>;

declare parents:JJRequirementRepository extends JpaSpecificationExecutor<JJRequirement>;

declare @type:JJRequirementRepository:@Repository;

}
