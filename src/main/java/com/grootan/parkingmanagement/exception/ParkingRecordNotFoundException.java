package com.grootan.parkingmanagement.exception;

import org.webjars.NotFoundException;

public class ParkingRecordNotFoundException extends NotFoundException {
	public ParkingRecordNotFoundException(String message) {
		super(message);
	}
}
