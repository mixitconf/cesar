package org.mixit.cesar.cfp.model;

public class ProposalError {
    public enum Code {
        REQUIRED
    };

    private Code code;
    private String property;

    public Code getCode() {
        return code;
    }

    public ProposalError setCode(Code code) {
        this.code = code;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public ProposalError setProperty(String property) {
        this.property = property;
        return this;
    }
}
