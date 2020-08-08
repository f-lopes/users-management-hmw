package io.florianlopes.usersmanagement.api.users.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final Class<?> resourceType;
    private final String resourceId;

    public ResourceNotFoundException(Class<?> resourceType, String resourceId) {
        super(
                String.format(
                        "Resource %s with id %s not found",
                        resourceType.getSimpleName(), resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(Class<?> resourceType, String resourceId, String message) {
        super(message);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(
            Class<?> resourceType, String resourceId, String message, Throwable cause) {
        super(message, cause);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(Class<?> resourceType, String resourceId, Throwable cause) {
        super(cause);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public Class<?> getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
