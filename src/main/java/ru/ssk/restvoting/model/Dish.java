package ru.ssk.restvoting.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dishes")
@ApiModel(description = "All details about the Dish entity.")
public class Dish extends AbstractNamedEntity {
    public Dish() {
    }

    @Column(name = "weight")
    @NotNull
    @Range(min = 5, max = 3000)
    @ApiModelProperty(notes = "The dish weight in grams")
    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
