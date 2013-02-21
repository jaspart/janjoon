// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJImportanceDataOnDemand;
import com.funder.janjoonweb.domain.JJImportanceIntegrationTest;
import com.funder.janjoonweb.domain.JJImportanceRepository;
import com.funder.janjoonweb.domain.JJImportanceService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJImportanceIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJImportanceIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJImportanceIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJImportanceIntegrationTest: @Transactional;
    
    @Autowired
    JJImportanceDataOnDemand JJImportanceIntegrationTest.dod;
    
    @Autowired
    JJImportanceService JJImportanceIntegrationTest.jJImportanceService;
    
    @Autowired
    JJImportanceRepository JJImportanceIntegrationTest.jJImportanceRepository;
    
    @Test
    public void JJImportanceIntegrationTest.testCountAllJJImportances() {
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", dod.getRandomJJImportance());
        long count = jJImportanceService.countAllJJImportances();
        Assert.assertTrue("Counter for 'JJImportance' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJImportanceIntegrationTest.testFindJJImportance() {
        JJImportance obj = dod.getRandomJJImportance();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to provide an identifier", id);
        obj = jJImportanceService.findJJImportance(id);
        Assert.assertNotNull("Find method for 'JJImportance' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJImportance' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJImportanceIntegrationTest.testFindAllJJImportances() {
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", dod.getRandomJJImportance());
        long count = jJImportanceService.countAllJJImportances();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJImportance', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJImportance> result = jJImportanceService.findAllJJImportances();
        Assert.assertNotNull("Find all method for 'JJImportance' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJImportance' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJImportanceIntegrationTest.testFindJJImportanceEntries() {
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", dod.getRandomJJImportance());
        long count = jJImportanceService.countAllJJImportances();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJImportance> result = jJImportanceService.findJJImportanceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJImportance' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJImportance' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJImportanceIntegrationTest.testFlush() {
        JJImportance obj = dod.getRandomJJImportance();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to provide an identifier", id);
        obj = jJImportanceService.findJJImportance(id);
        Assert.assertNotNull("Find method for 'JJImportance' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJImportance(obj);
        Integer currentVersion = obj.getVersion();
        jJImportanceRepository.flush();
        Assert.assertTrue("Version for 'JJImportance' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJImportanceIntegrationTest.testUpdateJJImportanceUpdate() {
        JJImportance obj = dod.getRandomJJImportance();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to provide an identifier", id);
        obj = jJImportanceService.findJJImportance(id);
        boolean modified =  dod.modifyJJImportance(obj);
        Integer currentVersion = obj.getVersion();
        JJImportance merged = (JJImportance)jJImportanceService.updateJJImportance(obj);
        jJImportanceRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJImportance' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJImportanceIntegrationTest.testSaveJJImportance() {
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", dod.getRandomJJImportance());
        JJImportance obj = dod.getNewTransientJJImportance(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJImportance' identifier to be null", obj.getId());
        jJImportanceService.saveJJImportance(obj);
        jJImportanceRepository.flush();
        Assert.assertNotNull("Expected 'JJImportance' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJImportanceIntegrationTest.testDeleteJJImportance() {
        JJImportance obj = dod.getRandomJJImportance();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJImportance' failed to provide an identifier", id);
        obj = jJImportanceService.findJJImportance(id);
        jJImportanceService.deleteJJImportance(obj);
        jJImportanceRepository.flush();
        Assert.assertNull("Failed to remove 'JJImportance' with identifier '" + id + "'", jJImportanceService.findJJImportance(id));
    }
    
}
