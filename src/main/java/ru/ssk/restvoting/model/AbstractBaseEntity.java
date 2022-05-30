package ru.ssk.restvoting.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.Hibernate;

import javax.persistence.*;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity implements HasId {
    public static final int START_SEQ = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated entity ID")
    protected Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    protected AbstractBaseEntity() {
    }

    protected AbstractBaseEntity(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !this.getClass().equals(Hibernate.getClass(other))) return false;
        return (id != null && id.equals(((AbstractBaseEntity) other).id));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": #" + id;
    }
}
