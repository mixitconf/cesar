package org.mixit.cesar.cfp.model;

public class ProposalError {
    public enum Code {
        REQUIRED
    };
    public enum Entity {
        MEMBER, PROPOSAL
    };

    private Code code;
    private String property;
    private Entity entity;

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

    public Entity getEntity() {
        return entity;
    }

    public ProposalError setEntity(Entity entity) {
        this.entity = entity;
        return this;
    }
}
