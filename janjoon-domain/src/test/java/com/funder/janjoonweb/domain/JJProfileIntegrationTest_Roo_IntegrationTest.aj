// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJProfileDataOnDemand;
import com.funder.janjoonweb.domain.JJProfileIntegrationTest;
import com.funder.janjoonweb.domain.JJProfileRepository;
import com.funder.janjoonweb.domain.JJProfileService;
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

privileged aspect JJProfileIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJProfileIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJProfileIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJProfileIntegrationTest: @Transactional;
    
    @Autowired
    JJProfileDataOnDemand JJProfileIntegrationTest.dod;
    
    @Autowired
    JJProfileService JJProfileIntegrationTest.jJProfileService;
    
    @Autowired
    JJProfileRepository JJProfileIntegrationTest.jJProfileRepository;
    
    @Test
    public void JJProfileIntegrationTest.testCountAllJJProfiles() {
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", dod.getRandomJJProfile());
        long count = jJProfileService.countAllJJProfiles();
        Assert.assertTrue("Counter for 'JJProfile' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJProfileIntegrationTest.testFindJJProfile() {
        JJProfile obj = dod.getRandomJJProfile();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to provide an identifier", id);
        obj = jJProfileService.findJJProfile(id);
        Assert.assertNotNull("Find method for 'JJProfile' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJProfile' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJProfileIntegrationTest.testFindAllJJProfiles() {
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", dod.getRandomJJProfile());
        long count = jJProfileService.countAllJJProfiles();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJProfile', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJProfile> result = jJProfileService.findAllJJProfiles();
        Assert.assertNotNull("Find all method for 'JJProfile' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJProfile' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJProfileIntegrationTest.testFindJJProfileEntries() {
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", dod.getRandomJJProfile());
        long count = jJProfileService.countAllJJProfiles();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJProfile> result = jJProfileService.findJJProfileEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJProfile' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJProfile' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJProfileIntegrationTest.testFlush() {
        JJProfile obj = dod.getRandomJJProfile();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to provide an identifier", id);
        obj = jJProfileService.findJJProfile(id);
        Assert.assertNotNull("Find method for 'JJProfile' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJProfile(obj);
        Integer currentVersion = obj.getVersion();
        jJProfileRepository.flush();
        Assert.assertTrue("Version for 'JJProfile' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJProfileIntegrationTest.testUpdateJJProfileUpdate() {
        JJProfile obj = dod.getRandomJJProfile();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to provide an identifier", id);
        obj = jJProfileService.findJJProfile(id);
        boolean modified =  dod.modifyJJProfile(obj);
        Integer currentVersion = obj.getVersion();
        JJProfile merged = jJProfileService.updateJJProfile(obj);
        jJProfileRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJProfile' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJProfileIntegrationTest.testSaveJJProfile() {
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", dod.getRandomJJProfile());
        JJProfile obj = dod.getNewTransientJJProfile(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJProfile' identifier to be null", obj.getId());
        try {
            jJProfileService.saveJJProfile(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJProfileRepository.flush();
        Assert.assertNotNull("Expected 'JJProfile' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJProfileIntegrationTest.testDeleteJJProfile() {
        JJProfile obj = dod.getRandomJJProfile();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProfile' failed to provide an identifier", id);
        obj = jJProfileService.findJJProfile(id);
        jJProfileService.deleteJJProfile(obj);
        jJProfileRepository.flush();
        Assert.assertNull("Failed to remove 'JJProfile' with identifier '" + id + "'", jJProfileService.findJJProfile(id));
    }
    
}
