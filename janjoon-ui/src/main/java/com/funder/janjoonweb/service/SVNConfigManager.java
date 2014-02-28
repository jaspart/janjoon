package com.funder.janjoonweb.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.*;
import org.tmatesoft.svn.core.wc2.*;

public class SVNConfigManager extends ConfigManagerAbstract {

	public SVNConfigManager(String type, String url, String path, String name,
			String mdp) {
		super(type, url, path, name, mdp);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();

	private SVNRepository repository;

	/*public SVNConfigManager() {
		setupLibrary();
		setType("SVN");

	}

	@SuppressWarnings("deprecation")
	public SVNConfigManager(String path) {
		setupLibrary();
		setType("SVN");

		if (!path.startsWith("file://")) {
			path = "file://" + path;
		}

		try {
			repository = SVNRepositoryFactory.create(SVNURL
					.parseURIDecoded(path));
			System.out.println("constructor "
					+ getRepository().getRepositoryRoot() + path);
		} catch (SVNException e) {
			System.out.println("Constructor Failed:SVNException");
			e.printStackTrace();
		}

	}*/

	public SVNRepository getRepository() {
		return (SVNRepository) repository;
	}

	public void setRepository(SVNRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean checkIn(String message) {

		SvnCommit commit = svnOperationFactory.createCommit();
		commit.setCommitMessage(message);
		commit.addTarget(SvnTarget.fromURL(getRepository().getLocation()));
		try {
			commit.run();
			System.out.println("checkin");
			return true;
		} catch (SVNException e) {
			System.out.println("SVNException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean checkOut(String branche) {

		try {
			final SvnCheckout checkout = svnOperationFactory.createCheckout();
			// checkout.setSingleTarget(SvnTarget.fromFile(workingCopyDirectory));
			// checkout.
			checkout.setSource(SvnTarget.fromURL(getRepository().getLocation()));
			// ... other options
			checkout.run();
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			svnOperationFactory.dispose();
		}
		return false;
	}

	@Override
	public boolean addFile(String Path, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createRepository(String path) {
		try {
			SVNRepositoryFactory.createLocalRepository(new File(path), true,
					false);
			return false;
		} catch (SVNException e) {
			System.out.println("SVNException");
			e.printStackTrace();
			return false;
		}

	}

	public String check(String path) {
		String type = "UNKNOWN";
		SVNNodeKind nodeKind;
		try {
			nodeKind = getRepository().checkPath(path, -1);
			if (nodeKind == SVNNodeKind.NONE) {
				type = "EMPTY";
			} else if (nodeKind == SVNNodeKind.FILE) {
				type = "FILE";
			} else if (nodeKind == SVNNodeKind.DIR) {
				type = "FOLDER";
			} else if (nodeKind == SVNNodeKind.UNKNOWN) {
				type = "UNKNOWN";
			}
		} catch (SVNException e) {
			System.out.println("Impossible de verifier le chemin " + path
					+ " : " + e);
		}

		return type;
	}

	@Override
	public TreeNode listRepositoryContent() {
		if("FOLDER".equalsIgnoreCase(this.check(""))){
			Collection entries;
			try {
				entries = getRepository().getDir( "", -1 , null , (Collection) null );
				Iterator iterator = entries.iterator( );
				while ( iterator.hasNext( ) ) {
					SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );
					entry.getName();
				
			}
			} catch (SVNException e) {
				System.out.println("SVNException");
				e.printStackTrace();
			}
			
	}
			
			
		DefaultTreeNode root = new DefaultTreeNode("folder", null);
		//repositoryTreeNode(((Repository) repository).getWorkTree(), root);
		return root;

	}

	private void repositoryTreeNode(File workTree, DefaultTreeNode root) {
		
	}
	

	@Override
	public ArrayList<String> getAllBranches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setFileTexte(File file, String texte) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	private static void setupLibrary() {
	     
        DAVRepositoryFactory.setup();
        
        SVNRepositoryFactoryImpl.setup();
       
        FSRepositoryFactory.setup();
    }
	
	public String getFileText(String filePath)
	{
		String texte="";
		 SVNProperties fileProperties = new SVNProperties();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();

	        try {
	            
	            SVNNodeKind nodeKind = getRepository().checkPath(filePath, -1);
	            
	            if (nodeKind == SVNNodeKind.NONE) {
	                System.err.println("There is no entry at '" + getRepository().getRepositoryUUID() + "'.");
	                System.exit(1);
	            } else if (nodeKind == SVNNodeKind.DIR) {
	                System.err.println("The entry at '" + getRepository().getRepositoryUUID() 
	                        + "' is a directory while a file was expected.");
	                System.exit(1);
	            }
	            
	           
	            getRepository().getFile(filePath, -1, fileProperties, baos);

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
	                texte=baos.toString();	                
	            } catch (IOException ioe) {
	                ioe.printStackTrace();
	            }
	        } else {
	            System.out.println("File contents can not be displayed in the console since the mime-type property says that it's not a kind of a text file.");
	        }
		return texte;
	}

	@Override
	public String cloneRemoteRepository(String url, String name, String path) {
		// TODO Auto-generated method stub
		return null;
	}	

	@Override
	public boolean pushRepository() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pullRepository() {
		// TODO Auto-generated method stub
		return false;
	}

}
