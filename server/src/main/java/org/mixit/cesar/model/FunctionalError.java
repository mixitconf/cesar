package org.mixit.cesar.model;

/**
 * DTO to send informations about the current exception
 */
public class FunctionalError {

    public enum Type {
        BAD_CREDENTIALS,
        REQUIRED_ARGS,
        USER_NOT_FOUND,
        UNAUTHORIZED,
        FORBIDDEN
    }

    private String type;
    private String message;
    private boolean functional = true;

    public String getType() {
        return type;
    }

    public FunctionalError setType(Type type) {
        this.type = type.toString();
        return this;
    }

    public String getMessage() {
        return message;
    }

    public FunctionalError setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isFunctional() {
        return functional;
    }
}
