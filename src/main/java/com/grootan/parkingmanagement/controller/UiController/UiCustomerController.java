package com.grootan.parkingmanagement.controller.UiController;

import com.grootan.parkingmanagement.controller.CustomerDetailsController;
import com.grootan.parkingmanagement.model.CustomerDetails;
import com.grootan.parkingmanagement.model.ParkingLot;
import com.grootan.parkingmanagement.model.ParkingSlotReservation;
import com.grootan.parkingmanagement.repository.ParkingSlotReservationRepository;
import com.grootan.parkingmanagement.service.CustomerDetailsService;
import com.grootan.parkingmanagement.service.ParkingSlotReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UiCustomerController {

	@Autowired
	CustomerDetailsService customerDetailsService;

	@Autowired
	ParkingSlotReservationRepository parkingSlotReservationRepository;

	@Autowired
	ParkingSlotReservationService parkingSlotReservationService;

	Logger logger= LoggerFactory.getLogger(CustomerDetailsController.class);

	@GetMapping("/customer/details")
	public String displayParkingLot(Model model)
	{
		CustomerDetails customerDetails=new CustomerDetails();
		model.addAttribute("customerDetails",customerDetails);
		return "customer_details";
	}

	@PostMapping("/customer/details")
	public String lotCreated(@ModelAttribute("customerDetails") CustomerDetails customerDetails,Model model)
	{
		ParkingSlotReservation parkingSlotReservation=parkingSlotReservationService.createParking(customerDetails);
		model.addAttribute("parkingSlotReservation",parkingSlotReservation);
		return "getTicket";
	}

}
