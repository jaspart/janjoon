// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJPhaseDataOnDemand;
import com.starit.janjoonweb.domain.JJPhaseIntegrationTest;
import com.starit.janjoonweb.domain.JJPhaseRepository;
import com.starit.janjoonweb.domain.JJPhaseService;
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

privileged aspect JJPhaseIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJPhaseIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJPhaseIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJPhaseIntegrationTest: @Transactional;
    
    @Autowired
    JJPhaseDataOnDemand JJPhaseIntegrationTest.dod;
    
    @Autowired
    JJPhaseService JJPhaseIntegrationTest.jJPhaseService;
    
    @Autowired
    JJPhaseRepository JJPhaseIntegrationTest.jJPhaseRepository;
    
    @Test
    public void JJPhaseIntegrationTest.testCountAllJJPhases() {
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", dod.getRandomJJPhase());
        long count = jJPhaseService.countAllJJPhases();
        Assert.assertTrue("Counter for 'JJPhase' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJPhaseIntegrationTest.testFindJJPhase() {
        JJPhase obj = dod.getRandomJJPhase();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to provide an identifier", id);
        obj = jJPhaseService.findJJPhase(id);
        Assert.assertNotNull("Find method for 'JJPhase' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJPhase' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJPhaseIntegrationTest.testFindAllJJPhases() {
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", dod.getRandomJJPhase());
        long count = jJPhaseService.countAllJJPhases();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJPhase', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJPhase> result = jJPhaseService.findAllJJPhases();
        Assert.assertNotNull("Find all method for 'JJPhase' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJPhase' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJPhaseIntegrationTest.testFindJJPhaseEntries() {
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", dod.getRandomJJPhase());
        long count = jJPhaseService.countAllJJPhases();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJPhase> result = jJPhaseService.findJJPhaseEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJPhase' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJPhase' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJPhaseIntegrationTest.testFlush() {
        JJPhase obj = dod.getRandomJJPhase();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to provide an identifier", id);
        obj = jJPhaseService.findJJPhase(id);
        Assert.assertNotNull("Find method for 'JJPhase' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJPhase(obj);
        Integer currentVersion = obj.getVersion();
        jJPhaseRepository.flush();
        Assert.assertTrue("Version for 'JJPhase' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJPhaseIntegrationTest.testUpdateJJPhaseUpdate() {
        JJPhase obj = dod.getRandomJJPhase();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to provide an identifier", id);
        obj = jJPhaseService.findJJPhase(id);
        boolean modified =  dod.modifyJJPhase(obj);
        Integer currentVersion = obj.getVersion();
        JJPhase merged = (JJPhase)jJPhaseService.updateJJPhase(obj);
        jJPhaseRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJPhase' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJPhaseIntegrationTest.testSaveJJPhase() {
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", dod.getRandomJJPhase());
        JJPhase obj = dod.getNewTransientJJPhase(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJPhase' identifier to be null", obj.getId());
        try {
            jJPhaseService.saveJJPhase(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJPhaseRepository.flush();
        Assert.assertNotNull("Expected 'JJPhase' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJPhaseIntegrationTest.testDeleteJJPhase() {
        JJPhase obj = dod.getRandomJJPhase();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJPhase' failed to provide an identifier", id);
        obj = jJPhaseService.findJJPhase(id);
        jJPhaseService.deleteJJPhase(obj);
        jJPhaseRepository.flush();
        Assert.assertNull("Failed to remove 'JJPhase' with identifier '" + id + "'", jJPhaseService.findJJPhase(id));
    }
    
}