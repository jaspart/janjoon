// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJCategoryDataOnDemand;
import com.funder.janjoonweb.domain.JJCategoryIntegrationTest;
import com.funder.janjoonweb.domain.JJCategoryRepository;
import com.funder.janjoonweb.domain.JJCategoryService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect JJCategoryIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJCategoryIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJCategoryIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJCategoryIntegrationTest: @Transactional;
    
    @Autowired
    private JJCategoryDataOnDemand JJCategoryIntegrationTest.dod;
    
    @Autowired
    JJCategoryService JJCategoryIntegrationTest.jJCategoryService;
    
    @Autowired
    JJCategoryRepository JJCategoryIntegrationTest.jJCategoryRepository;
    
    @Test
    public void JJCategoryIntegrationTest.testCountAllJJCategorys() {
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", dod.getRandomJJCategory());
        long count = jJCategoryService.countAllJJCategorys();
        Assert.assertTrue("Counter for 'JJCategory' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJCategoryIntegrationTest.testFindJJCategory() {
        JJCategory obj = dod.getRandomJJCategory();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to provide an identifier", id);
        obj = jJCategoryService.findJJCategory(id);
        Assert.assertNotNull("Find method for 'JJCategory' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJCategory' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJCategoryIntegrationTest.testFindAllJJCategorys() {
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", dod.getRandomJJCategory());
        long count = jJCategoryService.countAllJJCategorys();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJCategory', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJCategory> result = jJCategoryService.findAllJJCategorys();
        Assert.assertNotNull("Find all method for 'JJCategory' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJCategory' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJCategoryIntegrationTest.testFindJJCategoryEntries() {
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", dod.getRandomJJCategory());
        long count = jJCategoryService.countAllJJCategorys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJCategory> result = jJCategoryService.findJJCategoryEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJCategory' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJCategory' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJCategoryIntegrationTest.testFlush() {
        JJCategory obj = dod.getRandomJJCategory();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to provide an identifier", id);
        obj = jJCategoryService.findJJCategory(id);
        Assert.assertNotNull("Find method for 'JJCategory' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJCategory(obj);
        Integer currentVersion = obj.getVersion();
        jJCategoryRepository.flush();
        Assert.assertTrue("Version for 'JJCategory' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJCategoryIntegrationTest.testUpdateJJCategoryUpdate() {
        JJCategory obj = dod.getRandomJJCategory();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to provide an identifier", id);
        obj = jJCategoryService.findJJCategory(id);
        boolean modified =  dod.modifyJJCategory(obj);
        Integer currentVersion = obj.getVersion();
        JJCategory merged = (JJCategory)jJCategoryService.updateJJCategory(obj);
        jJCategoryRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJCategory' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJCategoryIntegrationTest.testSaveJJCategory() {
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", dod.getRandomJJCategory());
        JJCategory obj = dod.getNewTransientJJCategory(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJCategory' identifier to be null", obj.getId());
        jJCategoryService.saveJJCategory(obj);
        jJCategoryRepository.flush();
        Assert.assertNotNull("Expected 'JJCategory' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJCategoryIntegrationTest.testDeleteJJCategory() {
        JJCategory obj = dod.getRandomJJCategory();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJCategory' failed to provide an identifier", id);
        obj = jJCategoryService.findJJCategory(id);
        jJCategoryService.deleteJJCategory(obj);
        jJCategoryRepository.flush();
        Assert.assertNull("Failed to remove 'JJCategory' with identifier '" + id + "'", jJCategoryService.findJJCategory(id));
    }
    
}
