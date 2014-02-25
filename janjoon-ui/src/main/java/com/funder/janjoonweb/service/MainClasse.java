package com.funder.janjoonweb.service;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.primefaces.model.TreeNode;



public class MainClasse {

	public static void main(String[] args) throws NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, GitAPIException, IOException {
		
		
		GitConfigManager CM=new GitConfigManager("/home/lazher/git/.git/Repository/TestRepository/.git");
		
		//CM.checkIn("gfhfhsfg");
		System.out.println(CM.getRepository().getRepositoryState());
		//System.out.println(CM.getRepository().getWorkTree());
		//CM.getAllBranches();
		//TreeNode tree=CM.listRepositoryContent();
		
		
		/*while(tree.getChildCount()!=0)
		{
			tree=tree.getChildren().get(0);
			System.out.println(tree.getData().getClass());
		}	
		File file=(File) tree.getData();
		System.out.println(file);
		CM.setFileTexte(file, "fvgdfhi: dfvjmqdfv dfmvqjd dfqjm,f");*/
		
		
		Iterable<RevCommit> commits = CM.getGit().log().all().call();
		CM.getGit().checkout().setName(commits.iterator().next().getName());
		TreeNode tree=CM.listRepositoryContent();
		while(tree.getChildCount()!=0)
		{
			tree=tree.getChildren().get(0);
			System.out.println(tree.getData().getClass());
		}	
		System.out.println("pppopppoppp"+commits.iterator().next().getName());
		for (RevCommit commit : commits) {
		   //CM.getGit().checkout().setName(commit.getName());
			System.out.println(commit.getName());
		}
		//commits.iterator().next();
		

		//CM.checkOut();
		//CM.createRepository("/home/lazher/git/.git/newRepo");
		//CM.cloneRemoteRepository("https://github.com/chemakh/TestRepository.git", "TestCloneRepository","chemakh", "taraji0000");
		//CM.checkIn("3");
		//CM.pullRepository(CM.getRepository(), "https://github.com/chemakh/TestRepository.git","chemakh", "taraji0000");
		/*CM.addFile("dir1", "create2.txt");
		CM.addFile("","hetkhouth");
		CM.addFile("dir1/dir2", "create2.txt");
		CM.addFile("dir1/dir2/dir3", "create2.txt");
		//RevCommit rev=CM.getGit().commit().setMessage("test").call();*/
		//System.out.println(rev.toString());
		//CM.checkIn("commit");
		//CM.pushRepository("/home/lazher/git/Repository/.git/TestCloneRepository/.git",
				//"https://github.com/chemakh/TestRepository.git", "chemakh", "taraji0000");
	}
	}
