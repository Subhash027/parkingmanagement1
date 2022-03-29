package com.grootan.parkingmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grootan.parkingmanagement.enums.VehicleType;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    private String city;

    private boolean isEmpty;

    private VehicleType vehicleType;

    private Double price;



}
