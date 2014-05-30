// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJImputationDataOnDemand;
import com.starit.janjoonweb.domain.JJImputationIntegrationTest;
import com.starit.janjoonweb.domain.JJImputationRepository;
import com.starit.janjoonweb.domain.JJImputationService;
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

privileged aspect JJImputationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJImputationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJImputationIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJImputationIntegrationTest: @Transactional;
    
    @Autowired
    JJImputationDataOnDemand JJImputationIntegrationTest.dod;
    
    @Autowired
    JJImputationService JJImputationIntegrationTest.jJImputationService;
    
    @Autowired
    JJImputationRepository JJImputationIntegrationTest.jJImputationRepository;
    
    @Test
    public void JJImputationIntegrationTest.testCountAllJJImputations() {
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", dod.getRandomJJImputation());
        long count = jJImputationService.countAllJJImputations();
        Assert.assertTrue("Counter for 'JJImputation' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJImputationIntegrationTest.testFindJJImputation() {
        JJImputation obj = dod.getRandomJJImputation();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to provide an identifier", id);
        obj = jJImputationService.findJJImputation(id);
        Assert.assertNotNull("Find method for 'JJImputation' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJImputation' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJImputationIntegrationTest.testFindAllJJImputations() {
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", dod.getRandomJJImputation());
        long count = jJImputationService.countAllJJImputations();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJImputation', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJImputation> result = jJImputationService.findAllJJImputations();
        Assert.assertNotNull("Find all method for 'JJImputation' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJImputation' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJImputationIntegrationTest.testFindJJImputationEntries() {
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", dod.getRandomJJImputation());
        long count = jJImputationService.countAllJJImputations();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJImputation> result = jJImputationService.findJJImputationEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJImputation' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJImputation' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJImputationIntegrationTest.testFlush() {
        JJImputation obj = dod.getRandomJJImputation();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to provide an identifier", id);
        obj = jJImputationService.findJJImputation(id);
        Assert.assertNotNull("Find method for 'JJImputation' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJImputation(obj);
        Integer currentVersion = obj.getVersion();
        jJImputationRepository.flush();
        Assert.assertTrue("Version for 'JJImputation' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJImputationIntegrationTest.testUpdateJJImputationUpdate() {
        JJImputation obj = dod.getRandomJJImputation();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to provide an identifier", id);
        obj = jJImputationService.findJJImputation(id);
        boolean modified =  dod.modifyJJImputation(obj);
        Integer currentVersion = obj.getVersion();
        JJImputation merged = jJImputationService.updateJJImputation(obj);
        jJImputationRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJImputation' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJImputationIntegrationTest.testSaveJJImputation() {
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", dod.getRandomJJImputation());
        JJImputation obj = dod.getNewTransientJJImputation(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJImputation' identifier to be null", obj.getId());
        try {
            jJImputationService.saveJJImputation(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJImputationRepository.flush();
        Assert.assertNotNull("Expected 'JJImputation' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJImputationIntegrationTest.testDeleteJJImputation() {
        JJImputation obj = dod.getRandomJJImputation();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImputation' failed to provide an identifier", id);
        obj = jJImputationService.findJJImputation(id);
        jJImputationService.deleteJJImputation(obj);
        jJImputationRepository.flush();
        Assert.assertNull("Failed to remove 'JJImputation' with identifier '" + id + "'", jJImputationService.findJJImputation(id));
    }
    
}