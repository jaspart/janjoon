package com.starit.janjoonweb.ui.util.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
public class Svn {

	Integer id;
	private String url;
	private String login;
	private String password;
	private SVNRepository repository;
	
	public Svn() {
		this.repository = null;
	}
	public Svn(String url ,String login ,String password ) {
		super();
		this.url = url;
		this.login = login;
		this.password = password;
		this.repository = null;
	}
	public boolean connect(){
		boolean success = false;

		try {
			//Connexion
			repository = SVNRepositoryFactory.create( SVNURL.parseURIDecoded( this.url ) );
			//Authentification
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( this.login , this.password );
			this.repository.setAuthenticationManager(authManager);
			System.out.println(repository.getLocation().getPath());
			success = true;
		} catch (SVNException e) {
			System.out.println("Impossible de se connecter/autentifier au SVN : "+e);                
		}
		return success;
	}

	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}
	
	@Override
	public String toString() {
		String sortie = "==> Instance de la classe Svn : \n";
		sortie += "String url = "+this.getUrl()+" \n";
		sortie += "String login = "+this.getLogin()+" \n";
		sortie += "String password = "+this.getPassword()+" \n";

		return sortie;
	}


	
	public String check(String path){
		String type = "UNKNOWN";
		SVNNodeKind nodeKind;
		try {
			nodeKind = this.repository.checkPath( path ,  -1 );
			if ( nodeKind == SVNNodeKind.NONE ) {
				type = "EMPTY";
			} else if ( nodeKind == SVNNodeKind.FILE ) {
				type = "FILE";
			} else if ( nodeKind == SVNNodeKind.DIR ) {
				type = "FOLDER";	
			} else if ( nodeKind == SVNNodeKind.UNKNOWN ) {
				type = "UNKNOWN";	
			}
		} catch (SVNException e) {
			System.out.println("Impossible de verifier le chemin "+path+" : "+e);
		}

		return type;
	}

	
	@SuppressWarnings("rawtypes")
	public List<SvnEntry> list(String path ) throws SVNException {
		List<SvnEntry> tree = new ArrayList<SvnEntry>();
		if("FOLDER".equalsIgnoreCase(this.check(path))){
			
			Collection entries = this.repository.getDir( path, -1 , null , (Collection) null );
			Iterator iterator = entries.iterator( );
			while ( iterator.hasNext( ) ) {
				SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );

				SvnEntry svnEntry = new SvnEntry();
				svnEntry.setAuthor(entry.getName());
				svnEntry.setDate(entry.getDate());
				svnEntry.setLock(entry.getLock());
				svnEntry.setPath((path.equals( "" ) ? "" : path + "/" ));
				svnEntry.setRevision(entry.getRevision());
				svnEntry.setSize(entry.getSize());

				if(entry.getKind() == SVNNodeKind.NONE){
					svnEntry.setType("EMPTY");
				}else if(entry.getKind() == SVNNodeKind.FILE){
					svnEntry.setType("FILE");
				}else if(entry.getKind() == SVNNodeKind.DIR){
					svnEntry.setType("FOLDER");
				}else if(entry.getKind() == SVNNodeKind.UNKNOWN){
					svnEntry.setType("UNKNOWN");
				}
				svnEntry.setUrl(entry.getURL().toString());
				tree.add(svnEntry);


				if ( entry.getKind() == SVNNodeKind.DIR ) {
					this.list( ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ) );
				}
			}
		}else{
			System.out.println("Impossible de lister "+path+" car ce n'est pas un dossier");
		}
		return tree;
	}

	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id= id;
	}

	
	public String getUrl() {
		if (url==null)url="";
		return url;
	}

	
	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getLogin() {
		if (login==null)login="";
		return login;
	}

	
	public void setLogin(String login) {
		this.login = login;
	}

	
	public String getPassword() {
		if (password==null)password="";
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getRoot(){
		String root = "Pas de racine";
		try {
			root = this.repository.getRepositoryRoot( true ).toString() ;
		} catch (SVNException e) {
			System.out.println("Impossible de récuperer la racine:"+e);
		}
		return root;
	}

	
	public String getUID(){
		String uid = "Pas d'UID";
		try {
			uid = this.repository.getRepositoryUUID( true ).toString() ;
		} catch (SVNException e) {
			System.out.println("Impossible de récuperer l'UID:"+e);
		}
		return uid;
	}

}