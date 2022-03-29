package com.grootan.parkingmanagement.repository;


import com.grootan.parkingmanagement.model.CustomerDetails;
import com.grootan.parkingmanagement.model.ParkingSlotReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotReservationRepository extends JpaRepository<ParkingSlotReservation,Integer> {

	Optional<ParkingSlotReservation> getParkingSlotReservationById(Integer ID);

	@Query("select ParkingSlotReservation from ParkingSlotReservation ParkingSlotReservation where ParkingSlotReservation.vehicleNumber= ?1")
	List<ParkingSlotReservation> findByVehicleNumber(String vehicle);

	@Query("select ParkingSlotReservation from ParkingSlotReservation ParkingSlotReservation")
	List<ParkingSlotReservation> findAllCustomer();

}
