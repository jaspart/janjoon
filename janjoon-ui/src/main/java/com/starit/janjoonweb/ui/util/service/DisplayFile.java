package com.starit.janjoonweb.ui.util.service;


import java.io.*;
import java.util.*;

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.*;
import org.tmatesoft.svn.core.internal.io.dav.*;
import org.tmatesoft.svn.core.internal.io.fs.*;
import org.tmatesoft.svn.core.internal.io.svn.*;
import org.tmatesoft.svn.core.io.*;
import org.tmatesoft.svn.core.wc.*;


public class DisplayFile {
    
    public static void main(String[] args) {
       
        String url = "file:///home/lazher/svn";
        String name = "anonymous";
        String password = "anonymous";
        String filePath = "/.git/config";       
        setupLibrary();

        if (args != null) {
            
            url = (args.length >= 1) ? args[0] : url;
           
            filePath = (args.length >= 2) ? args[1] : filePath;
           
            name = (args.length >= 3) ? args[2] : name;
            
            password = (args.length >= 4) ? args[3] : password;
        }
        SVNRepository repository = null;
        try {
           
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            
            System.err
                    .println("error while creating an SVNRepository for the location '"
                            + url + "': " + svne.getMessage());
            System.exit(1);
        }

        
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
      
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);
            
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + url + "'.");
                System.exit(1);
            } else if (nodeKind == SVNNodeKind.DIR) {
                System.err.println("The entry at '" + url
                        + "' is a directory while a file was expected.");
                System.exit(1);
            }
            
           
            repository.getFile(filePath, -1, fileProperties, baos);

        } catch (SVNException svne) {
            System.err.println("error while fetching the file contents and properties: " + svne.getMessage());
            System.exit(1);
        }

        String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);

        boolean isTextType = SVNProperty.isTextMimeType(mimeType);

        Iterator iterator = fileProperties.nameSet().iterator();
       
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = fileProperties.getStringValue(propertyName);
            System.out.println("File property: " + propertyName + "="
                    + propertyValue);
        }
       
        if (isTextType) {
            System.out.println("File contents:");
            System.out.println();
            try {            	
                baos.writeTo(System.out);
                String s=baos.toString();
                System.err.println(s);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            System.out
                    .println("File contents can not be displayed in the console since the mime-type property says that it's not a kind of a text file.");
        }
       
        long latestRevision = -1;
        try {
            latestRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
            System.err.println("error while fetching the latest repository revision: " + svne.getMessage());
            System.exit(1);
        }
        System.out.println("");
        System.out.println("---------------------------------------------");
        System.out.println("Repository latest revision: " + latestRevision);
        System.exit(0);
    }

    private static void setupLibrary() {
     
        DAVRepositoryFactory.setup();
        
        SVNRepositoryFactoryImpl.setup();
       
        FSRepositoryFactory.setup();
    }
}