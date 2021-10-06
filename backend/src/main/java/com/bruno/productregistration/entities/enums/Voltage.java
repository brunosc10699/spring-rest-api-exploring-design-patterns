package com.bruno.productregistration.entities.enums;

import com.bruno.productregistration.services.exceptions.IncorrectValueException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Voltage {

    MONOVOLTAGE127(0, "127V"),
    MONOVOLTAGE230(1, "230V"),
    BIVOLTAGE(2, "127V/230V");

    private Integer code;
    private String description;

    public static Voltage toEnum(Integer code){
        if(code == null) throw new IncorrectValueException("The voltage code is null!");
        for(Voltage voltage : Voltage.values()){
            if(code == voltage.getCode()) return voltage;
        }
        throw new IllegalArgumentException("Invalid voltage code: " + code);
    }
}
