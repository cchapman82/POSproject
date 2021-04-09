package edu.depaul.cdm.se.POSproject;

import java.io.Serializable;
import java.util.ArrayList;
import com.mongodb.*;
import java.util.List;

public class Employee implements Serializable {
    
    //private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private Long id;
    private Double pay;
    private String payType;
    private Integer secLevel;
    private Boolean current;
    private String password;
    
    public Employee() {
        this.firstName = "";
        this.lastName = "";
        this.id = null;
        this.pay = 0.00;
        this.payType = "";
        this.secLevel = 2;
        this.current = false;
        this.password = "";
    }
    
    public Employee(String firstName, String lastName, Long id, Object pay, String payType, Integer secLevel, Boolean current, String password) {
        this.firstName = firstName; this.lastName = lastName; this.id = id; this.pay = Double.valueOf(pay.toString()); this.payType = payType; this.secLevel = secLevel; this.current = current; this.password = password;
    }
    
     public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    } 
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    } 
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    } 
    
    public Double getPay() {
        return pay;
    }
    
    public void setPay(Double pay) {
        this.pay = pay;
    } 
    
    public String getPayType() {
        return payType;
    }
    
    public void setPayType(String payType) {
        this.payType = payType;
    } 
    
    public Integer getSecLevel() {
        return secLevel;
    }
    
    public void setSecLevel(Integer secLevel) {
        this.secLevel = secLevel;
    } 
    
    public Boolean getCurrent() {
        return current;
    }
    
    public void setCurrent(Boolean current) {
        this.current = current;
    } 
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String eToString() {
        StringBuilder retString = new StringBuilder();
        retString.append("First Name: ");
        retString.append(firstName);
        retString.append("Last Name: ");
        retString.append(lastName);
        retString.append("Id: ");
        retString.append(id);
        retString.append("Pay: ");
        retString.append(pay);
        retString.append("Pay Type: ");
        retString.append(payType);
        retString.append("Security Level: ");
        retString.append(secLevel);
        retString.append("Curent: ");
        retString.append(current);
        retString.append("Password: ");
        retString.append(password);
        
        return retString.toString();
    }

    //datbase methods
	public void createEmployee(Employee employee) {
        employee.setId(AppController.getInstanceDM().getEmployeeRepository().count());
        AppController.getInstanceDM().saveItem(AppController.getInstanceDM().getEmployeeRepository(), employee);
	}
}
