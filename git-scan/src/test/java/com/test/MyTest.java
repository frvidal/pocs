package com.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

public class MyTest {

	private String DIR_GIT = "../../application/";
	
	Git git(Path pathGit) throws IOException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(
			new File(String.format(pathGit.toString() + "/.git"))).readEnvironment().findGitDir()
				.build();
		final Git git = new Git(repository);
		return git;
	}

	/**
	 * Producing the equivalent of the command {@code git blame -L lineNumber,+1 filename}.
	 * 
	 * @param git the GIT repository
	 * @param filePath the filePath
	 * @param lineNumber the line number
	 */
	public PersonIdent getAuthor(Git git, String filePath, int lineNumber) throws GitAPIException {
		final BlameResult blameResult = git.blame().setFilePath(filePath).setFollowFileRenames(true).call();
		return blameResult.getSourceCommitter(lineNumber);
	} 

	@Test
	public void globalParser() throws IOException, GitAPIException {

		Path pathGitDir = Paths.get(new File(DIR_GIT).getAbsolutePath());
		final Git git = git(pathGitDir);

		// only parsing
		final ProjectRoot projectRoot = new ParserCollectionStrategy().collect(Paths.get(DIR_GIT+"/back-fitzhi"));
		for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
			List<ParseResult<CompilationUnit>> res = sourceRoot.tryToParse();
			for (ParseResult<CompilationUnit> pr : res) {
				if (pr.isSuccessful()) {
					if (!pr.getResult().isEmpty()) {
						CompilationUnit cu =  pr.getResult().get();
						List<MarkerAnnotationExpr> l = cu.findAll(
							MarkerAnnotationExpr.class, 
							Predicate.isEqual(new MarkerAnnotationExpr("Service")));
						for (MarkerAnnotationExpr mae : l) {
							if (mae.getBegin().isPresent()) {
								Path pathRelative = pathGitDir.relativize(cu.getStorage().get().getPath());
								System.out.println(
									String.format(
										"%s %s %s", 
										cu.getStorage().get().getFileName(),
										mae,
										this.getAuthor(git, pathRelative.toString(), mae.getBegin().get().line)
									)
								);
							}
						}
					}
				}
			}
		}
	}
}
