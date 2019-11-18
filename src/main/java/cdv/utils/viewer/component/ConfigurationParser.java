package cdv.utils.viewer.component;

import cdv.utils.viewer.configuration.ViewerConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigurationParser {

    public ViewerConfiguration parse(Path path) throws IOException {
        return new ObjectMapper(new YAMLFactory()).readValue(
                path.toFile(),
                ViewerConfiguration.class);
    }

}
