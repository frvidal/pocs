package com.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand.FastForwardMode;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.junit.Test;

public class CommitsExplorationTest {

	public static final String LN = System.getProperty("line.separator");

	// private String TEST_DIR = "../../test";

	// private String FITZHI_DIR = "../../application";

	private String SPRING_FRAMEWORK_DIR = "../../spring-framework";

	// private String SPRING_BOOT_DIR = "../../spring-boot";

	// private String ANGULAR_DIR = "../../angular";

	// private String MATERIAL_DIR = "../../material";

	// private String KUBERNETES_DIR = "../../kubernetes";

	@Test
	public void list() throws Exception {  
		Git git = Git.open(Paths.get(SPRING_FRAMEWORK_DIR).toFile());
		var walk = new RevWalk(git.getRepository());

		var complete = false;

		var limit = 2;

		var total = 0;
		var selected = 0;
		var start = git.getRepository().resolve(Constants.HEAD);

		var fixRecords = new ArrayList<FixedFile>();

		while (!complete) {

			total++;
			var id = git.getRepository().resolve(Constants.HEAD);
			var lastCommit = walk.parseCommit( id );	
			/*
			if (true) {
				var file = new FixedFile(lastCommit.getId(), lastCommit.getShortMessage(), "entry.getNewPath()");
				file.modifiedLinesCommit.add(new CommitOperation(1, 2, 1, 4, "blockContent"));
				var r = new ArrayList<FixedFile>();
				r.add(file);
				var filename = String.format("file-%s-%d.json", "spring-framework", limit);
				saveFile(Paths.get(filename).toFile().getAbsolutePath(), r);
				complete = true;
				return;
			}
			*/


			if (lastCommit != null) {
				complete =  (lastCommit.getParentCount() == 0);
				if (!complete) {
					// We avoid all commit merge
					if (lastCommit.getParentCount() == 1) {
						var message = lastCommit.getShortMessage();
						if (message.toLowerCase().contains("fix")) {
							var files = this.revTree(message, git, lastCommit);
							// We just work on simple commit with just one file.
							if (files.size() <= limit) {
								selected++;
								fixRecords.addAll(files);
							}
						}
					}
					git.reset().setRef("Head~1").setMode(org.eclipse.jgit.api.ResetCommand.ResetType.HARD).call();
				}
			}
		}
		fixRecords.stream().forEach(file -> {
			System.out.println(file.shortMessage);
			System.out.println("----------------------------");
			file.modifiedLinesCommit.stream().forEach(lines -> {
				System.out.println(lines.blockContent);
			});
		});

		System.out.println("Total commits " + total);	
		System.out.println("Selected commits " + selected);	

		var filename = String.format("file-%s-%d.json", "spring-framework", limit);
		saveFile(Paths.get(filename).toFile().getAbsolutePath(), fixRecords);

		git.checkout().setName(git.getRepository().getBranch()).call();
		git.merge().setFastForward(FastForwardMode.FF_ONLY).setCommit(false).include(start).call();		
	}

	private List<FixedFile> revTree(String shortMessage, Git git, RevCommit commit) throws Exception {

		var repository = git.getRepository();
		List<FixedFile> files = new ArrayList<>();
		try (var revWalk = new RevWalk(repository)) {
			var parent = revWalk.parseCommit(commit.getParent(0).getId());
			if (parent != null) {
				try (var formater = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
					formater.setRepository(repository);
					formater.setDiffComparator(RawTextComparator.DEFAULT);
					formater.setDetectRenames(true);
					List<DiffEntry> diffs = formater.scan(parent.getTree(), commit.getTree());
					// We onboard only simple commit regarding 1 file.
					if (diffs.size() == 1) {
						for (DiffEntry entry : diffs) {
							if (entry.getNewPath().contains(".java")) {
								if (!isJavaTestFile(Paths.get(SPRING_FRAMEWORK_DIR + "/" + entry.getNewPath()))) {
									var file = new FixedFile(commit.getId(), commit.getShortMessage(), entry.getNewPath());
									files.add(file);
									lineModified(git, file, formater, entry);
								}
							}
						}
					} else {
						System.out.print(".");
					}
				}
			}
		} catch (final MalformedInputException mfie) {
			mfie.printStackTrace();
			return new ArrayList<>();
		}
		return files;
	}

	String loadFile(Path path) {

		var sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(sb::append);
        }
        catch (IOException | UncheckedIOException e) {
			System.out.println ("Error with path " + path);
            e.printStackTrace();
			return null;
        }
		
		return sb.toString();

	}

	public void lineModified(Git git, FixedFile file, DiffFormatter formater,  DiffEntry entry) throws Exception {
		if (entry.getChangeType() != ChangeType.MODIFY) {
			return;
		}
		final var fileHeader = formater.toFileHeader(entry);
		final var editList = fileHeader.toEditList();
		// System.out.println(entry);
		var edits = editList.listIterator();
		var numberOfCodeBlocks = 0;
		while (edits.hasNext()) {
			var edit = edits.next();
			if (numberOfCodeBlocks++ > 1) {
				file.evicted = true;
				return;
			}
			// System.out.println(edit);
			
			var sbModifiedBlock = new StringBuilder();
			for (int i = edit.getBeginB(); i < edit.getEndB(); i++) {
				var blameCommand = new BlameCommand(git.getRepository());
				var result = blameCommand.setFilePath(file.pathFile).setFollowFileRenames(true).call();
				sbModifiedBlock.append(result.getResultContents().getString(result.getSourceLine(i))).append(LN);
			}
				
			CommitOperation commitOperation = new CommitOperation(edit.getBeginA(), edit.getEndA(), edit.getBeginB(), edit.getEndB(), sbModifiedBlock.toString());
			file.modifiedLinesCommit.add(commitOperation);
		}
	}

	private boolean isJavaTestFile(Path path) {

		if (path.toFile().getAbsolutePath().contains ("/src/test/java")) {
			return true;
		}

		var content = loadFile(path);
		if (content == null) {
			return true;
		}

		return content.contains("import org.junit.jupiter.api.Test;");
	}

	private void saveFile(String filename, List<FixedFile> fixRecords) {

		System.out.println("Saving " + fixRecords.size() + " records...");
		var gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		try (FileWriter fw = new FileWriter(filename)) {
			var content = gson.toJson(fixRecords);
			fw.write(content);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.out.println("...saved done.");

	}
}
