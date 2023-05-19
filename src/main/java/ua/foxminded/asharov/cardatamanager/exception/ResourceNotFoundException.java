package ua.foxminded.asharov.cardatamanager.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String resourceName) {
        this(resourceName, null);
    }

    public ResourceNotFoundException(String resourceName, String additionalInfo) {
        super(HttpStatus.NOT_FOUND, String.format("Resource '%s'%s not found", resourceName,
                additionalInfo != null ? String.format("(%s)", additionalInfo) : ""));
    }

}
