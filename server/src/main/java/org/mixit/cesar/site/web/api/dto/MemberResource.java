package org.mixit.cesar.site.web.api.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mixit.cesar.site.model.Tuple;
import org.mixit.cesar.site.model.member.Interest;
import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.model.member.Sponsor;
import org.mixit.cesar.site.utils.HashUtil;
import org.mixit.cesar.site.web.api.MemberController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

/**
 * Member DTO for API HATEOAS
 */
public class MemberResource extends ResourceSupport {

    private Long idMember;
    private String login;
    private String email;
    private String firstname;
    private String lastname;
    private String company;
    private String logo;
    private String hash;
    private String sessionType;
    private String shortDescription;
    private String longDescription;
    private List<Tuple> userLinks = new ArrayList<>();
    private List<String> interests = new ArrayList<>();
    private List<Long> sessions = new ArrayList<>();
    private List<Tuple> level = new ArrayList<>();

    public static <T extends Member<T>> MemberResource convert(T member) {

        MemberResource memberResource = new MemberResource()
                .setIdMember(member.getId())
                .setLogin(member.getLogin())
                .setEmail(member.getEmail())
                .setFirstname(member.getFirstname())
                .setLastname(member.getLastname())
                .setCompany(member.getCompany())
                .setShortDescription(member.getShortDescription())
                .setLongDescription(member.getLongDescription())
                .setHash(HashUtil.md5Hex(member.getEmail()));

        if (!member.getSessions().isEmpty()) {
            memberResource.setSessions(member.getSessions()
                    .stream()
                    .filter(s -> Boolean.TRUE.equals(s.getSessionAccepted()))
                    .map(s -> s.getId())
                    .collect(Collectors.toList()));
        }
        if (!member.getMemberEvents().isEmpty()) {
            memberResource.setLevel(member
                    .getMemberEvents()
                    .stream()
                    .map(e -> new Tuple().setKey(Objects.toString(e.getLevel(), "")).setValue(e.getRegisteredAt()))
                    .collect(Collectors.toList()));
        }
        if (!member.getInterests().isEmpty()) {
            memberResource.setInterests(member.getInterests()
                    .stream()
                    .map(Interest::getName)
                    .collect(Collectors.toList()));
        }
        if (!member.getSharedLinks().isEmpty()) {
            memberResource.setUserLinks(member.getSharedLinks()
                    .stream()
                    .map(l -> new Tuple().setKey(l.getName()).setValue(l.getURL()))
                    .collect(Collectors.toList()));
        }
        if (member instanceof Sponsor) {
            memberResource
                    .setLogo(((Sponsor) member).getLogoUrl());
        }
        memberResource.add(ControllerLinkBuilder.linkTo(MemberController.class).slash(member.getId()).withSelfRel());

        return memberResource;
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

    public String getLogo() {
        return logo;
    }

    public MemberResource setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public MemberResource setHash(String hash) {
        this.hash = hash;
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

    public List<Tuple> getLevel() {
        return level;
    }

    public MemberResource setLevel(List<Tuple> level) {
        this.level = level;
        return this;
    }

    public List<Tuple> getUserLinks() {
        return userLinks;
    }

    public MemberResource setUserLinks(List<Tuple> userLinks) {
        this.userLinks = userLinks;
        return this;
    }

    public List<String> getInterests() {
        return interests;
    }

    public MemberResource setInterests(List<String> interests) {
        this.interests = interests;
        return this;
    }

    public String getSessionType() {
        return sessionType;
    }

    public MemberResource setSessionType(String sessionType) {
        this.sessionType = sessionType;
        return this;
    }

    public List<Long> getSessions() {
        return sessions;
    }

    public MemberResource setSessions(List<Long> sessions) {
        this.sessions = sessions;
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
