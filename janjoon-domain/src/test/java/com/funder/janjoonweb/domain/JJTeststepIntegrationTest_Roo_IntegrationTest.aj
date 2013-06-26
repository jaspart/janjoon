// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJTeststepDataOnDemand;
import com.funder.janjoonweb.domain.JJTeststepIntegrationTest;
import com.funder.janjoonweb.domain.JJTeststepRepository;
import com.funder.janjoonweb.domain.JJTeststepService;
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

privileged aspect JJTeststepIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJTeststepIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJTeststepIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJTeststepIntegrationTest: @Transactional;
    
    @Autowired
    JJTeststepDataOnDemand JJTeststepIntegrationTest.dod;
    
    @Autowired
    JJTeststepService JJTeststepIntegrationTest.jJTeststepService;
    
    @Autowired
    JJTeststepRepository JJTeststepIntegrationTest.jJTeststepRepository;
    
    @Test
    public void JJTeststepIntegrationTest.testCountAllJJTeststeps() {
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", dod.getRandomJJTeststep());
        long count = jJTeststepService.countAllJJTeststeps();
        Assert.assertTrue("Counter for 'JJTeststep' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJTeststepIntegrationTest.testFindJJTeststep() {
        JJTeststep obj = dod.getRandomJJTeststep();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to provide an identifier", id);
        obj = jJTeststepService.findJJTeststep(id);
        Assert.assertNotNull("Find method for 'JJTeststep' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJTeststep' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJTeststepIntegrationTest.testFindAllJJTeststeps() {
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", dod.getRandomJJTeststep());
        long count = jJTeststepService.countAllJJTeststeps();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJTeststep', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJTeststep> result = jJTeststepService.findAllJJTeststeps();
        Assert.assertNotNull("Find all method for 'JJTeststep' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJTeststep' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJTeststepIntegrationTest.testFindJJTeststepEntries() {
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", dod.getRandomJJTeststep());
        long count = jJTeststepService.countAllJJTeststeps();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJTeststep> result = jJTeststepService.findJJTeststepEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJTeststep' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJTeststep' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJTeststepIntegrationTest.testFlush() {
        JJTeststep obj = dod.getRandomJJTeststep();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to provide an identifier", id);
        obj = jJTeststepService.findJJTeststep(id);
        Assert.assertNotNull("Find method for 'JJTeststep' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJTeststep(obj);
        Integer currentVersion = obj.getVersion();
        jJTeststepRepository.flush();
        Assert.assertTrue("Version for 'JJTeststep' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJTeststepIntegrationTest.testUpdateJJTeststepUpdate() {
        JJTeststep obj = dod.getRandomJJTeststep();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to provide an identifier", id);
        obj = jJTeststepService.findJJTeststep(id);
        boolean modified =  dod.modifyJJTeststep(obj);
        Integer currentVersion = obj.getVersion();
        JJTeststep merged = (JJTeststep)jJTeststepService.updateJJTeststep(obj);
        jJTeststepRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJTeststep' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJTeststepIntegrationTest.testSaveJJTeststep() {
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", dod.getRandomJJTeststep());
        JJTeststep obj = dod.getNewTransientJJTeststep(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJTeststep' identifier to be null", obj.getId());
        try {
            jJTeststepService.saveJJTeststep(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJTeststepRepository.flush();
        Assert.assertNotNull("Expected 'JJTeststep' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJTeststepIntegrationTest.testDeleteJJTeststep() {
        JJTeststep obj = dod.getRandomJJTeststep();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJTeststep' failed to provide an identifier", id);
        obj = jJTeststepService.findJJTeststep(id);
        jJTeststepService.deleteJJTeststep(obj);
        jJTeststepRepository.flush();
        Assert.assertNull("Failed to remove 'JJTeststep' with identifier '" + id + "'", jJTeststepService.findJJTeststep(id));
    }
    
}
