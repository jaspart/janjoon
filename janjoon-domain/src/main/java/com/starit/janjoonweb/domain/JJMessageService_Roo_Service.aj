// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.starit.janjoonweb.domain;

import com.starit.janjoonweb.domain.JJMessage;
import com.starit.janjoonweb.domain.JJMessageService;
import java.util.List;

privileged aspect JJMessageService_Roo_Service{

public abstract long JJMessageService.countAllJJMessages();public abstract void JJMessageService.deleteJJMessage(JJMessage JJMessage_);public abstract JJMessage JJMessageService.findJJMessage(Long id);public abstract List<JJMessage>JJMessageService.findAllJJMessages();public abstract List<JJMessage>JJMessageService.findJJMessageEntries(int firstResult,int maxResults);public abstract void JJMessageService.saveJJMessage(JJMessage JJMessage_);public abstract JJMessage JJMessageService.updateJJMessage(JJMessage JJMessage_);}
