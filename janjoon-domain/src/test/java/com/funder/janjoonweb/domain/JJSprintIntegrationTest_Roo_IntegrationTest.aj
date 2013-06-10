// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJSprintDataOnDemand;
import com.funder.janjoonweb.domain.JJSprintIntegrationTest;
import com.funder.janjoonweb.domain.JJSprintRepository;
import com.funder.janjoonweb.domain.JJSprintService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJSprintIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJSprintIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJSprintIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJSprintIntegrationTest: @Transactional;
    
    @Autowired
    private JJSprintDataOnDemand JJSprintIntegrationTest.dod;
    
    @Autowired
    JJSprintService JJSprintIntegrationTest.jJSprintService;
    
    @Autowired
    JJSprintRepository JJSprintIntegrationTest.jJSprintRepository;
    
    @Test
    public void JJSprintIntegrationTest.testCountAllJJSprints() {
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", dod.getRandomJJSprint());
        long count = jJSprintService.countAllJJSprints();
        Assert.assertTrue("Counter for 'JJSprint' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJSprintIntegrationTest.testFindJJSprint() {
        JJSprint obj = dod.getRandomJJSprint();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to provide an identifier", id);
        obj = jJSprintService.findJJSprint(id);
        Assert.assertNotNull("Find method for 'JJSprint' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJSprint' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJSprintIntegrationTest.testFindAllJJSprints() {
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", dod.getRandomJJSprint());
        long count = jJSprintService.countAllJJSprints();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJSprint', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJSprint> result = jJSprintService.findAllJJSprints();
        Assert.assertNotNull("Find all method for 'JJSprint' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJSprint' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJSprintIntegrationTest.testFindJJSprintEntries() {
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", dod.getRandomJJSprint());
        long count = jJSprintService.countAllJJSprints();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJSprint> result = jJSprintService.findJJSprintEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJSprint' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJSprint' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJSprintIntegrationTest.testFlush() {
        JJSprint obj = dod.getRandomJJSprint();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to provide an identifier", id);
        obj = jJSprintService.findJJSprint(id);
        Assert.assertNotNull("Find method for 'JJSprint' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJSprint(obj);
        Integer currentVersion = obj.getVersion();
        jJSprintRepository.flush();
        Assert.assertTrue("Version for 'JJSprint' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJSprintIntegrationTest.testUpdateJJSprintUpdate() {
        JJSprint obj = dod.getRandomJJSprint();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to provide an identifier", id);
        obj = jJSprintService.findJJSprint(id);
        boolean modified =  dod.modifyJJSprint(obj);
        Integer currentVersion = obj.getVersion();
        JJSprint merged = (JJSprint)jJSprintService.updateJJSprint(obj);
        jJSprintRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJSprint' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJSprintIntegrationTest.testSaveJJSprint() {
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", dod.getRandomJJSprint());
        JJSprint obj = dod.getNewTransientJJSprint(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJSprint' identifier to be null", obj.getId());
        jJSprintService.saveJJSprint(obj);
        jJSprintRepository.flush();
        Assert.assertNotNull("Expected 'JJSprint' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJSprintIntegrationTest.testDeleteJJSprint() {
        JJSprint obj = dod.getRandomJJSprint();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJSprint' failed to provide an identifier", id);
        obj = jJSprintService.findJJSprint(id);
        jJSprintService.deleteJJSprint(obj);
        jJSprintRepository.flush();
        Assert.assertNull("Failed to remove 'JJSprint' with identifier '" + id + "'", jJSprintService.findJJSprint(id));
    }
    
}
