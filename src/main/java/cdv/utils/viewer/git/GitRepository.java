package cdv.utils.viewer.git;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class GitRepository {

    private static final String FAILURE_MESSAGE =
            "Unable to execute operation with Git";

    private final Path location;

    public void clone(String url) {
        try {
            Git.cloneRepository()
                    .setDirectory(location.toFile())
                    .setURI(url)
                    .call();
        } catch (Exception e) {
            throw new RuntimeException(FAILURE_MESSAGE, e);
        }
    }

    public boolean exists() {
        return Files.isDirectory(location) &&
                Files.isDirectory(location.resolve(".git"));
    }

    public void pull() {
        executeGitOperation(git -> git.pull().call());
    }

    public Optional<String> findLastTagId() {
        return executeGitOperation(git -> {
            List<Ref> tags = git.tagList().call();
            return tags.isEmpty()
                    ? Optional.empty()
                    : Optional.of(tags.get(0).getObjectId().getName());
        });
    }

    public List<String> collectCommitsAfterTag(String tag) {
        return executeGitOperation(git -> {
            var tagId = ObjectId.fromString(tag);
            return StreamSupport.stream(git.log().call().spliterator(), false)
                    .takeWhile(commit -> !commit.getId().equals(tagId))
                    .map(RevCommit::getFullMessage)
                    .collect(Collectors.toList());
        });
    }

    public List<String> collectAllCommits() {
        return executeGitOperation(git ->
                StreamSupport.stream(git.log().call().spliterator(), false)
                        .map(RevCommit::getFullMessage)
                        .map(this::formatCommitMessage)
                        .collect(Collectors.toList()));
    }

    private <T> T executeGitOperation(GitOperation<T> operation) {
        try {
            try (var git  = Git.open(location.toFile())) {
                return operation.execute(git);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to execute operation with Git", e);
        }
    }

    private String formatCommitMessage(String message) {
        return Arrays.stream(message.split("\n"))
                .map(String::trim)
                .collect(Collectors.joining("\n   "));
    }

    @FunctionalInterface
    private interface GitOperation<T> {

        T execute(Git git) throws Exception;

    }

}
