package com.grootan.parkingmanagement.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ParkingSlot {

    @Id
    private Integer parkingSlotId;
    private Integer FloorId;
    private Integer slotNumber;
    private String wingCode;
}
