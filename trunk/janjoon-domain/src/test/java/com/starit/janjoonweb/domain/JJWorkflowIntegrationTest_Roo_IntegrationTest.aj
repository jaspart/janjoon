// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJWorkflowDataOnDemand;
import com.starit.janjoonweb.domain.JJWorkflowIntegrationTest;
import com.starit.janjoonweb.domain.JJWorkflowRepository;
import com.starit.janjoonweb.domain.JJWorkflowService;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJWorkflowIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJWorkflowIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJWorkflowIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJWorkflowIntegrationTest: @Transactional;
    
    @Autowired
    JJWorkflowDataOnDemand JJWorkflowIntegrationTest.dod;
    
    @Autowired
    JJWorkflowService JJWorkflowIntegrationTest.jJWorkflowService;
    
    @Autowired
    JJWorkflowRepository JJWorkflowIntegrationTest.jJWorkflowRepository;
    
    @Test
    public void JJWorkflowIntegrationTest.testCountAllJJWorkflows() {
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", dod.getRandomJJWorkflow());
        long count = jJWorkflowService.countAllJJWorkflows();
        Assert.assertTrue("Counter for 'JJWorkflow' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testFindJJWorkflow() {
        JJWorkflow obj = dod.getRandomJJWorkflow();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to provide an identifier", id);
        obj = jJWorkflowService.findJJWorkflow(id);
        Assert.assertNotNull("Find method for 'JJWorkflow' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJWorkflow' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testFindAllJJWorkflows() {
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", dod.getRandomJJWorkflow());
        long count = jJWorkflowService.countAllJJWorkflows();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJWorkflow', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJWorkflow> result = jJWorkflowService.findAllJJWorkflows();
        Assert.assertNotNull("Find all method for 'JJWorkflow' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJWorkflow' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testFindJJWorkflowEntries() {
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", dod.getRandomJJWorkflow());
        long count = jJWorkflowService.countAllJJWorkflows();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJWorkflow> result = jJWorkflowService.findJJWorkflowEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJWorkflow' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJWorkflow' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testFlush() {
        JJWorkflow obj = dod.getRandomJJWorkflow();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to provide an identifier", id);
        obj = jJWorkflowService.findJJWorkflow(id);
        Assert.assertNotNull("Find method for 'JJWorkflow' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJWorkflow(obj);
        Integer currentVersion = obj.getVersion();
        jJWorkflowRepository.flush();
        Assert.assertTrue("Version for 'JJWorkflow' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testUpdateJJWorkflowUpdate() {
        JJWorkflow obj = dod.getRandomJJWorkflow();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to provide an identifier", id);
        obj = jJWorkflowService.findJJWorkflow(id);
        boolean modified =  dod.modifyJJWorkflow(obj);
        Integer currentVersion = obj.getVersion();
        JJWorkflow merged = jJWorkflowService.updateJJWorkflow(obj);
        jJWorkflowRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJWorkflow' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testSaveJJWorkflow() {
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", dod.getRandomJJWorkflow());
        JJWorkflow obj = dod.getNewTransientJJWorkflow(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJWorkflow' identifier to be null", obj.getId());
        try {
            jJWorkflowService.saveJJWorkflow(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJWorkflowRepository.flush();
        Assert.assertNotNull("Expected 'JJWorkflow' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJWorkflowIntegrationTest.testDeleteJJWorkflow() {
        JJWorkflow obj = dod.getRandomJJWorkflow();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJWorkflow' failed to provide an identifier", id);
        obj = jJWorkflowService.findJJWorkflow(id);
        jJWorkflowService.deleteJJWorkflow(obj);
        jJWorkflowRepository.flush();
        Assert.assertNull("Failed to remove 'JJWorkflow' with identifier '" + id + "'", jJWorkflowService.findJJWorkflow(id));
    }
    
}
