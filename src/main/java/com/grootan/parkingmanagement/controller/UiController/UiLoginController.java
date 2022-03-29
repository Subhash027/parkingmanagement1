package com.grootan.parkingmanagement.controller.UiController;

import com.grootan.parkingmanagement.model.ParkingLot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiLoginController {

	@GetMapping("/login")
	public String displayParkingLot(Model model)
	{
		ParkingLot parkingLot=new ParkingLot();
		model.addAttribute("parkingLot",parkingLot);
		return "login_page";
	}
}
