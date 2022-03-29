package com.grootan.parkingmanagement.repository;


import com.grootan.parkingmanagement.enums.VehicleType;
import com.grootan.parkingmanagement.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot,Integer> {

	List<ParkingLot> findParkingLotByVehicleType(VehicleType vehicleType);

	Optional<ParkingLot> findPriceByName(String name);
}
