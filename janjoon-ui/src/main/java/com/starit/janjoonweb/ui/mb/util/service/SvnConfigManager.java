package com.starit.janjoonweb.ui.mb.util.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SvnConfigManager extends AbstractConfigManager {

	private static final long serialVersionUID = 1L;
	private SVNRepository repository;
	private static final Logger logger = Logger
			.getLogger(SvnConfigManager.class);

	private static SVNClientManager ourClientManager;
	private static ISVNEventHandler myCommitEventHandler;
	private static ISVNEventHandler myUpdateEventHandler;
	private static ISVNEventHandler myWCEventHandler;
	private static SVNURL svnURL;

	@SuppressWarnings("deprecation")
	public SvnConfigManager(String type, String url, String path, String login,
			String password, String productName, String VersionName) {

		super("SVN", url, path, login, password);
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();

		myCommitEventHandler = new CommitEventHandler();
		myUpdateEventHandler = new UpdateEventHandler();
		myWCEventHandler = new WCEventHandler();

		ISVNAuthenticationManager authManager;

		try {
			svnURL = SVNURL.parseURIDecoded(url);
			repository = SVNRepositoryFactory.create(svnURL, null);

			authManager = new BasicAuthenticationManager(login, password);
			// set an auth manager which will provide user credentials
			repository.setAuthenticationManager(authManager);
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);

			ourClientManager = SVNClientManager.newInstance(options,
					authManager);

			ourClientManager.getCommitClient().setEventHandler(
					myCommitEventHandler);

			ourClientManager.getUpdateClient().setEventHandler(
					myUpdateEventHandler);

			ourClientManager.getWCClient().setEventHandler(myWCEventHandler);

			this.path = path + productName;

			logger.info("Repository Root: "
					+ repository.getRepositoryRoot(true));
			logger.info("Repository UUID: "
					+ repository.getRepositoryUUID(true));
		} catch (SVNException e) {
			repository = null;
		}
		if (repository != null) {
			try {
				svnURL = SVNURL.parseURIDecoded(url);
				svnURL = svnURL.appendPath(productName, false);
				ourClientManager.getCommitClient().doMkDir(
						new SVNURL[] { svnURL },
						"make " + productName + " directory");
			} catch (SVNException e) {

			}

			try {
				svnURL = SVNURL.parseURIDecoded(url);
				svnURL = svnURL.appendPath(productName, false);

				String[] versions = VersionName.split("/");

				for (String version : versions) // iterate over the pais
				{
					try {
						svnURL = svnURL.appendPath(version, false);

						ourClientManager.getCommitClient().doMkDir(
								new SVNURL[] { svnURL },
								"make " + version + " directory");

					} catch (SVNException e) {
						svnURL = svnURL.appendPath(version, false);
					}
				}
			} catch (SVNException e1) {
				e1.printStackTrace();
			}

			try {
				svnURL = SVNURL.parseURIDecoded(url);
				svnURL = svnURL.appendPath(productName, false);
			} catch (SVNException e) {
				e.printStackTrace();
			}

		}

	}

	public SVNRepository getRepository() {
		return repository;
	}

	public void setRepository(SVNRepository repository) {
		this.repository = repository;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkIn(File f, String message) {

		SVNCommitClient commitClient = ourClientManager.getCommitClient();
		commitClient.setIgnoreExternals(false);
		try {
			File[] files = null;
			if (f == null)
				files = new File[] { new File(this.path) };
			else
				files = new File[] { f };
			SVNCommitInfo v = commitClient.doCommit(files, false, message,
					false, true);
			System.out.println(v.toString());
			if (v.getNewRevision() == -1)
				return false;
			else
				return true;
		} catch (SVNException e) {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkOut(String branche) {

		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		/*
		 * sets externals not to be ignored during the checkout
		 */
		updateClient.setIgnoreExternals(false);
		/*
		 * returns the number of the revision at which the working copy is
		 */
		try {
			updateClient.doCheckout(SVNURL.parseURIDecoded(url),
					new File(path), SVNRevision.HEAD, SVNRevision.HEAD, true);
			return true;
		} catch (SVNException e) {
			return false;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public String cloneRemoteRepository(String url, String name, String path) {

		if (repository != null) {
			try {
				File file = new File(path + "/" + name);
				// Check out
				SVNUpdateClient updateClient = new SVNUpdateClient(
						repository.getAuthenticationManager(), null);
				updateClient.setIgnoreExternals(false);
				// System.out.println("test");

				updateClient.doCheckout(SVNURL.parseURIDecoded(url), file,
						SVNRevision.HEAD, SVNRevision.HEAD, true);
				return null;
			} catch (SVNException e) {
				e.printStackTrace();
				return "SVNException";
			}
		} else
			return "wrongException";

	}

	@Override
	public boolean addFile(String p, String name, boolean isFile) {

		p = p.replace(path, "");
		File myfile = new File(path + File.separator + p, name);
		logger.info("add file operation started !!!");
		try {

			if (!isFile) {
				myfile.mkdirs();
				myfile.createNewFile();
				// git.add().addFilepattern(".").call();
				logger.info("Add file " + myfile + " to repository at " + path);

				addEntry(myfile);
				return true;

			} else {
				myfile.createNewFile();
				// git.add().addFilepattern(".").call();
				logger.info("Add file " + myfile + " to repository at " + path);

				addEntry(myfile);
				return true;
			}

		} catch (IOException e) {
			File directory = new File(path + "/" + p);
			if (directory.mkdirs()) {

				try {
					addEntry(directory);
					try {
						myfile.createNewFile();

						logger.info("Add file " + myfile + " to repository at "
								+ path);
						addEntry(myfile);

						return true;
					} catch (IOException e1) {

						return false;
					}
				} catch (SVNException e2) {
					return false;
				}

			} else {
				logger.error("error throw add file operation :IOException"
						+ e.getMessage());
				return false;
			}

		} catch (SVNException e) {
			return false;
		}
	}

	@Override
	public boolean createRepository(String path) {
		try {
			SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository(
					new File(path), true, false);
			return true;
		} catch (SVNException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public TreeNode listRepositoryContent(String version) {

		DefaultTreeNode root = new DefaultTreeNode("folder", path + "/"
				+ version, null);

		repositoryTreeNode(new File(path + "/" + version), root);

		return root;

	}

	private void repositoryTreeNode(File workTree, DefaultTreeNode root) {

		File[] files = workTree.listFiles();
		int i = 0;
		while (files != null && i < files.length) {
			if (!files[i].getName().equalsIgnoreCase(".git")) {

				if (files[i].isDirectory()) {
					DefaultTreeNode tree = new DefaultTreeNode("folder",
							files[i], root);
					repositoryTreeNode(files[i], tree);
					File[] t = files[i].listFiles();
					int j = 0;
					while (j < t.length) {

						j++;
					}

				} else {
					new DefaultTreeNode("file", files[i], root);
				}

			}
			i++;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean pullRepository() {

		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		/*
		 * sets externals not to be ignored during the update
		 */
		updateClient.setIgnoreExternals(false);
		/*
		 * returns the number of the revision wcPath was updated to
		 */
		try {
			System.out.println(updateClient.doUpdate(new File(path),
					SVNRevision.HEAD, true));
			return true;
		} catch (SVNException e) {
			return false;
		}

	}

	@SuppressWarnings({ "deprecation" })
	public static void addEntry(File wcPath) throws SVNException {

		ourClientManager.getWCClient().doAdd(wcPath, false, false, false, true);

	}

	public static void delete(File wcPath, boolean force) throws SVNException {
		ourClientManager.getWCClient().doDelete(wcPath, force, false);
	}

	@Override
	public ArrayList<String> getAllBranches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pushRepository() {
		// TODO Auto-generated method stub
		return false;
	}

}
