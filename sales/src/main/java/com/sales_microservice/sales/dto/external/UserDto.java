package com.sales_microservice.sales.dto.external;

import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
