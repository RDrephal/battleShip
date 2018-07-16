package rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BooleanModel {

    private Boolean requestAccept;

    public BooleanModel(Boolean requestAccept) {
        this.requestAccept = requestAccept;
    }

    @JsonProperty
    public Boolean getRequestAccept() {
        return requestAccept;
    }
}
