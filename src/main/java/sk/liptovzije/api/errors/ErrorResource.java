package sk.liptovzije.api.errors;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@lombok.Getter
@JsonRootName("errors")
public class ErrorResource {
    private List<FieldErrorResource> fieldErrors;

    public ErrorResource(List<FieldErrorResource> fieldErrorResources) {
        this.fieldErrors = fieldErrorResources;
    }
}
