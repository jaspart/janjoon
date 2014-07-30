// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJSoftwareDataOnDemand;
import com.starit.janjoonweb.domain.JJSoftwareIntegrationTest;
import com.starit.janjoonweb.domain.JJSoftwareRepository;
import com.starit.janjoonweb.domain.JJSoftwareService;
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

privileged aspect JJSoftwareIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJSoftwareIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJSoftwareIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJSoftwareIntegrationTest: @Transactional;
    
    @Autowired
    JJSoftwareDataOnDemand JJSoftwareIntegrationTest.dod;
    
    @Autowired
    JJSoftwareService JJSoftwareIntegrationTest.jJSoftwareService;
    
    @Autowired
    JJSoftwareRepository JJSoftwareIntegrationTest.jJSoftwareRepository;
    
    @Test
    public void JJSoftwareIntegrationTest.testCountAllJJSoftwares() {
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", dod.getRandomJJSoftware());
        long count = jJSoftwareService.countAllJJSoftwares();
        Assert.assertTrue("Counter for 'JJSoftware' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testFindJJSoftware() {
        JJSoftware obj = dod.getRandomJJSoftware();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to provide an identifier", id);
        obj = jJSoftwareService.findJJSoftware(id);
        Assert.assertNotNull("Find method for 'JJSoftware' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJSoftware' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testFindAllJJSoftwares() {
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", dod.getRandomJJSoftware());
        long count = jJSoftwareService.countAllJJSoftwares();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJSoftware', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJSoftware> result = jJSoftwareService.findAllJJSoftwares();
        Assert.assertNotNull("Find all method for 'JJSoftware' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJSoftware' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testFindJJSoftwareEntries() {
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", dod.getRandomJJSoftware());
        long count = jJSoftwareService.countAllJJSoftwares();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJSoftware> result = jJSoftwareService.findJJSoftwareEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJSoftware' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJSoftware' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testFlush() {
        JJSoftware obj = dod.getRandomJJSoftware();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to provide an identifier", id);
        obj = jJSoftwareService.findJJSoftware(id);
        Assert.assertNotNull("Find method for 'JJSoftware' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJSoftware(obj);
        Integer currentVersion = obj.getVersion();
        jJSoftwareRepository.flush();
        Assert.assertTrue("Version for 'JJSoftware' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testUpdateJJSoftwareUpdate() {
        JJSoftware obj = dod.getRandomJJSoftware();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to provide an identifier", id);
        obj = jJSoftwareService.findJJSoftware(id);
        boolean modified =  dod.modifyJJSoftware(obj);
        Integer currentVersion = obj.getVersion();
        JJSoftware merged = jJSoftwareService.updateJJSoftware(obj);
        jJSoftwareRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJSoftware' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testSaveJJSoftware() {
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", dod.getRandomJJSoftware());
        JJSoftware obj = dod.getNewTransientJJSoftware(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJSoftware' identifier to be null", obj.getId());
        try {
            jJSoftwareService.saveJJSoftware(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJSoftwareRepository.flush();
        Assert.assertNotNull("Expected 'JJSoftware' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJSoftwareIntegrationTest.testDeleteJJSoftware() {
        JJSoftware obj = dod.getRandomJJSoftware();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSoftware' failed to provide an identifier", id);
        obj = jJSoftwareService.findJJSoftware(id);
        jJSoftwareService.deleteJJSoftware(obj);
        jJSoftwareRepository.flush();
        Assert.assertNull("Failed to remove 'JJSoftware' with identifier '" + id + "'", jJSoftwareService.findJJSoftware(id));
    }
    
}
