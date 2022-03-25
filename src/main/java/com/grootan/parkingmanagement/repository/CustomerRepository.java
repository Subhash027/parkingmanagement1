package com.grootan.parkingmanagement.repository;
import com.grootan.parkingmanagement.domain.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * customer details repository
 */
@Repository
public interface CustomerRepository extends JpaRepository<CustomerDetails,Integer>
{

    @Query("select customer from CustomerDetails customer where customer.vehicleNumber= ?1")
    CustomerDetails findByVehicleNumber(String actor);

    @Query("select cd from CustomerDetails cd")
    List<CustomerDetails> findAllCustomer();


}
