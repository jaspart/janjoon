package com.starit.janjoonweb.ui.util.service;

import java.util.Date;

import org.tmatesoft.svn.core.SVNLock;





public class SvnEntry {
	
	
	Integer id;	
	private String author;	
	private long revision;
	private Date date;
	private String path;	
	private String type;
	private SVNLock lock;
	private long size;
	private String url;	

	public SvnEntry() {
	}
	
	
	public SvnEntry(String author ,Integer revision ,Date date ,String path ,String type ,SVNLock lock ,Integer size ,String url ) {
		super();
		this.author = author;
		this.revision = revision;
		this.date = date;
		this.path = path;
		this.type = type;
		this.lock = lock;
		this.size = size;
		this.url = url;
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
		String sortie = "==> Instance de la classe SvnEntry : \n";
		sortie += "String author = "+this.getAuthor()+" \n";
		sortie += "Integer revision = "+this.getRevision()+" \n";
		sortie += "String date = "+this.getDate()+" \n";
		sortie += "String path = "+this.getPath()+" \n";
		sortie += "String type = "+this.getType()+" \n";
		sortie += "String lock = "+this.getLock()+" \n";
		sortie += "Integer size = "+this.getSize()+" \n";
		sortie += "String url = "+this.getUrl()+" \n";
		
		return sortie;
	}	
	
	public Integer getId() {
		return this.id;
	}
	
	
	public void setId(Integer id) {
		this.id= id;
	}
	
	
	public String getAuthor() {
		if (author==null)author="";
		return author;
	}
	
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	public long getRevision() {
		return revision;
	}
	
	
	public void setRevision(long l) {
		this.revision = l;
	}
	
	
	public Date getDate() {
		if (date==null)date=new Date();
		return date;
	}
	
	
	public void setDate(Date date2) {
		this.date = date2;
	}
	
	
	public String getPath() {
		if (path==null)path="";
		return path;
	}
	
	
	public void setPath(String path) {
		this.path = path;
	}
	
	
	public String getType() {
		if (type==null)type="";
		return type;
	}
	
	
	public void setType(String type) {
		this.type = type;
	}
	
	
	public SVNLock getLock() {
		return lock;
	}
	
	
	public void setLock(SVNLock svnLock) {
		this.lock = svnLock;
	}
	
	
	public long getSize() {
		return size;
	}
	
	
	public void setSize(long l) {
		this.size = l;
	}
	
	
	public String getUrl() {
		if (url==null)url="";
		return url;
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
