package org.sid.acoountdataserice.feign;

import org.sid.acoountdataserice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(url = "http://localhost:8083",value = "customer-rest-client")
public interface CustomerRestClient {
    @GetMapping("/customers")
    public List<Customer> findAllCustomers();
    @GetMapping("/customers/{id}")
    public Customer findCustomersById(@PathVariable Long id);
    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer);
}
