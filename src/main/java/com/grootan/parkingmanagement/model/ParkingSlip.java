package com.grootan.parkingmanagement.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ParkingSlip {
    @Id
    private Integer ParkingSlip;
    private Integer parkingSlotReservationId;
    private String vehicleNumber;
    private Date inTime;
    private Date actualOutTime;
    private Integer basicCost;
    private Integer penalty;
    private Integer totalCost;

}
