package cdv.utils.viewer;

import cdv.utils.viewer.component.ConfigurationParser;
import cdv.utils.viewer.component.Viewer;
import cdv.utils.viewer.configuration.ViewerConfiguration;
import cdv.utils.viewer.git.GitClient;

import java.io.IOException;
import java.nio.file.Path;

public class Application {

    public static void main(String[] args) throws IOException {

        ViewerConfiguration configuration = new ConfigurationParser()
                .parse(Path.of("configuration.yml"));

        new Viewer(configuration, new GitClient())
                .printCommitsAfterLastTag();
    }

}
