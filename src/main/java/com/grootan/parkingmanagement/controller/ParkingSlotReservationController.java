package com.grootan.parkingmanagement.controller;


import com.grootan.parkingmanagement.domain.ParkingSlotReservation;
import com.grootan.parkingmanagement.service.ParkingSlotReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParkingSlotReservationController {
	Logger logger= LoggerFactory.getLogger(ParkingSlotReservationController.class);


	@Autowired
	ParkingSlotReservationService parkingSlotReservationService;

	@PostMapping("/getReserve/{vehicleNumber}/{customerId}")
	public ResponseEntity<ParkingSlotReservation> getReserve(@PathVariable(value = "vehicleNumber")String vehicleNumber, @PathVariable(value = "customerId")Integer CustomerId )
	{
		logger.info("get vehicle number and Customer Id"+" "+vehicleNumber+" "+CustomerId);
		ParkingSlotReservation parkingSlotReservation1=parkingSlotReservationService.confrimReservation(vehicleNumber,CustomerId);
		return ResponseEntity.ok().body(parkingSlotReservation1);
	}
	@GetMapping("/getbooking")
	public ResponseEntity<List<ParkingSlotReservation>> getBookingDetails()
	{
		return parkingSlotReservationService.getAllBooking();
	}

}
