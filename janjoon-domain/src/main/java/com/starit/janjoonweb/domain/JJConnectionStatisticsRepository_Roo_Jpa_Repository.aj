// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJConnectionStatistics;
import com.starit.janjoonweb.domain.JJConnectionStatisticsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

privileged aspect JJConnectionStatisticsRepository_Roo_Jpa_Repository{

declare parents:JJConnectionStatisticsRepository extends JpaRepository<JJConnectionStatistics,Long>;

declare parents:JJConnectionStatisticsRepository extends JpaSpecificationExecutor<JJConnectionStatistics>;

declare @type:JJConnectionStatisticsRepository:@Repository;

}
