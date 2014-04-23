package com.starit.janjoonweb.ui.mb.util.service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.primefaces.model.*;

import com.starit.janjoonweb.domain.JJContact;

public class GitConfigManager extends AbstractConfigManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Git git;
	private Repository repository;

	public Repository getRepository() {
		return (Repository) repository;
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
			System.out.println("GitConfigManager created");

		} catch (IOException e) {
			System.out.println("wrong Repository Path");
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
			System.out.println("GitConfigManager created");

		} catch (IOException e) {
			System.out.println("wrong Repository Path");
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

			System.out.println("GitConfigManager created");

		} catch (IOException e) {
			System.out.println("wrong Repository Path");
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
			System.out.println("GitConfigManager created");

		} catch (IOException e) {
			System.out.println("wrong Repository Path");
			e.printStackTrace();
		}

	}

	public GitConfigManager(int i, JJContact contact) {
		super("GIT", "", "", contact);
	}

	// commit a repository
	@Override
	public boolean checkIn(String message) {
		try {
			CommitCommand commit = git.commit();
			// commit.setAuthor(jJContact.getName(), "");
			commit.setMessage(message);
			commit.setAll(true);
			commit.call();
			System.out.println(repository.getDirectory().getName()
					+ " Commited");
			return true;
		} catch (NoHeadException e) {
			System.out.println("NoHeadException");
			e.printStackTrace();
			return false;
		} catch (NoMessageException e) {
			System.out.println("NoMessageException");
			e.printStackTrace();
			return false;
		} catch (UnmergedPathsException e) {
			System.out.println("UnmergedPathsException");
			e.printStackTrace();
			return false;
		} catch (ConcurrentRefUpdateException e) {
			System.out.println("ConcurrentRefUpdateException");
			e.printStackTrace();
			return false;
		} catch (WrongRepositoryStateException e) {
			System.out.println("WrongRepositoryStateException");
			e.printStackTrace();
			return false;
		} catch (GitAPIException e) {
			System.out.println("GitAPIException");
			e.printStackTrace();
			return false;
		}

	}

	// mettre à jour une repository par rapport au head
	@Override
	public boolean checkOut(String branche) {
		try {
			CheckoutCommand checkOutCommand = git.checkout();
			// checkOutCommand.setAllPaths(true);
			checkOutCommand.setName(branche);
			checkOutCommand.call();
			System.out.println(repository.getDirectory().getName()
					+ " checkedOut");
			return true;
		} catch (RefAlreadyExistsException e) {
			System.out.println("RefAlreadyExistsException");
			e.printStackTrace();
			return false;
		} catch (RefNotFoundException e) {
			System.out.println("RefNotFoundException");
			e.printStackTrace();
			return false;
		} catch (InvalidRefNameException e) {
			System.out.println("InvalidRefNameException");
			e.printStackTrace();
			return false;
		} catch (CheckoutConflictException e) {
			System.out.println("CheckoutConflictException");
			e.printStackTrace();
			return false;
		} catch (GitAPIException e) {
			System.out.println("GitAPIException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean createRepository(String path) {

		try {
			Repository newRepo;
			if (path.endsWith("/"))
				newRepo = new FileRepository(path + ".git");
			else
				newRepo = new FileRepository(path + "/.git");

			newRepo.create();
			System.out.println(newRepo.getDirectory().getPath() + " Created");
			return true;
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public TreeNode listRepositoryContent(String version) {

		System.out.println(repository.getWorkTree().getPath());

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
				// System.out.println(files[i].getName());
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
			System.out.println("cloneRemoteRepository getted");
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repo;
			try {
				file = new File(path + "/" + name + "/.git");
				repo = builder.setGitDir(file)
				// scan up the file system tree
						.build();
				System.out.println("repository getted"
						+ repo.getDirectory().getPath());
				return repo.getDirectory().getPath();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		} catch (InvalidRemoteException e) {
			System.out.println("InvalidRemoteException getted");
			e.printStackTrace();
			try {
				delete(new File(path + "/" + name));
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			return "InvalidRemoteException";
		} catch (TransportException e) {

			System.out.println("TransportException getted" + url);
			try {
				delete(new File(path + "/" + name));
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			return "TransportException";
		} catch (GitAPIException e) {

			System.out.println("GitAPIException getted");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean pushRepository() {

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
			System.out.println("push called");
			return true;
		} catch (GitAPIException e) {
			System.out.println("GitAPIException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean pullRepository() {

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
			System.out.println(repository.getDirectory().getParent()
					+ " pulled");
			return true;
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException");
			e.printStackTrace();
			return false;
		} catch (WrongRepositoryStateException e) {
			System.out.println("WrongRepositoryStateException");
			e.printStackTrace();
			return false;
		} catch (InvalidConfigurationException e) {
			System.out.println("InvalidConfigurationException");
			e.printStackTrace();
			return false;
		} catch (DetachedHeadException e) {
			System.out.println("DetachedHeadException");
			e.printStackTrace();
			return false;
		} catch (InvalidRemoteException e) {
			System.out.println("InvalidRemoteException");
			e.printStackTrace();
			return false;
		} catch (CanceledException e) {
			System.out.println("CanceledException");
			e.printStackTrace();
			return false;
		} catch (RefNotFoundException e) {
			System.out.println("RefNotFoundException");
			e.printStackTrace();
			return false;
		} catch (NoHeadException e) {
			System.out.println("NoHeadException");
			e.printStackTrace();
			return false;
		} catch (TransportException e) {
			System.out.println("TransportException");
			e.printStackTrace();
			return false;
		} catch (GitAPIException e) {
			System.out.println("GitAPIException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean addFile(String path, String name, boolean isFile) {

		path = path.replace(repository.getDirectory().getParent(), "");
		File myfile = new File(repository.getDirectory().getParent() + "/"
				+ path, name);
		try {

			System.out.println(myfile.getPath());
			if (!isFile)
				myfile.mkdirs();
			myfile.createNewFile();
			git.add().addFilepattern(".").call();
			System.out.println("Added file " + myfile + " to repository at "
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
					System.out.println("Added file " + myfile
							+ " to repository at " + repository.getDirectory());
					return true;
				} catch (IOException e1) {
					System.out.println("IOException");
					e.printStackTrace();
					return false;
				} catch (NoFilepatternException e1) {
					System.out.println("NoFilepatternException");
					e.printStackTrace();
					return false;
				} catch (GitAPIException e1) {
					System.out.println("GitAPIException");
					e.printStackTrace();
					return false;
				}

			} else {
				System.out.println("IOException");
				e.printStackTrace();
				return false;
			}
		} catch (NoFilepatternException e) {
			System.out.println("NoFilepatternException");
			e.printStackTrace();
			return false;
		} catch (GitAPIException e) {
			System.out.println("GitAPIException");
			e.printStackTrace();
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
			System.out.println("1");
			return true;
		} catch (IOException e) {
			System.out.println("IOException");
			System.out.println("2");
			e.printStackTrace();
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
