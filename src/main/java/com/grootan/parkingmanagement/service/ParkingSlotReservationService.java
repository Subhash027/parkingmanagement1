package com.grootan.parkingmanagement.service;

import com.grootan.parkingmanagement.domain.ParkingSlotReservation;
import com.grootan.parkingmanagement.repository.ParkingSlotReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSlotReservationService {
	@Autowired
	ParkingSlotReservationRepository parkingSlotReservationRepository;

	public ParkingSlotReservation confrimReservation(String vehicleNumber, Integer ID)
	{
//
//		ParkingSlotReservation parkingSlotReservation=new ParkingSlotReservation();
//		parkingSlotReservation.setVehicleNumber(vehicleNumber);
//		parkingSlotReservation.setCustomerDetailsId(ID);
//
		return  null;
	}
	public ResponseEntity<List<ParkingSlotReservation>> getAllBooking(){

		return null;
	}


}
