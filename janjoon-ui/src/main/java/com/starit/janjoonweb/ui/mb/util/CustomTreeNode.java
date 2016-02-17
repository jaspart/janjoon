package com.starit.janjoonweb.ui.mb.util;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Extension of the {@link org.primefaces.model.DefaultTreeNode} class that
 * overrides the node type, and includes a data component.
 * 
 * @see org.primefaces.model.DefaultTreeNode
 * @author John Yeary
 * @version 1.0
 */
public class CustomTreeNode extends DefaultTreeNode {

	private static final long	serialVersionUID	= 5333810777428638968L;

	private String				styleClass;

	public String getStyleClass() {
		return this.styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * @param type
	 *            The type of node this represents.
	 * @param data
	 *            {@code Object} value stored in the node.
	 * @param parent
	 *            the {@link org.primefaces.model.TreeNode} which is the parent
	 *            to this object, or {@code null} if this is the "root" node.
	 */
	public CustomTreeNode(String type, Object data, TreeNode parent) {
		super(DEFAULT_TYPE, data, parent);
	}

	/**
	 * Constructor which sets {@code Object} data, and parent node.
	 * 
	 * @param data
	 *            {@code Object} value stored in the node.
	 * @param parent
	 *            parent the {@link org.primefaces.model.TreeNode} which is the
	 *            parent to this object, or {@code null} if this is the "root"
	 *            node.
	 */
	public CustomTreeNode(Object data, TreeNode parent) {
		super(data, parent);
	}

	/**
	 * This method returns
	 * {@link com.bluelotussoftware.example.jsf.TreeNodeType#getType()}
	 * depending on whether the node is a "leaf" node which contains no
	 * children, or a "node" if it contains children.
	 * 
	 * @return {@link com.bluelotussoftware.example.jsf.TreeNodeType#getType()}
	 *         based on whether this node has child objects.
	 * @Override public String getType() {
	 *           System.out.println("type="+super.getType()); return
	 *           super.getType(); //DEFAULT_TYPE; }
	 */
}