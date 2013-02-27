package org.primefaces.examples.view;  
  
import java.io.Serializable;  

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
  
import org.primefaces.model.DefaultTreeNode;  
import org.primefaces.model.TreeNode;  
  
import org.primefaces.examples.domain.Document;  
  
@ManagedBean
@ApplicationScoped
public class DocumentsController implements Serializable {  
  
    /** DocumentsController serial */
	private static final long serialVersionUID = 1L;
	private TreeNode root;  
  
    @SuppressWarnings("unused")
	public DocumentsController() {  
        root = new DefaultTreeNode("root", null);  
  
        TreeNode documents = new DefaultTreeNode(new Document("src", "-", "Folder"), root);  
        TreeNode pictures = new DefaultTreeNode(new Document("WebContent", "-", "Folder"), root);  
        TreeNode movies = new DefaultTreeNode(new Document("conf", "-", "Folder"), root);  
  
        TreeNode work = new DefaultTreeNode(new Document("com.company.product", "-", "Folder"), documents);  
        TreeNode primefaces = new DefaultTreeNode(new Document("modules", "-", "Folder"), documents);  
  
        //Documents  
        TreeNode expenses = new DefaultTreeNode("src", new Document("Expenses.java", "30 KB", "Word Document"), work);  
        TreeNode resume = new DefaultTreeNode("src", new Document("Resume.java", "10 KB", "Word Document"), work);  
        TreeNode refdoc = new DefaultTreeNode("src", new Document("RefDoc.cpp", "40 KB", "Pages Document"), primefaces);  
  
        //Pictures  
        TreeNode barca = new DefaultTreeNode("WebContent", new Document("home.xhtml", "30 KB", "JPEG Image"), pictures);  
        TreeNode primelogo = new DefaultTreeNode("WebContent", new Document("login.jsp", "45 KB", "JPEG Image"), pictures);  
        TreeNode optimus = new DefaultTreeNode("WebContent", new Document("fronttrading.xhtml", "96 KB", "PNG Image"), pictures);  
  
        //Movies  
        TreeNode pacino = new DefaultTreeNode(new Document("production", "-", "Folder"), movies);  
        TreeNode deniro = new DefaultTreeNode(new Document("integration", "-", "Folder"), movies);  
  
        TreeNode scarface = new DefaultTreeNode("conf", new Document("trading.ini", "15 GB", "Movie File"), pacino);  
        TreeNode carlitosWay = new DefaultTreeNode("conf", new Document("users.conf", "24 GB", "Movie File"), pacino);  
  
        TreeNode goodfellas = new DefaultTreeNode("conf", new Document("trading.ini", "23 GB", "Movie File"), deniro);  
        TreeNode untouchables = new DefaultTreeNode("conf", new Document("users.conf", "17 GB", "Movie File"), deniro);  
    }  
  
    public TreeNode getRoot() {  
        return root;  
    }  
}  