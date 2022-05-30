package ru.ssk.restvoting.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEmailEntity extends AbstractNamedEntity {
    @Email
    @NotBlank
    @Size(min = 7, max = 70)
    @Column(name = "email")
    protected String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AbstractEmailEntity() {
    }

    protected AbstractEmailEntity(Integer id, String name, String email) {
        super(id, name);
        this.email = email;
    }
}
