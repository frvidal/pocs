package com.test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RenameCallback;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.junit.Test;

public class GitLogExplorationTest {

	private String SPRING_FITZHI_DIR = "../../first-test/";

	// private String SPRING_FRAMEWORK_DIR = "../../spring-framework";

	// private String SPRING_BOOT_DIR = "../../spring-boot";

	// private String SPRING_ANGULAR_DIR = "../../angular";

	// private String SPRING_MATERIAL_DIR = "../../material";

	// private String SPRING_KUBERNETES_DIR = "../../kubernetes";

	@Test
	public void list() throws Exception {  
		Git git = Git.open(Paths.get(SPRING_FITZHI_DIR).toFile());
		var commits = fileGitHistory(git.getRepository(), "com/application/packageB/MyClass.java");
		var i = 0;
		for (RevCommit rev : commits) {
			System.out.println(rev.getShortMessage());
			i++;
		}
		System.out.println("i " + i);
	}


	List<RevCommit> fileGitHistory(Repository repository, String filePath)
			throws Exception {

		List<RevCommit> commits = new ArrayList<>();

		try (RevWalk rw = new RevWalk(repository)) {
			var diffCollector = new DiffCollector();

			rw.setTreeFilter(getFollowFilter(repository, filePath));
			rw.markStart(rw.parseCommit(repository.resolve(Constants.HEAD)));
			
			// We do not include the merge operations in the scope of the audit.
			rw.setRevFilter(RevFilter.NO_MERGES);

			for (RevCommit c : rw) {
				commits.add(c);
			}

		} catch (final Exception e) {
			throw e;
		}
		return commits;
	}

	FollowFilter getFollowFilter(Repository repository, String filePath) throws Exception {

		var diffCollector = new DiffCollector();
		var config = repository.getConfig();
		config.setBoolean("diff", null, "renames", true);
		config.setInt("diff", null, "renameLimit", Integer.MAX_VALUE);
		var dc = config.get(DiffConfig.KEY);
		
		var followFilter = FollowFilter.create(filePath, dc);
		followFilter.setRenameCallback(diffCollector);

		return followFilter;
	}

	static class DiffCollector extends RenameCallback {
		List<DiffEntry> diffs = new ArrayList<DiffEntry>();

		@Override
		public void renamed(DiffEntry diff) {
			diffs.add(diff);
		}
	}

}
