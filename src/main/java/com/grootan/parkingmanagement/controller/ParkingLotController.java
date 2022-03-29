package com.grootan.parkingmanagement.controller;

import com.grootan.parkingmanagement.model.ParkingLot;
import com.grootan.parkingmanagement.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parkingLot")
public class ParkingLotController {

	@Autowired
	ParkingLotService parkingLotService;


	@GetMapping("/")
	public Iterable<ParkingLot> getAllParkingLots() {
		return parkingLotService.getParkingLots();
	}

	@GetMapping("/empty_lots")
	public List<ParkingLot> getEmptyParkingLots()
	{
		List<ParkingLot> result = new ArrayList<>();
		parkingLotService.getParkingLots().forEach(result::add);
		result=result.stream().filter(parking -> parking.isEmpty()).collect(Collectors.toList());
		return result;
	}

	@GetMapping("/filled_lots")
	public List<ParkingLot> getFilledParkingLots()
	{
		List<ParkingLot> result = new ArrayList<>();
		parkingLotService.getParkingLots().forEach(result::add);
		result=result.stream().filter(parking -> !parking.isEmpty()).collect(Collectors.toList());
		return result;
	}

	@PostMapping("/create")
	public ResponseEntity<ParkingLot> createParkingLot(@RequestBody @Valid ParkingLot parkingLot) {
		return new ResponseEntity<>(parkingLotService.createParkingLot(parkingLot), HttpStatus.CREATED);
	}


}
