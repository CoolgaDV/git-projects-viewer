package cdv.utils.viewer.git;

import java.nio.file.Path;

public class GitClient {

    public GitRepository getRepository(Path repositoryLocation) {
        return new GitRepository(repositoryLocation);
    }

}
