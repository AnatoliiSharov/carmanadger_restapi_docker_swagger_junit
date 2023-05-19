package ua.foxminded.asharov.cardatamanager.exception;

import org.springframework.http.HttpStatus;

public class ResourceIsStillUsedException  extends ApiException {


    public ResourceIsStillUsedException(String resourceName) {
        this(resourceName, null);
    }

    public ResourceIsStillUsedException(String resourceName, String additionalInfo) {
        super(HttpStatus.BAD_REQUEST, String.format("Resource can not be deleted cause used '%s'%s", resourceName,
                additionalInfo != null ? String.format("(%s)", additionalInfo) : ""));
    }
    
}
