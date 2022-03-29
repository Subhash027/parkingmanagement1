package com.grootan.parkingmanagement.controller.UiController;


import com.grootan.parkingmanagement.model.ParkingLot;
import com.grootan.parkingmanagement.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UiParkingController {

	@Autowired
	ParkingLotService parkingLotService;

	@GetMapping("/parkinglot")
	public String displayParkingLot(Model model)
	{
		ParkingLot parkingLot=new ParkingLot();
		model.addAttribute("parkingLot",parkingLot);
		return "parking_Lot";
	}

	@PostMapping("/parkinglot")
	public String lotCreated(@ModelAttribute("parkingLot") ParkingLot parkingLot)
	{
		parkingLotService.createParkingLot(parkingLot);
		return "Lot_created";
	}
}
