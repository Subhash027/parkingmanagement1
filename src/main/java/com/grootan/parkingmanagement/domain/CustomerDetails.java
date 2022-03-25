package com.grootan.parkingmanagement.domain;
import com.grootan.parkingmanagement.enums.VehicleType;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer customerDetailsId;
    @NotNull
    private String vehicleNumber;
    @NotNull
    private VehicleType vehicleType;
    @Nullable
    private String mail;
    @NotNull
    private Long phoneNumber;
    @Nullable
    private Long whatsappNumber;





}