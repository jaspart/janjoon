// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJProjectDataOnDemand;
import com.funder.janjoonweb.domain.JJProjectIntegrationTest;
import com.funder.janjoonweb.domain.JJProjectRepository;
import com.funder.janjoonweb.domain.JJProjectService;
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

privileged aspect JJProjectIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJProjectIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJProjectIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJProjectIntegrationTest: @Transactional;
    
    @Autowired
    JJProjectDataOnDemand JJProjectIntegrationTest.dod;
    
    @Autowired
    JJProjectService JJProjectIntegrationTest.jJProjectService;
    
    @Autowired
    JJProjectRepository JJProjectIntegrationTest.jJProjectRepository;
    
    @Test
    public void JJProjectIntegrationTest.testCountAllJJProjects() {
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", dod.getRandomJJProject());
        long count = jJProjectService.countAllJJProjects();
        Assert.assertTrue("Counter for 'JJProject' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJProjectIntegrationTest.testFindJJProject() {
        JJProject obj = dod.getRandomJJProject();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to provide an identifier", id);
        obj = jJProjectService.findJJProject(id);
        Assert.assertNotNull("Find method for 'JJProject' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJProject' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJProjectIntegrationTest.testFindAllJJProjects() {
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", dod.getRandomJJProject());
        long count = jJProjectService.countAllJJProjects();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJProject', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJProject> result = jJProjectService.findAllJJProjects();
        Assert.assertNotNull("Find all method for 'JJProject' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJProject' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJProjectIntegrationTest.testFindJJProjectEntries() {
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", dod.getRandomJJProject());
        long count = jJProjectService.countAllJJProjects();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJProject> result = jJProjectService.findJJProjectEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJProject' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJProject' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJProjectIntegrationTest.testFlush() {
        JJProject obj = dod.getRandomJJProject();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to provide an identifier", id);
        obj = jJProjectService.findJJProject(id);
        Assert.assertNotNull("Find method for 'JJProject' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJProject(obj);
        Integer currentVersion = obj.getVersion();
        jJProjectRepository.flush();
        Assert.assertTrue("Version for 'JJProject' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJProjectIntegrationTest.testUpdateJJProjectUpdate() {
        JJProject obj = dod.getRandomJJProject();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to provide an identifier", id);
        obj = jJProjectService.findJJProject(id);
        boolean modified =  dod.modifyJJProject(obj);
        Integer currentVersion = obj.getVersion();
        JJProject merged = (JJProject)jJProjectService.updateJJProject(obj);
        jJProjectRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJProject' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJProjectIntegrationTest.testSaveJJProject() {
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", dod.getRandomJJProject());
        JJProject obj = dod.getNewTransientJJProject(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJProject' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJProject' identifier to be null", obj.getId());
        try {
            jJProjectService.saveJJProject(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJProjectRepository.flush();
        Assert.assertNotNull("Expected 'JJProject' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJProjectIntegrationTest.testDeleteJJProject() {
        JJProject obj = dod.getRandomJJProject();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJProject' failed to provide an identifier", id);
        obj = jJProjectService.findJJProject(id);
        jJProjectService.deleteJJProject(obj);
        jJProjectRepository.flush();
        Assert.assertNull("Failed to remove 'JJProject' with identifier '" + id + "'", jJProjectService.findJJProject(id));
    }
    
}
