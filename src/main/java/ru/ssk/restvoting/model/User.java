package ru.ssk.restvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;
import ru.ssk.restvoting.util.validation.ValidationGroup;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User extends AbstractEmailEntity implements Serializable {
    @NotBlank
    @Size(min = 4, max = 15, groups = ValidationGroup.Password.class)
    @Size(min = 4, max = 100, groups = ValidationGroup.Persist.class)
    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")})
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 100)
    @Column(name = "role")
    @JoinColumn(name = "user_id") //https://stackoverflow.com/a/62848296/548473
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Role> roles;

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(String name, String email, String password) {
        super(null, name, email);
        this.password = password;
    }

    public User(Integer id, String name, String email, String password) {
        super(id, name, email);
        this.password = password;
    }

    public User(Integer id, String name, String email, String password, Collection<Role> roles) {
        super(id, name, email);
        this.password = password;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles.isEmpty() ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name +
                ", email='" + email +
                ", roles=" + roles +
                '}';
    }
}
