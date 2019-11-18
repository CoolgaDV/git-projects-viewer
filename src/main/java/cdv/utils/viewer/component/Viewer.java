package cdv.utils.viewer.component;

import cdv.utils.viewer.configuration.ViewerConfiguration;
import cdv.utils.viewer.git.GitClient;
import cdv.utils.viewer.git.GitRepository;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class Viewer {

    private final ViewerConfiguration configuration;
    private final GitClient gitClient;

    public void printCommitsAfterLastTag() throws IOException {

        Path root = Path.of(configuration.getRootDirectory());
        if (Files.notExists(root)) {
            Files.createDirectories(root);
        }

        configuration.getProjectSettings().forEach(project -> {

            Path projectDirectory = root.resolve(project.getDirectory());
            GitRepository repository = gitClient.getRepository(projectDirectory);

            if (!repository.exists()) {
                repository.clone(project.getUrl());
            } else {
                repository.pull();
            }

            List<String> commits = repository.findLastTagId()
                    .map(repository::collectCommitsAfterTag)
                    .orElse(repository.collectAllCommits());

            if (commits.isEmpty()) {
                return;
            }

            System.out.println("\n" + project.getName());
            commits.forEach(commit -> System.out.println(" - " + commit));
        });
    }

}
