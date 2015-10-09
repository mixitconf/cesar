package org.mixit.cesar.model.security;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.mixit.cesar.model.FlatView;
import org.mixit.cesar.model.member.Member;
import org.mixit.cesar.model.session.SessionLanguage;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(FlatView.class)
    private Long id;

    @Size(max = 255)
    private String oauthId;

    @Size(max = 255)
    private String login;

    @Size(max = 255)
    private String lastname;

    @Size(max = 255)
    private String firstname;

    @Size(max = 255)
    private String password;

    @Size(max = 255)
    private String token;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @Size(max = 255)
    private String email;

    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private SessionLanguage defaultLanguage = SessionLanguage.fr;

    @ManyToOne(optional = false)
    public Member member;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Authority> authorities = new HashSet<>();

    private boolean valid;

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Account setToken(String token) {
        this.token = token;
        return this;
    }

    public String getOauthId() {
        return oauthId;
    }

    public Account setOauthId(String oauthId) {
        this.oauthId = oauthId;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Account setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public Account setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public Account setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public Account setMember(Member member) {
        this.member = member;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public Account setProvider(OAuthProvider provider) {
        this.provider = provider;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Account setEmail(String email) {
        this.email = email;
        return this;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public Account setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public SessionLanguage getDefaultLanguage() {
        return defaultLanguage;
    }

    public Account setDefaultLanguage(SessionLanguage defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public Account setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Account addAuthority(Authority authority) {
        this.authorities.add(authority);
        return this;
    }

    public boolean isValid() {
        return valid;
    }

    public Account setValid(boolean valid) {
        this.valid = valid;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(login, account.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
