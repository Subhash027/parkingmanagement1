package com.grootan.parkingmanagement.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer parkingSlotReservationId;
    private Integer customerDetailsId;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date inTime;
    private String vehicleNumber;
    @CreatedDate
    @Temporal(TemporalType.DATE)
    @LastModifiedDate
    private Date bookingDate;
    private Integer parkingSlotId;




}
