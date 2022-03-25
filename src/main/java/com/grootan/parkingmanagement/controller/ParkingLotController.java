package com.grootan.parkingmanagement.controller;

import com.grootan.parkingmanagement.domain.ParkingLot;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParkingLotController {


	@PostMapping("/lot/{lot}")
	public ParkingLot Lot(@PathVariable(value = "lot")@RequestBody ParkingLot parkingLot)
	{
		return parkingLot;
	}


}
