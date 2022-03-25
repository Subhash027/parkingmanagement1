package com.grootan.parkingmanagement.domain;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ParkingLot {
    @Id
    @GeneratedValue
    private Integer parkingLotId;

    private String blockId;

    private Integer numberOfFloor;


}
