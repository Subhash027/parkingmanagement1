package com.grootan.parkingmanagement.model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Transactional
@Entity
public class ParkingSlotReservation  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String parkingLot;

    private LocalDateTime inTime;

    private String vehicleNumber;

    private LocalDate bookingDate;

    private LocalDateTime outTime;

    private Double price;

    public ParkingSlotReservation(String parkingLot, LocalDateTime inTime, String vehicleNumber, LocalDate bookingDate, LocalDateTime outTime,Double price) {
        this.parkingLot = parkingLot;
        this.inTime = inTime;
        this.vehicleNumber = vehicleNumber;
        this.bookingDate = bookingDate;
        this.outTime = outTime;
        this.price=price;
    }
}
