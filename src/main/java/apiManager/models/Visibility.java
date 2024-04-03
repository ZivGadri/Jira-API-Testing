package apiManager.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Visibility {
    private String type;
    private String value;

    public Visibility() {
        this.type = "role";
        this.value = "Administrator";
    }

}
