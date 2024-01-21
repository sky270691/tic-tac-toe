package com.risky.tictactoeapp.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class PosEntity {

    private Integer x;
    private Integer y;

}
