package com.bruno.productregistration.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tb_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product {

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
