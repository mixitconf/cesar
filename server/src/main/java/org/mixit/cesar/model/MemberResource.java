package org.mixit.cesar.model;

import java.util.Objects;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * Member DTO
 */
public class MemberResource extends ResourceSupport{

    private Long idMember;
    private String login;
    private String email;
    private String firstname;
    private String lastname;
    private String company;
    private String shortDescription;
    private String longDescription;

    public static MemberResource convert(Member member){
        MemberResource resource = new MemberResource()
                .setIdMember(member.getId())
                .setLogin(member.getLogin())
                .setEmail(member.getEmail())
                .setFirstname(member.getFirstname())
                .setLastname(member.getLastname())
                .setCompany(member.getCompany())
                .setShortDescription(member.getShortDescription())
                .setLongDescription(member.getLongDescription());
        resource.add(ControllerLinkBuilder.linkTo(MemberResource.class).slash("member").slash(member.getId()).withSelfRel());
        return resource;
    }

    public Long getIdMember() {
        return idMember;
    }

    public MemberResource setIdMember(Long idMember) {
        this.idMember = idMember;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public MemberResource setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public MemberResource setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public MemberResource setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public MemberResource setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public MemberResource setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public MemberResource setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public MemberResource setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MemberResource that = (MemberResource) o;
        return Objects.equals(idMember, that.idMember) &&
                Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idMember, login);
    }
}
