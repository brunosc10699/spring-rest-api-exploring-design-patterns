package com.bruno.productregistration.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product implements Serializable {

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @EqualsAndHashCode.Exclude
    @Column(nullable = false)
    private String name;

    @EqualsAndHashCode.Exclude
    private String description;

    @EqualsAndHashCode.Exclude
    private Double price;

    @EqualsAndHashCode.Exclude
    private Integer inventory;

}
