package com.sales_microservice.sales.infraestructure.clients.users;


import com.sales_microservice.sales.dto.external.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-microservice")
public interface UserServiceClient {
    @GetMapping("/users/email/{email}")
    UserDto getUserByEmail(@PathVariable String email);
}
