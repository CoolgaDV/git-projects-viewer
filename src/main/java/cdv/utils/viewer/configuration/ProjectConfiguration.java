package cdv.utils.viewer.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ProjectConfiguration {

    private String name;
    private String url;
    private String directory;

}
