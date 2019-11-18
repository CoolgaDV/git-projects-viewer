package cdv.utils.viewer.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ViewerConfiguration {

    private String rootDirectory;
    private List<ProjectConfiguration> projectSettings;

}
