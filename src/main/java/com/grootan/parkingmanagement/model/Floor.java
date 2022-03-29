package com.grootan.parkingmanagement.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Floor
{
    @Id
    private Integer floorId;
    private Integer floorNumber;
    private Integer numberOfSlot;
    private String isFloorAvailable;
}
