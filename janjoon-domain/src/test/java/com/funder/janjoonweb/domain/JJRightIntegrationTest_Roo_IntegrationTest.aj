// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJRightDataOnDemand;
import com.funder.janjoonweb.domain.JJRightIntegrationTest;
import com.funder.janjoonweb.domain.JJRightRepository;
import com.funder.janjoonweb.domain.JJRightService;
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

privileged aspect JJRightIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJRightIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJRightIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJRightIntegrationTest: @Transactional;
    
    @Autowired
    JJRightDataOnDemand JJRightIntegrationTest.dod;
    
    @Autowired
    JJRightService JJRightIntegrationTest.jJRightService;
    
    @Autowired
    JJRightRepository JJRightIntegrationTest.jJRightRepository;
    
    @Test
    public void JJRightIntegrationTest.testCountAllJJRights() {
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", dod.getRandomJJRight());
        long count = jJRightService.countAllJJRights();
        Assert.assertTrue("Counter for 'JJRight' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJRightIntegrationTest.testFindJJRight() {
        JJRight obj = dod.getRandomJJRight();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to provide an identifier", id);
        obj = jJRightService.findJJRight(id);
        Assert.assertNotNull("Find method for 'JJRight' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJRight' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJRightIntegrationTest.testFindAllJJRights() {
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", dod.getRandomJJRight());
        long count = jJRightService.countAllJJRights();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJRight', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJRight> result = jJRightService.findAllJJRights();
        Assert.assertNotNull("Find all method for 'JJRight' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJRight' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJRightIntegrationTest.testFindJJRightEntries() {
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", dod.getRandomJJRight());
        long count = jJRightService.countAllJJRights();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJRight> result = jJRightService.findJJRightEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJRight' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJRight' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJRightIntegrationTest.testFlush() {
        JJRight obj = dod.getRandomJJRight();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to provide an identifier", id);
        obj = jJRightService.findJJRight(id);
        Assert.assertNotNull("Find method for 'JJRight' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJRight(obj);
        Integer currentVersion = obj.getVersion();
        jJRightRepository.flush();
        Assert.assertTrue("Version for 'JJRight' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJRightIntegrationTest.testUpdateJJRightUpdate() {
        JJRight obj = dod.getRandomJJRight();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to provide an identifier", id);
        obj = jJRightService.findJJRight(id);
        boolean modified =  dod.modifyJJRight(obj);
        Integer currentVersion = obj.getVersion();
        JJRight merged = (JJRight)jJRightService.updateJJRight(obj);
        jJRightRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJRight' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJRightIntegrationTest.testSaveJJRight() {
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", dod.getRandomJJRight());
        JJRight obj = dod.getNewTransientJJRight(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJRight' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJRight' identifier to be null", obj.getId());
        try {
            jJRightService.saveJJRight(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJRightRepository.flush();
        Assert.assertNotNull("Expected 'JJRight' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJRightIntegrationTest.testDeleteJJRight() {
        JJRight obj = dod.getRandomJJRight();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJRight' failed to provide an identifier", id);
        obj = jJRightService.findJJRight(id);
        jJRightService.deleteJJRight(obj);
        jJRightRepository.flush();
        Assert.assertNull("Failed to remove 'JJRight' with identifier '" + id + "'", jJRightService.findJJRight(id));
    }
    
}
