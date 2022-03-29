package com.grootan.parkingmanagement.controller;


import com.grootan.parkingmanagement.exception.VehicleNotFoundException;
import com.grootan.parkingmanagement.model.CustomerDetails;
import com.grootan.parkingmanagement.model.ParkingSlotReservation;
import com.grootan.parkingmanagement.service.ParkingSlotReservationService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ParkingSlotReservationController {
	Logger logger= LoggerFactory.getLogger(ParkingSlotReservationController.class);


	@Autowired
	ParkingSlotReservationService parkingSlotReservationService;

	@GetMapping("/getBookingRecords")
	public Iterable<ParkingSlotReservation> getAllParkingRecords()
	{
		return parkingSlotReservationService.getParkingList();
	}
	@GetMapping("/currentParking")
	public List<ParkingSlotReservation> getCurrentParkingRecords()
	{
		List<ParkingSlotReservation> result = new ArrayList<>();
		parkingSlotReservationService.getParkingList().forEach(result::add);
		result=result.stream().filter(booking -> booking.getOutTime()==null).collect(Collectors.toList());
		return result;
	}

	@GetMapping("/get/{licence_plate}")
	public List<ParkingSlotReservation> getCurrentParkingWithLicencePlate(@PathVariable(value = "licence_plate") String licencePlate)
	{
		List<ParkingSlotReservation> result = new ArrayList<>();
		parkingSlotReservationService.getParkingList().forEach(result::add);
		result=result.stream().filter(parking -> parking.getVehicleNumber().equals(licencePlate)).collect(Collectors.toList());

		if(result.size()==0)
			throw new VehicleNotFoundException("there is no vehicle ");
		else
			return result;
	}

	@PostMapping("/booking")
	public ResponseEntity<ParkingSlotReservation> checkInToParkingLot(@RequestBody @Valid CustomerDetails customer)
	{
		return new ResponseEntity<>(parkingSlotReservationService.createParking(customer), HttpStatus.CREATED);
	}

	@PutMapping("/out/{id}")
	public ResponseEntity<ParkingSlotReservation> checkOutFromParkingLot(@PathVariable Integer id)
	{
		return new ResponseEntity<>(parkingSlotReservationService.updateParkingRecord(id), HttpStatus.OK);
	}

}
