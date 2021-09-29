package com.bruno.productregistration.entities.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Voltage {

    MONOVOLTAGE127(0, "127V"),
    MONOVOLTAGE230(1, "230v"),
    BIVOLTAGE(2, "127v/230v");

    private Integer code;
    private String description;

    public static Voltage toEnum(Integer code){
        if(code == null) return null;
        for(Voltage voltage : Voltage.values()){
            if(code == voltage.getCode()) return voltage;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
