package org.sid.acoountdataserice.web;

import lombok.AllArgsConstructor;
import org.sid.acoountdataserice.feign.CustomerRestClient;
import org.sid.acoountdataserice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/account-service")
public class AccountRestController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CustomerRestClient customerRestClient;
    // Client Rest Template
    @GetMapping("/customers/v1")
    public List<Customer> listCustomerv1(){
        Customer[] customers = restTemplate.getForObject("http://localhost:8083/customers", Customer[].class);
        return List.of(customers);
    }
    @GetMapping("/customers/v1/{id}")
    public Customer findCustomersByIdv1(@PathVariable Long id){
        Customer customers = restTemplate.getForObject("http://localhost:8083/customers/"+id, Customer.class);
        return customers;
    }
    @PostMapping("/customers/v1")
    public Customer saveCustomerV1(@RequestBody Customer customer){
        Customer customer1 = restTemplate.postForObject("http://localhost:8083/customers", customer, Customer.class);
        return customer1;
    }
    // Client Web Flux
    @GetMapping("/customers/v2")
    public Flux<Customer> listCustomerV2(){
        WebClient webClient= WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
        Flux<Customer> customerFlux = webClient.get()
                .uri("/customers")
                .retrieve().bodyToFlux(Customer.class);
        return customerFlux;
    }
    @GetMapping("/customers/v2/{id}")
    public Mono<Customer> findCustomersByIdv2(@PathVariable Long id){
        WebClient webClient= WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
        Mono<Customer> customerMono = webClient.get()
                .uri("/customers/{id}",id)
                .retrieve().bodyToMono(Customer.class);
        return customerMono;
    }
    @PostMapping("/customers/v2")
    public Mono<Customer> saveCustomerV2(@RequestBody Customer customer){
        WebClient webClient= WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
        Mono<Customer> customerMono = webClient.post()
                .uri("/customers")
                .bodyValue(customer)
                .retrieve().bodyToMono(Customer.class);
        return customerMono;
    }
    // Client OpenFeign
    @GetMapping("/customers/v3")
    public List<Customer> listCustomerV3(){
        return customerRestClient.findAllCustomers();

    }
    @GetMapping("/customers/v3/{id}")
    public Customer findCustomersByIdv3(@PathVariable Long id){
        return customerRestClient.findCustomersById(id);

    }
    @PostMapping("/customers/v3")
    public Customer saveCustomerV3(@RequestBody Customer customer){
        return customerRestClient.saveCustomer(customer);

    }


}
