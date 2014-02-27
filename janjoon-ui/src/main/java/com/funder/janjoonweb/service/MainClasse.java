package com.funder.janjoonweb.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.*;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;

public class MainClasse {

	//private static boolean keepLocks=true;

	public static void main(String[] args) {
		
		//SVNConfigManager config = new SVNConfigManager("/home/lazher/svn");
		/*File f=new File(config.getRepository().getLocation().getPath());
		//config.checkIn("FirstCommit");
		SVNClientManager ourClientManager = SVNClientManager.newInstance(null,
				config.getRepository().getAuthenticationManager());
		ourClientManager.getWCClient().doInfo(f, SVNRevision.HEAD);
		
		ourClientManager.getCommitClient().doCommit(new File[] { f },
				keepLocks, "firstCommit", false, true);*/
		//config.checkIn("commit");
		 Svn svn = new Svn();
	        //On definis l'url du dépot subversion
	        svn.setUrl("file:///home/lazher/svn/");
	        //On définis l'identifiant pour accèder au dépot (si l'accès est anonyme, ne rien définir)
	        svn.setLogin("chemakh");
	        //On définis le mot de passe pour accèder au dépot (si l'accès est anonyme, ne rien définir)
	        svn.setPassword("monPassword");
	 
	        //on se connect au svn
	        if(svn.connect()){
	        	
	            //On récupere la liste des dossier et fichiers du dépot a partir de la racine (donc le chemin "")
	            try {
	               System.out.println("succes");
	            	List<SvnEntry> tree= svn.list("");
	                //Pour chaques élément récupéré
	                for (int i = 0; i<tree.size(); i++) {
	                    SvnEntry entry = tree.get(i);
	                    //On imprime les infos de l'élément dans la console
	                    System.out.println(entry.toString());
	                }
	            } catch (SVNException e) {
	                System.out.println("listing de '' impossible : "+e);
	            }
	        }else{
	            System.out.println("connexion impossible");
        }

	}

}
