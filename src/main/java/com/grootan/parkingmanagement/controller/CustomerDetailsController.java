package com.grootan.parkingmanagement.controller;

import com.grootan.parkingmanagement.model.CustomerDetails;
import com.grootan.parkingmanagement.service.CustomerDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class CustomerDetailsController {

    Logger logger= LoggerFactory.getLogger(CustomerDetailsController.class);


    @Autowired
    CustomerDetailsService customerDetailsService;

    /***
     *
     * @param customerDetails
     * @return
     */
   @PostMapping("parking/userdetails")
   public CustomerDetails saveUserDetails(@RequestBody CustomerDetails customerDetails )
   {
       logger.info("collect data from customer store into DB");
       customerDetailsService.saveUserDetails(customerDetails);
       logger.info("user saved in databse");
       return customerDetails;
   }

    /***
     *
     * @param vehicleNumber
     * @return
     */
    @DeleteMapping("/parking/vehicle/{vehicleNumber}")
    public CustomerDetails deleteByVehicleNumber(@PathVariable(value = "vehicleNumber")String vehicleNumber)
    {
        logger.warn("delete customer");
        return  customerDetailsService.deleteByvehicleNumber(vehicleNumber);
    }

    /***
     *
     * @return
     */
    @GetMapping("/userdetails")
    public List<CustomerDetails> findAll()
    {
        logger.info("fetching data from DB");
        return customerDetailsService.findAll();
    }

    @GetMapping("/getbyvehicleNumber/{vehicleNumber}")
    public ResponseEntity<?> findByVehicleNumber(@PathVariable(value = "vehicleNumber")String vehicleNumber)
    {
        logger.info("get vehicle number from customer");
        Optional<CustomerDetails> customerDetails=customerDetailsService.findByVehicleNumber(vehicleNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDetails);
    }




}
