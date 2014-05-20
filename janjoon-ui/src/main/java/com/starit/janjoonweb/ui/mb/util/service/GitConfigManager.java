package com.starit.janjoonweb.ui.mb.util.service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.primefaces.model.*;

import com.starit.janjoonweb.domain.JJContact;

public class GitConfigManager extends AbstractConfigManager {

	private static final Logger logger = Logger
			.getLogger(GitConfigManager.class);
	private static final long serialVersionUID = 1L;
	private Git git;
	private Repository repository;

	public Repository getRepository() {
		return repository;
	}

	public void setRepository() {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
			else if (path.endsWith("/"))
				file = new File(path + ".git");
			else
				file = new File(path + "/.git");

			repository = builder.setGitDir(file)
					.setWorkTree(file.getParentFile())
					// scan up the file system tree
					.build();
			git = new Git(repository);

			logger.debug("GitConfigManager created");

		} catch (IOException e) {
			logger.error("wrong Repository Path");
			e.printStackTrace();
		}

	}

	public Git getGit() {
		return git;
	}

	public void setGit() {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
			else if (path.endsWith("/"))
				file = new File(path + ".git");
			else
				file = new File(path + "/.git");
			repository = builder.setGitDir(file)
					.setWorkTree(file.getParentFile())
					// scan up the file system tree
					.build();
			git = new Git(repository);
			logger.debug("GitConfigManager created");

		} catch (IOException e) {
			logger.error("wrong Repository Path");
			e.printStackTrace();
		}
	}

	public GitConfigManager(String url, String path, JJContact jjContact) {
		super("GIT", url, path, jjContact);

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
			else if (path.endsWith("/"))
				file = new File(path + ".git");
			else
				file = new File(path + "/.git");

			repository = builder.setGitDir(file)
					.setWorkTree(file.getParentFile())
					// scan up the file system tree
					.build();
			git = new Git(repository);

			logger.debug("GitConfigManager created");

		} catch (IOException e) {
			logger.error("wrong Repository Path");
			e.printStackTrace();
		}
	}

	public GitConfigManager(String url, String path) {

		super("GIT", url, path, null);
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
			else if (path.endsWith("/"))
				file = new File(path + ".git");
			else
				file = new File(path + "/.git");

			repository = builder.setGitDir(file)
					.setWorkTree(file.getParentFile())
					// scan up the file system tree
					.build();
			git = new Git(repository);
			logger.debug("GitConfigManager created");

		} catch (IOException e) {
			logger.error("wrong Repository Path");
			e.printStackTrace();
		}

	}

	public GitConfigManager(int i, JJContact contact) {
		super("GIT", "", "", contact);
	}

	// commit a repository
	@Override
	public boolean checkIn(String message) {

		logger.info("---commit operation started!!");
		try {

			CommitCommand commit = git.commit();
			// commit.setAuthor(jJContact.getName(), "");
			commit.setMessage(message);
			commit.setAll(true);
			commit.call();
			logger.debug(repository.getDirectory().getName() + " Commited!!!");
			return true;
		} catch (NoHeadException e) {

			logger.error("error throw commit operation :NoHeadException"
					+ e.getMessage());
			return false;
		} catch (NoMessageException e) {
			logger.error("error throw commit operation :NoMessageException"
					+ e.getMessage());
			return false;
		} catch (UnmergedPathsException e) {
			logger.error("error throw commit operation :UnmergedPathsException"
					+ e.getMessage());
			return false;
		} catch (ConcurrentRefUpdateException e) {
			logger.error("error throw commit operation :ConcurrentRefUpdateException"
					+ e.getMessage());
			return false;
		} catch (WrongRepositoryStateException e) {
			logger.error("error throw commit operation :WrongRepositoryStateException"
					+ e.getMessage());
			return false;
		} catch (GitAPIException e) {

			logger.error("error throw commit operation :GitAPIException"
					+ e.getMessage());
			return false;
		}

	}

	// mettre à jour une repository par rapport au head
	@Override
	public boolean checkOut(String branche) {

		logger.info("---checkOut operation started!!!!");
		try {

			CheckoutCommand checkOutCommand = git.checkout();
			checkOutCommand.setName(branche);
			checkOutCommand.call();
			logger.debug(repository.getDirectory().getName()
					+ " checked Out!!!");
			return true;
		} catch (RefAlreadyExistsException e) {

			logger.error("error throw check out operation :RefAlreadyExistsException "
					+ e.getMessage());
			return false;
		} catch (RefNotFoundException e) {
			logger.error("error throw check out operation :RefNotFoundException "
					+ e.getMessage());
			return false;
		} catch (InvalidRefNameException e) {
			logger.error("error throw check out operation :InvalidRefNameException "
					+ e.getMessage());
			return false;
		} catch (CheckoutConflictException e) {
			logger.error("error throw check out operation :CheckoutConflictException "
					+ e.getMessage());
			return false;
		} catch (GitAPIException e) {
			logger.error("error throw check out operation :GitAPIException "
					+ e.getMessage());
			return false;
		}

	}

	@Override
	public boolean createRepository(String path) {

		logger.info("create repository operation started!!!!");
		try {
			Repository newRepo;
			if (path.endsWith("/"))
				newRepo = new FileRepository(path + ".git");
			else
				newRepo = new FileRepository(path + "/.git");

			newRepo.create();

			logger.debug(newRepo.getDirectory().getPath() + " Created!!!");
			return true;
		} catch (IOException e) {

			logger.error("error threw create repository operation :IOException"
					+ e.getMessage());
			return false;
		}

	}

	@Override
	public TreeNode listRepositoryContent(String version) {

		DefaultTreeNode root = new DefaultTreeNode("folder", repository
				.getDirectory().getParentFile() + "/" + version, null);

		repositoryTreeNode(new File(repository.getDirectory().getParentFile()
				+ "/" + version), root);

		return root;

	}

	private void repositoryTreeNode(File workTree, DefaultTreeNode root) {

		File[] files = workTree.listFiles();
		int i = 0;
		while (i < files.length) {
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

	@Override
	public String cloneRemoteRepository(String url, String name, String path) {

		logger.info("clone repository operation started !!!");
		try {
			CloneCommand cloneCommand = Git.cloneRepository();
			File file = new File(path + "/" + name);
			cloneCommand.setDirectory(file);
			cloneCommand.setURI(url);

			if (jJContact != null) {
				cloneCommand
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
								jJContact.getName(), jJContact.getPassword()));
			}
			cloneCommand.call();
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repo;
			try {
				file = new File(path + "/" + name + "/.git");
				repo = builder.setGitDir(file)
				// scan up the file system tree
						.build();

				logger.debug("repository cloned !!");
				return repo.getDirectory().getPath();
			} catch (IOException e) {

				logger.error("error throw clone operation :IOException"
						+ e.getMessage());
				return null;
			}

		} catch (InvalidRemoteException e) {
			logger.error("error throw clone operation :InvalidRemoteException"
					+ e.getMessage());
			try {
				delete(new File(path + "/" + name));
			} catch (IOException e1) {
				logger.error("error throw clone operation :IOException"
						+ e1.getMessage());
			}
			return "InvalidRemoteException";
		} catch (TransportException e) {

			logger.error("error throw clone operation :TransportException"
					+ e.getMessage());
			try {
				delete(new File(path + "/" + name));
			} catch (IOException e1) {

				logger.error("error throw clone operation :IOException"
						+ e1.getMessage());
			}
			return "TransportException";
		} catch (GitAPIException e) {

			logger.error("error throw clone operation :GitAPIException"
					+ e.getMessage());
			return null;
		}
	}

	@Override
	public boolean pushRepository() {

		logger.info("push repository operation started !!!");
		try {
			git = new Git(repository);

			PushCommand pushCommand = git.push();
			pushCommand.setRemote(url);
			pushCommand.setPushAll();
			pushCommand.setForce(true);
			if (jJContact != null)
				pushCommand
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
								jJContact.getName(), jJContact.getPassword()));
			pushCommand.call();
			logger.debug("push repository operation done with success");
			return true;
		} catch (GitAPIException e) {
			logger.error("error throw push operation :GitAPIException "
					+ e.getMessage());
			return false;
		}

	}

	@Override
	public boolean pullRepository() {

		logger.info("pull repository operation started !!!");
		try {
			git = new Git(repository);
			StoredConfig config = repository.getConfig();
			RemoteConfig remoteConfig;
			remoteConfig = new RemoteConfig(config, "origin");
			URIish uri = new URIish(url);
			remoteConfig.addURI(uri);
			remoteConfig.update(config);
			PullCommand putchCommand = git.pull();
			if (jJContact != null)
				putchCommand
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
								jJContact.getName(), jJContact.getPassword()));

			putchCommand.call();
			logger.debug("pull repository operation done with success");
			return true;
		} catch (URISyntaxException e) {

			logger.error("error throw pull operation :URISyntaxException "
					+ e.getMessage());
			return false;
		} catch (WrongRepositoryStateException e) {
			logger.error("error throw pull operation :WrongRepositoryStateException "
					+ e.getMessage());
			return false;
		} catch (InvalidConfigurationException e) {
			logger.error("error throw pull operation :InvalidConfigurationException "
					+ e.getMessage());
			return false;
		} catch (DetachedHeadException e) {
			logger.error("error throw pull operation :DetachedHeadException "
					+ e.getMessage());
			return false;
		} catch (InvalidRemoteException e) {
			logger.error("error throw pull operation :InvalidRemoteException "
					+ e.getMessage());
			return false;
		} catch (CanceledException e) {
			logger.error("error throw pull operation :CanceledException "
					+ e.getMessage());
			return false;
		} catch (RefNotFoundException e) {
			logger.error("error throw pull operation :RefNotFoundException "
					+ e.getMessage());
			return false;
		} catch (NoHeadException e) {
			logger.error("error throw pull operation :NoHeadException "
					+ e.getMessage());
			return false;
		} catch (TransportException e) {
			logger.error("error throw pull operation :TransportException "
					+ e.getMessage());
			return false;
		} catch (GitAPIException e) {
			logger.error("error throw pull operation :GitAPIException "
					+ e.getMessage());
			return false;
		}

	}

	@Override
	public boolean addFile(String path, String name, boolean isFile) {

		path = path.replace(repository.getDirectory().getParent(), "");
		File myfile = new File(repository.getDirectory().getParent() + "/"
				+ path, name);
		logger.info("add file operation started !!!");
		try {

			if (!isFile)
				myfile.mkdirs();
			myfile.createNewFile();
			git.add().addFilepattern(".").call();
			logger.info("Add file " + myfile + " to repository at "
					+ repository.getDirectory());
			return true;
		} catch (IOException e) {
			File directory = new File(repository.getDirectory().getParent()
					+ "/" + path);
			if (directory.mkdirs()) {
				try {
					git.add().addFilepattern(".").call();
					myfile.createNewFile();
					git.add().addFilepattern(".").call();
					logger.info("Add file " + myfile + " to repository at "
							+ repository.getDirectory());
					return true;
				} catch (IOException e1) {

					logger.error("error throw add file operation :IOException"
							+ e1.getMessage());
					return false;
				} catch (NoFilepatternException e1) {
					logger.error("error throw add file operation :NoFilepatternException"
							+ e1.getMessage());

					return false;
				} catch (GitAPIException e1) {
					logger.error("error throw add file operation :GitAPIException"
							+ e1.getMessage());

					return false;
				}

			} else {
				logger.error("error throw add file operation :IOException"
						+ e.getMessage());

				return false;
			}
		} catch (NoFilepatternException e) {
			logger.error("error throw add file operation :NoFilepatternException"
					+ e.getMessage());
			return false;
		} catch (GitAPIException e) {
			logger.error("error throw add file operation :GitAPIException"
					+ e.getMessage());
			return false;
		}

	}

	@Override
	public ArrayList<String> getAllBranches() {
		try {
			List<Ref> refs = git.branchList().call();
			ArrayList<String> list = new ArrayList<String>();
			for (Ref ref : refs) {
				list.add(ref.getName().replace("refs/heads/", ""));

			}
			return list;
		} catch (GitAPIException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean setFileTexte(File file, String texte) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(texte);
			writer.close();
			return true;
		} catch (IOException e) {

			return false;
		}

	}

	public static void delete(File file) throws IOException {

		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}

		} else {
			// if file, then delete it
			file.delete();
		}
	}

}
