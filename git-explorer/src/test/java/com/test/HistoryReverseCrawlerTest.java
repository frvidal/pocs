package com.test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.junit.Test;

public class HistoryReverseCrawlerTest {

	private String TEST_DIR = "../../test";

	// private String FITZHI_DIR = "../../application";

	// private String SPRING_FRAMEWORK_DIR = "../../spring-framework";

	// private String SPRING_BOOT_DIR = "../../spring-boot";

	// private String ANGULAR_DIR = "../../angular";

	// private String MATERIAL_DIR = "../../material";

	// private String KUBERNETES_DIR = "../../kubernetes";

	@Test
	public void reverse() throws Exception {  
		var git = Git.open(Paths.get(TEST_DIR).toFile());
		var walk = new RevWalk(git.getRepository());

		var complete = false;
		var tot = 0;
		
		List<Record> records = new ArrayList<>();

		while (!complete) {

			tot++;
			var id = git.getRepository().resolve(Constants.HEAD);
			var lastCommit = walk.parseCommit( id );	
			if (lastCommit != null) {
				complete =  (lastCommit.getParentCount() == 0);
				if (!complete) {
					if (lastCommit.getShortMessage().toLowerCase().contains("fix")) {
						System.out.println(lastCommit.getShortMessage());
						System.out.println("----------------------------");
						complete  = !revTree(records, git, lastCommit);
					} 
					git.reset().setRef("Head~1").setMode(org.eclipse.jgit.api.ResetCommand.ResetType.HARD).call();
				}
			}
		}
		System.out.println("Total commits " + tot);

	}

	private boolean revTree(List<Record> records, Git git, RevCommit commit) throws Exception {

		var repository = git.getRepository();
		try (var revWalk = new RevWalk(repository)) {
			var parent = revWalk.parseCommit(commit.getParent(0).getId());
			try (var formater = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
				formater.setRepository(repository);
				formater.setDiffComparator(RawTextComparator.DEFAULT);
				formater.setDetectRenames(true);
				List<FixedFile> paths = new ArrayList<>();
				List<DiffEntry> diffs = formater.scan(parent.getTree(), commit.getTree());
				for (DiffEntry entry : diffs) {
					var file = new FixedFile(commit.getId(), commit.getShortMessage(), entry.getNewPath());
					paths.add(file);
					lineModified(git, file, formater, entry);
				}
				/*
				if (numberOfFiles < 5) {
					Record rec = new Record(shortMessage);
					paths.stream().forEach(System.out::println);
					rec.paths.addAll(paths);
					records.add(rec);
				}
				*/
			}
		}
		System.out.println();
		return true;
	}

	public void lineModified(Git git, FixedFile file, DiffFormatter formater,  DiffEntry entry) throws Exception {
		if (entry.getChangeType() != ChangeType.MODIFY) {
			return;
		}
		final var fileHeader = formater.toFileHeader(entry);
		final var editList = fileHeader.toEditList();
		System.out.println(entry);
		var edits = editList.listIterator();
		while (edits.hasNext()) {
			var edit = edits.next();
			System.out.println(edit);
			for (int i = edit.getBeginB(); i < edit.getEndB(); i++) {

				var blameCommand = new BlameCommand(git.getRepository());
				var result = blameCommand.setFilePath(file.pathFile).setFollowFileRenames(true).call();
				
				var origin = result.getSourceCommit(result.getSourceLine(i));
				// If origin == null, the line has been created within the commit.
				System.out.println(i + " " + result.getResultContents().getString(result.getSourceLine(i)));	
				if (origin != null) {
					System.out.println  ("\t\t\tfrom : " + origin. getShortMessage());
				}
			}
		}
	}
/*
	private SourceCodeDiffChange diffFile(DiffEntry diffEntry, DiffFormatter diffFormater)
			throws IOException, CorruptObjectException {
		int linesDeleted = 0;
		int linesAdded = 0;
		for (Edit edit : diffFormater.toFileHeader(diffEntry).toEditList()) {
			linesDeleted += edit.getEndA() - edit.getBeginA();
			linesAdded += edit.getEndB() - edit.getBeginB();
		}
		return new SourceCodeDiffChange(diffEntry.getNewPath(), linesDeleted, linesAdded);
*/


}
