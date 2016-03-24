package org.mixit.cesar.site.web.dto;

import java.util.Objects;

import org.mixit.cesar.site.model.member.Member;
import org.mixit.cesar.site.utils.HashUtil;
import org.springframework.hateoas.ResourceSupport;

/**
 * Member DTO for API HATEOAS
 */
public class MemberDto extends ResourceSupport {

    private Long idMember;
    private String firstname;
    private String lastname;
    private String hash;

    public static <T extends Member<T>> MemberDto convert(T member) {

        MemberDto memberResource = new MemberDto()
                .setIdMember(member.getId())
                .setFirstname(member.getFirstname())
                .setLastname(member.getLastname())
                .setHash(HashUtil.md5Hex(member.getEmail()));

        return memberResource;
    }

    public Long getIdMember() {
        return idMember;
    }

    public MemberDto setIdMember(Long idMember) {
        this.idMember = idMember;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public MemberDto setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public MemberDto setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public MemberDto setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MemberDto that = (MemberDto) o;
        return Objects.equals(idMember, that.idMember);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idMember);
    }
}
