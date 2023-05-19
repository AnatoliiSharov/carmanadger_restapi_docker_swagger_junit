package ua.foxminded.asharov.cardatamanager.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ResourceAlreadyExistsException extends ApiException {

    public ResourceAlreadyExistsException(String resourceName) {
        this(resourceName, null);
    }

    public ResourceAlreadyExistsException(String resourceName, String additionalInfo) {
        super(HttpStatus.BAD_REQUEST, String.format("Resource '%s'%s already exists", resourceName,
                additionalInfo != null ? String.format("(%s)", additionalInfo) : ""));
    }

}
