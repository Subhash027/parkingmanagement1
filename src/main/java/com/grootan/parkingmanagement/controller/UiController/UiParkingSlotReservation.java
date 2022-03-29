package com.grootan.parkingmanagement.controller.UiController;

import com.grootan.parkingmanagement.controller.CustomerDetailsController;
import com.grootan.parkingmanagement.model.CustomerDetails;
import com.grootan.parkingmanagement.model.ParkingLot;
import com.grootan.parkingmanagement.service.ParkingLotService;
import com.grootan.parkingmanagement.service.ParkingSlotReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UiParkingSlotReservation {

	@Autowired
	ParkingSlotReservationService parkingSlotReservationService;

	@Autowired
	ParkingLotService parkingLotService;

	Logger logger= LoggerFactory.getLogger(UiParkingSlotReservation.class);

	@GetMapping("/getParkingAvailable")
	public String getEmptyParkingLots(Model model)
	{
		List<ParkingLot> result = new ArrayList<>();
		parkingLotService.getParkingLots().forEach(result::add);
		result=result.stream().filter(parking -> parking.isEmpty()).collect(Collectors.toList());
		model.addAttribute("result",result);
		return "get_slot";
	}
//	@PostMapping("/customer/details")
//	public String lotCreated(@ModelAttribute("customerDetails") CustomerDetails customerDetails)
//	{
//		logger.info("collect data from customer store into DB");
//		parkingSlotReservationService.saveUserDetails(customerDetails);
//		logger.info("user saved in databse");
//		return "get_Parking";
//	}
}
