package com.starit.janjoonweb.ui.mb.util.service;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class TestClass {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws SVNException {
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL
				.parseURIDecoded("https://svn.riouxsvn.com/testchemakh"));
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager("chemakh", "taraji0000");
		repository.setAuthenticationManager(authManager);
		@SuppressWarnings({ "unused", "rawtypes" })
		Collection logEntries = repository.log(new String[] { "" }, null, 0,-1, true, true);
	}

	private static SVNCommitInfo addDir(ISVNEditor editor, String dirPath,
			String filePath, byte[] data) throws SVNException {
		editor.openRoot(-1);

		editor.addDir(dirPath, null, -1);

		editor.addFile(filePath, null, -1);

		editor.applyTextDelta(filePath, null);

		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(filePath,
				new ByteArrayInputStream(data), editor, true);

		editor.closeFile(filePath, checksum);

		// Closes dirPath.
		editor.closeDir();

		// Closes the root directory.
		editor.closeDir();

		return editor.closeEdit();
	}

	private static SVNCommitInfo modifyFile(ISVNEditor editor, String dirPath,
			String filePath, byte[] oldData, byte[] newData)
			throws SVNException {
		editor.openRoot(-1);

		editor.openDir(dirPath, -1);

		editor.openFile(filePath, -1);

		editor.applyTextDelta(filePath, null);

		SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(filePath,
				new ByteArrayInputStream(oldData), 0, new ByteArrayInputStream(
						newData), editor, true);

		// Closes filePath.
		editor.closeFile(filePath, checksum);

		// Closes dirPath.
		editor.closeDir();

		// Closes the root directory.
		editor.closeDir();

		return editor.closeEdit();
	}

	private static SVNCommitInfo copyDir(ISVNEditor editor, String srcDirPath,
			String dstDirPath, long revision) throws SVNException {
		editor.openRoot(-1);

		editor.addDir(dstDirPath, srcDirPath, revision);

		// Closes dstDirPath.
		editor.closeDir();

		// Closes the root directory.
		editor.closeDir();

		return editor.closeEdit();
	}

	private static SVNCommitInfo deleteDir(ISVNEditor editor, String dirPath)
			throws SVNException {
		editor.openRoot(-1);

		editor.deleteEntry(dirPath, -1);

		// Closes the root directory.
		editor.closeDir();

		return editor.closeEdit();
	}

	@SuppressWarnings("rawtypes")
	public static void listEntries(SVNRepository repository, String path)
			throws SVNException {

		Collection entries = repository.getDir(path, -1, null,
				(Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("/" + (path.equals("") ? "" : path + "/")
					+ entry.getName() + " ( author: '" + entry.getAuthor()
					+ "'; revision: " + entry.getRevision() + "; date: "
					+ entry.getDate() + ")");
			if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName()
						: path + "/" + entry.getName());
			}
		}
	}

}
