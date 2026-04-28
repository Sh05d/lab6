package org.example.employeemanagementsystem.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "ID can't be null")
    @Size(min = 3, message = "Length must be more than 2 characters")
    private String id;
    @NotEmpty(message = "Name can't be null")
    @Size(min = 5, message = "Length must be more than 4 letters")
    @Pattern(regexp = "^[^0-9]*$", message = "Name must not contain numbers")
    private String name;
    @Email
    private String email;
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number should Start with 05 and should be 10 digits") //this pattern make sure it start eith 05 and be 10 digits only
    private String phoneNumber;
    @NotNull(message = "Age can't be null")
    @Min(26)
    private int age;
    @NotEmpty(message = "Position can't be null")
    @Pattern(regexp = "(?i)Supervisor|Coordinator", message = "Position should be either supervisor or coordinator")
    private String position;
    @AssertFalse(message = " on leave should be false")
    private boolean onLeave;
    @NotNull(message = "Hire date can't be null")
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    @NotNull(message = "Annual leave can't be null")
    @PositiveOrZero
    private int annualLeave;
}
