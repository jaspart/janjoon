// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJChapter;
import com.starit.janjoonweb.domain.JJChapterService;
import java.util.List;

privileged aspect JJChapterService_Roo_Service {
    
    public abstract long JJChapterService.countAllJJChapters();    
    public abstract void JJChapterService.deleteJJChapter(JJChapter JJChapter_);    
    public abstract JJChapter JJChapterService.findJJChapter(Long id);    
    public abstract List<JJChapter> JJChapterService.findAllJJChapters();    
    public abstract List<JJChapter> JJChapterService.findJJChapterEntries(int firstResult, int maxResults);    
    public abstract void JJChapterService.saveJJChapter(JJChapter JJChapter_);    
    public abstract JJChapter JJChapterService.updateJJChapter(JJChapter JJChapter_);    
}
