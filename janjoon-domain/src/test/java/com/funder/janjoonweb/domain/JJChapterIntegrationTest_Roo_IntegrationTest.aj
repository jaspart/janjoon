// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.funder.janjoonweb.domain;

import com.funder.janjoonweb.domain.JJChapterDataOnDemand;
import com.funder.janjoonweb.domain.JJChapterIntegrationTest;
import com.funder.janjoonweb.domain.JJChapterRepository;
import com.funder.janjoonweb.domain.JJChapterService;
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

privileged aspect JJChapterIntegrationTest_Roo_IntegrationTest {
    
    declare @type: JJChapterIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: JJChapterIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: JJChapterIntegrationTest: @Transactional;
    
    @Autowired
    JJChapterDataOnDemand JJChapterIntegrationTest.dod;
    
    @Autowired
    JJChapterService JJChapterIntegrationTest.jJChapterService;
    
    @Autowired
    JJChapterRepository JJChapterIntegrationTest.jJChapterRepository;
    
    @Test
    public void JJChapterIntegrationTest.testCountAllJJChapters() {
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", dod.getRandomJJChapter());
        long count = jJChapterService.countAllJJChapters();
        Assert.assertTrue("Counter for 'JJChapter' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void JJChapterIntegrationTest.testFindJJChapter() {
        JJChapter obj = dod.getRandomJJChapter();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to provide an identifier", id);
        obj = jJChapterService.findJJChapter(id);
        Assert.assertNotNull("Find method for 'JJChapter' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'JJChapter' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void JJChapterIntegrationTest.testFindAllJJChapters() {
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", dod.getRandomJJChapter());
        long count = jJChapterService.countAllJJChapters();
        Assert.assertTrue("Too expensive to perform a find all test for 'JJChapter', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<JJChapter> result = jJChapterService.findAllJJChapters();
        Assert.assertNotNull("Find all method for 'JJChapter' illegally returned null", result);
        Assert.assertTrue("Find all method for 'JJChapter' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void JJChapterIntegrationTest.testFindJJChapterEntries() {
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", dod.getRandomJJChapter());
        long count = jJChapterService.countAllJJChapters();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<JJChapter> result = jJChapterService.findJJChapterEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'JJChapter' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'JJChapter' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void JJChapterIntegrationTest.testFlush() {
        JJChapter obj = dod.getRandomJJChapter();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to provide an identifier", id);
        obj = jJChapterService.findJJChapter(id);
        Assert.assertNotNull("Find method for 'JJChapter' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyJJChapter(obj);
        Integer currentVersion = obj.getVersion();
        jJChapterRepository.flush();
        Assert.assertTrue("Version for 'JJChapter' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJChapterIntegrationTest.testUpdateJJChapterUpdate() {
        JJChapter obj = dod.getRandomJJChapter();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to provide an identifier", id);
        obj = jJChapterService.findJJChapter(id);
        boolean modified =  dod.modifyJJChapter(obj);
        Integer currentVersion = obj.getVersion();
        JJChapter merged = (JJChapter)jJChapterService.updateJJChapter(obj);
        jJChapterRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'JJChapter' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void JJChapterIntegrationTest.testSaveJJChapter() {
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", dod.getRandomJJChapter());
        JJChapter obj = dod.getNewTransientJJChapter(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'JJChapter' identifier to be null", obj.getId());
        try {
            jJChapterService.saveJJChapter(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        jJChapterRepository.flush();
        Assert.assertNotNull("Expected 'JJChapter' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void JJChapterIntegrationTest.testDeleteJJChapter() {
        JJChapter obj = dod.getRandomJJChapter();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'JJChapter' failed to provide an identifier", id);
        obj = jJChapterService.findJJChapter(id);
        jJChapterService.deleteJJChapter(obj);
        jJChapterRepository.flush();
        Assert.assertNull("Failed to remove 'JJChapter' with identifier '" + id + "'", jJChapterService.findJJChapter(id));
    }
    
}
