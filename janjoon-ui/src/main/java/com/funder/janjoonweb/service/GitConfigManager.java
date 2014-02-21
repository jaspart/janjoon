package com.funder.janjoonweb.service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class GitConfigManager extends ConfigManagerAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Repository repository;
	private Git git;
	
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(String path) {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
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

	public void setGit(String path) {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
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

	public GitConfigManager(String chemin) {
		setType("GIT");
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			File file;
			if (chemin.endsWith(".git"))
				file = new File(chemin);
			else
				file = new File(chemin + "/.git");
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

	// commit a repository
	@Override
	public boolean checkIn(String message) {
		try {
			CommitCommand commit = git.commit();
			commit.setAuthor("lazher", "");
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
			//checkOutCommand.setAllPaths(true);
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
			Repository newRepo = new FileRepository(path + ".git");
			newRepo.create();
			System.out
					.println(repository.getDirectory().getName() + " Created");
			return true;
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public TreeNode listRepositoryContent(String branche) {
		if (getAllBranches().contains(branche)) 
		{
			checkOut(branche);
			DefaultTreeNode root = new DefaultTreeNode("folder",repository.getWorkTree(), null);
			repositoryTreeNode(repository.getWorkTree(), root);
			return root;
		} else
		{
			System.out.println(branche+"branche n'existe pas");
			return null;
		}
	}

	private void repositoryTreeNode(File workTree, DefaultTreeNode root) {
		File[] files = workTree.listFiles();
		int i = 0;
		while (i < files.length) {
			if (!files[i].getName().equalsIgnoreCase(".git")) 
			{
				System.out.println(files[i].getName());
				if (files[i].isDirectory()) 
				{
					DefaultTreeNode tree = new DefaultTreeNode("folder",files[i], root);
					repositoryTreeNode(files[i], tree);
					File[] t = files[i].listFiles();
					int j = 0;
					while (j < t.length) 
					{
						System.out.println("     " + t[j].getName());
						j++;
					}
					
				}else
				{
					new DefaultTreeNode("file",files[i], root);
				}
				
			}
			i++;
		}
	}

	@Override
	public String cloneRemoteRepository(String url, String name,
			String username, String password) {
		CloneCommand cloneCommand = Git.cloneRepository();
		File file = new File(repository.getDirectory().getPath() + "/" + name);
		cloneCommand.setDirectory(file);
		cloneCommand.setURI(url);

		if (username != null) {
			cloneCommand
					.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
							username, password));
		}
		try {
			cloneCommand.call();
			System.out.println("cloneRemoteRepository getted");
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repo;
			try {
				file = new File(repository.getDirectory().getPath() + "/"
						+ name + "/.git");
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
			return null;
		} catch (TransportException e) {
			System.out.println("TransportException getted");
			e.printStackTrace();
			return null;
		} catch (GitAPIException e) {
			System.out.println("GitAPIException getted");
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean pushRepository(String path, String url, String userName,
			String password) {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();

		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
			else
				file = new File(path + "/.git");

			Repository rep = builder.setGitDir(file)
					.setWorkTree(file.getParentFile())
					// scan up the file system tree
					.build();
			git = new Git(repository);
			Git git = new Git((Repository) rep);

			PushCommand pushCommand = git.push();
			pushCommand.setRemote(url);

			pushCommand.setPushAll();

			pushCommand.setForce(true);
			// pushCommand.

			if (userName != null)
				pushCommand
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
								userName, password));

			pushCommand.call();
			System.out.println("push called");
			return true;
		} catch (InvalidRemoteException e) {
			System.out.println("InvalidRemoteException");
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
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean pullRepository(String path, String url, String userName,
			String password) {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();

		try {
			File file;
			if (path.endsWith(".git"))
				file = new File(path);
			else
				file = new File(path + "/.git");

			Repository rep = builder.setGitDir(file)
					.setWorkTree(file.getParentFile())
					// scan up the file system tree
					.build();
			git = new Git(rep);
			StoredConfig config = rep.getConfig();
			RemoteConfig remoteConfig = new RemoteConfig(config, "origin");
			URIish uri = new URIish(url);
			remoteConfig.addURI(uri);
			remoteConfig.update(config);

			Git git = new Git(rep);
			PullCommand putchCommand = git.pull();
			if (userName != null)
				putchCommand
						.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
								userName, password));

			// RefSpec spec = new RefSpec("refs/heads/*:refs/remotes/origin/*");
			putchCommand.call();
			System.out.println(rep.getDirectory().getParent() + " pulled");
			return true;
		} catch (InvalidRemoteException e) {
			System.out.println("InvalidRemoteException");
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
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean addFile(String path, String name) {

		File myfile = new File(repository.getDirectory().getParent() + "/"
				+ path, name);
		try {

			System.out.println(myfile.getPath());
			myfile.createNewFile();
			git.add().addFilepattern(".").call();

			// git.commit().setMessage(name + " added").setAll(true)
			// .call();
			System.out.println("Added file " + myfile + " to repository at "
					+ repository.getDirectory());
			return true;
		} catch (IOException e) {
			File directory = new File(repository.getDirectory().getParent()
					+ "/" + path);
			if (directory.mkdirs()) {
				try {
					git.add().addFilepattern(".").call();
					// git.commit().setMessage(directory.getName() +
					// " added").setAll(true)
					// .call();
					myfile.createNewFile();
					git.add().addFilepattern(".").call();
					// git.commit().setMessage(name + " added").setAll(true)
					// .call();
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

		// run the add-call

	}

	@Override
	public ArrayList<String> getAllBranches() {
		try {
			List<Ref> refs = git.branchList().call();
			ArrayList<String> list = new ArrayList<String>();
			for (Ref ref : refs) {
				//System.out.println(ref.getName() + "   " + ref.toString());
				list.add(ref.getName().replace("refs/heads/", ""));
				//System.out.println(ref.getName().replace("refs/heads/", ""));
			}
			return list;
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean setFileTexte(File file, String texte) {
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter(file));
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

}
