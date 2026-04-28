package org.example.employeemanagementsystem.Controller;

import jakarta.validation.Valid;
import org.example.employeemanagementsystem.Model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        if(employees.isEmpty()){
            return ResponseEntity.status(400).body("There is no employee to show");
        }
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee newEmployee, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for(Employee employee: employees){
            if(employee.getId().equals(newEmployee.getId())){
                return ResponseEntity.status(400).body("ID already exist");
            }
        }
        employees.add(newEmployee);
        return ResponseEntity.status(200).body("Employee added successfully");

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id,@RequestBody @Valid Employee updatedEmployee, Errors errors){
        if(errors.hasErrors()){
            String message =errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for(int i=0; i< employees.size(); i++){
            if(employees.get(i).getId().equals(id)){
                updatedEmployee.setId(id);
                employees.set(i,updatedEmployee);
                return ResponseEntity.status(200).body("Employee updated successfully");
            }
        }
        return ResponseEntity.status(400).body("Employee not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id){
        for(int i=0; i<employees.size();i++){
            if(employees.get(i).getId().equals(id)){
                employees.remove(i);
                return ResponseEntity.status(200).body("Employee deleted successfully");
            }
        }
        return ResponseEntity.status(400).body("Employee not found");
    }

    @GetMapping("/search-position/{position}")
    public ResponseEntity<?> searchByPosition(@PathVariable String position){
        if(!position.equalsIgnoreCase("supervisor") && !position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(400).body("Position should be either supervisor or coordinator");
        }
        ArrayList<Employee> employeesByPosition =new ArrayList<>();
        for(Employee employee: employees){
            if(employee.getPosition().equals(position)){
                employeesByPosition.add(employee);
            }
        }
        if(employeesByPosition.isEmpty()){
            return ResponseEntity.status(400).body("There is no employee in this position");
        }
        return ResponseEntity.status(200).body(employeesByPosition);
    }

    @GetMapping("/range-age/{minAge}/{maxAge}")
    public ResponseEntity<?> getByRangeAge(@PathVariable int minAge,@PathVariable int maxAge){
        if(maxAge < minAge){
            return ResponseEntity.status(400).body("Max age should be larger than min age");
        }
        if(minAge < 26 || minAge > 69 ){
            return ResponseEntity.status(400).body("You should enter valid age for an employee for min age");
        }
        if(maxAge < 26 || maxAge > 69 ){
            return ResponseEntity.status(400).body("You should enter valid age for an employee for max age");
        }
        ArrayList<Employee> employeeInRange = new ArrayList<>();
        for(Employee employee: employees){
            if(employee.getAge() >= minAge && employee.getAge() <= maxAge){
                employeeInRange.add(employee);
            }
        }
        if(employeeInRange.isEmpty()){
            return ResponseEntity.status(400).body("There is no employee in this range of age");
        }
        return ResponseEntity.status(200).body(employeeInRange);
    }

    @PutMapping("/annual-leave/{id}")
    public ResponseEntity<?> applyAnnualLeave(@PathVariable String id){
        for(Employee employee: employees){
            if(employee.getId().equals(id)){
                if(employee.isOnLeave()){
                    return ResponseEntity.status(400).body("Employee already on leave");
                }
                if(employee.getAnnualLeave() == 0){
                    return ResponseEntity.status(400).body("Employee has no remain of annual leave");
                }
                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave()-1);
                return ResponseEntity.status(200).body("Employee got annual leave");
            }
        }
        return ResponseEntity.status(400).body("Employee not exist");
    }

    @GetMapping("/no-annual-leave")
    public ResponseEntity<?> employeeWithNoAnnualLeave(){
        if(employees.isEmpty()){
            return ResponseEntity.status(400).body("There is no employees in the system yet");
        }
        ArrayList<Employee> employeesWithNoAnnual = new ArrayList<>();
        for(Employee employee: employees){
            if(employee.getAnnualLeave() == 0){
                employeesWithNoAnnual.add(employee);
            }
        }
        if(employeesWithNoAnnual.isEmpty()){
            return ResponseEntity.status(400).body("All employees still have annual leave");
        }
        return ResponseEntity.status(200).body(employeesWithNoAnnual);
    }

    @PutMapping("/promote/{requesterID}/{employeeID}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String requesterID,@PathVariable String employeeID){
        if(employees.isEmpty()){
            return ResponseEntity.status(400).body("There is no employee in the system");
        }
        if(employees.size() ==1){
            return ResponseEntity.status(400).body("There is only one employee in the system");
        }
        boolean isRequesterExist = false;
        for(Employee employee :employees){
            if(employee.getId().equals(requesterID)){
                isRequesterExist = true;
                if(!employee.getPosition().equalsIgnoreCase("Supervisor")){
                    return ResponseEntity.status(400).body("Requester not a Supervisor");
                }
            }
        }
        if(!isRequesterExist){
            return ResponseEntity.status(400).body("Requester id not exist");
        }
        for(Employee employee :employees){
            if(employee.getId().equals(employeeID)){
              if(employee.getAge() < 30){
                  return ResponseEntity.status(400).body("Employee should be at least 30 to promote");
              }
              if(employee.isOnLeave()){
                  return ResponseEntity.status(400).body("Employee should be not on leave to promote");
              }
              if(employee.getPosition().equalsIgnoreCase("Supervisor")){
                  return ResponseEntity.status(400).body("Employee already Supervisor");
              }
              employee.setPosition("Supervisor");
              return ResponseEntity.status(200).body("Employee promoted successfully");
            }
        }
        return ResponseEntity.status(400).body("Employee not found to promote");
    }
}
