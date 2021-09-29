package com.bruno.productregistration.entities.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Classification {

    A(0, "Eficiency class: A"),
    B(1, "Eficiency class: B"),
    C(2, "Eficiency class: C"),
    D(3, "Eficiency class: D"),
    E(4, "Eficiency class: E"),
    F(5, "Eficiency class: F"),
    G(6, "Eficiency class: G");
    
    private Integer code;
    private String description;
    
    public static Classification toEnum(Integer code){
        if(code == null) return null;
        for(Classification classification : Classification.values()){
            if(code == classification.getCode()) return classification;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
