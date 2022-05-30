package ru.ssk.restvoting.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "restaurants")
@Access(AccessType.FIELD)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ApiModel(description = "All details about the Restaurant entity.")
public class Restaurant extends AbstractEmailEntity {
    @NotBlank
    @Size(min = 2, max = 255)
    @Column(name = "address")
    @ApiModelProperty(notes = "The restaurant address. Must be unique.")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, String email, String address) {
        super(id, name, email);
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name +
                ", email='" + email +
                ", address='" + address +
                '}';
    }
}
