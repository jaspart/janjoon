// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJProduct;
import com.starit.janjoonweb.domain.JJProductService;
import java.util.List;

privileged aspect JJProductService_Roo_Service {
    
    public abstract long JJProductService.countAllJJProducts();    
    public abstract void JJProductService.deleteJJProduct(JJProduct JJProduct_);    
    public abstract JJProduct JJProductService.findJJProduct(Long id);    
    public abstract List<JJProduct> JJProductService.findAllJJProducts();    
    public abstract List<JJProduct> JJProductService.findJJProductEntries(int firstResult, int maxResults);    
    public abstract void JJProductService.saveJJProduct(JJProduct JJProduct_);    
    public abstract JJProduct JJProductService.updateJJProduct(JJProduct JJProduct_);    
}