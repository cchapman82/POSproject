package edu.depaul.cdm.se.POSproject;

/*
*       Start restaurant Point of Sale System as a Model View Controller web application.
*       Allows users to log in, start, order for and close transactions.  It also allows
*       users to add and update employees as long as there is the correct security levels.
* */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    //  MongoDB repositories to be accessed by the application
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    TransactionRepository transactionRepository;

          
    @Override 
    public void run(String... args) throws Exception {

		// initialize application controller to initiate the repositories and populate menu items as well as initial employees
		AppController a = AppController.getInstance(employeeRepository, menuRepository, transactionRepository, "src/main/resources/static/initEmp.json", "src/main/resources/static/initMenu.json");
    }

	public static void main(String[] args) {

        SpringApplication app = new SpringApplication(Main.class);
        app.run(args);
	}
}
