// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit
// any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJPermission;
import com.starit.janjoonweb.domain.JJPermissionService;
import java.util.List;

privileged aspect JJPermissionService_Roo_Service{

public abstract long JJPermissionService.countAllJJPermissions();public abstract void JJPermissionService.deleteJJPermission(JJPermission JJPermission_);public abstract JJPermission JJPermissionService.findJJPermission(Long id);public abstract List<JJPermission>JJPermissionService.findAllJJPermissions();public abstract List<JJPermission>JJPermissionService.findJJPermissionEntries(int firstResult,int maxResults);public abstract void JJPermissionService.saveJJPermission(JJPermission JJPermission_);public abstract JJPermission JJPermissionService.updateJJPermission(JJPermission JJPermission_);}
