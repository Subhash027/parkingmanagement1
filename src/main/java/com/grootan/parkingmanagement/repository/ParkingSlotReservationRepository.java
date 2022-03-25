package com.grootan.parkingmanagement.repository;


import com.grootan.parkingmanagement.domain.CustomerDetails;
import com.grootan.parkingmanagement.domain.ParkingSlotReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotReservationRepository extends JpaRepository<ParkingSlotReservation,Integer> {

	ParkingSlotReservation findByCustomerDetailsId(Integer customerDetailsId);

	@Query("select ParkingSlotReservation from ParkingSlotReservation ParkingSlotReservation where ParkingSlotReservation.vehicleNumber= ?1")
	CustomerDetails findByVehicleNumber(String actor);

	@Query("select ParkingSlotReservation from ParkingSlotReservation ParkingSlotReservation")
	List<CustomerDetails> findAllCustomer();

}
