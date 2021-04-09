package edu.depaul.cdm.se.POSproject;

/*
* 		View Controller for application
* */

import javax.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class POSController {

	private Employee currEmployee;
	private String message = "";
	private String next = "";
    @Value("${spring.application.name}")
    private String appName;

    // Initial page for application login
    @GetMapping("/")
    public String getLogon(Model model) {
        model.addAttribute("employee", new Employee());
        return "logOn";
    }

    // Checks if password is associated with current employee and, if so, sets employee to a session attribute
	@PostMapping(value={"/logOn"})
	public String checkPsswd(Model model, HttpSession session, Employee employee) {
    	message = "!";
		employee = AppController.getEmployeeByPassword(employee.getPassword());
		if(!(employee.getId() == null)){
			currEmployee = employee;
			session.setAttribute("employee", employee);
			session.setAttribute("menuAL", AppController.getMenuAL());
			next = "tableScreen";
		}else {
			next = "errlogon";

		}
		model.addAttribute("message", message);
		return next;
	}

	//Select table and start transaction or continue transaction if owned by employee
	@PostMapping(value={"/tableScreen"})
	public String startTrans(Model model, Transaction transaction, HttpSession session) {
		next = "";
		message = "";
		// Check to see if Transaction exists in open transaction list
		if (!AppController.getTransAL().contains(transaction)) {
			transaction.setId(AppController.getInstanceDM().getCount(AppController.getInstanceDM().getTransactionRepository()));
			transaction.setEID(((Employee) session.getAttribute("employee")).getId());
			AppController.getInstanceDM().saveItem(AppController.getInstanceDM().getTransactionRepository(), transaction);
			next = "trans";
			message = " Please order items";

		// Check if employeee owns transaction
		} else if (AppController.getTransAL().get(AppController.getTransAL().indexOf(transaction)).getEID().equals(((Employee)session.getAttribute("employee")).getId())) {
			transaction = AppController.getTransAL().get(AppController.getTransAL().indexOf(transaction));
			next = "trans";
			message = " PLease order next items";
		} else {
			message = " Not your transaction";
			next = "tableScreen";
		}
		model.addAttribute("transaction", transaction);
		AppController.addTrans(transaction);
		model.addAttribute("message", message);
		return next;
	}

	// Order items and update transaction
	@PostMapping(value={"/trans"})
	public String updateTrans(Model model, Transaction transaction) {
    	message = " would you like to order more items?";
		Transaction transactionToUpdate = AppController.getOpenTransaction(transaction, currEmployee, "dto");
		transaction = AppController.updateOpenTransaction(transaction, transactionToUpdate);
		String order = transaction.orderedToString();
		String[] ss = new String[0];
		transaction.setOrdered(ss);
		model.addAttribute("transaction", transaction);
		model.addAttribute("message", message);
		model.addAttribute("order", order);
		return "trans";
	}

	// Navigate to payment screen to close transaction
	@PostMapping(value={"/trans"}, params={"close"})
	public String closeTrans(Model model, Transaction transaction) {
		transaction = AppController.getOpenTransaction(transaction, currEmployee, "dto");
		model.addAttribute("transaction", transaction);
		return "ctran";
	}

	//close screens
	@PostMapping(value = {"/ctran"})
	public String closedTrans(Model model, Transaction transaction, @RequestParam String action) {
		message = AppController.closeTransaction(transaction, action, currEmployee);
		model.addAttribute("message", message);
		return "closedScreen";
	}

	// exit screens
	@PostMapping(value={"/rEmp", "/rMenu", "/errAdmin", "/exit"})
	public String dReturn(Model model) {
    	message = " choose another table or logout.";
    	model.addAttribute("message", message);
		model.addAttribute("employee", new Employee());
		return "tableScreen";
	}

	// Check to see if current user is admin security to update items in application
	@PostMapping(value={"/tableScreen"}, params={"admin"})
	public String startAdmin(Employee employee, Model model) {
		if(currEmployee.getSecLevel().equals(1)) {
			return "admin";
		} else {
			return "errAdmin";
		}
	}

	// Terminate session and return to beginning of application
	@PostMapping(value={"/tableScreen"}, params={"logout"})
	public String logOut(HttpSession session, Model model) {
		session.removeAttribute("employee");
		model.addAttribute("employee", new Employee());
		return "logOn";
	}

	//  Screens to gather info about employee creation or updating
	@PostMapping(value={"/admin"}, params={"CreateEmployee"})
	public String cEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "cEmp";
	}

	//  Screen to enter employee information
	@PostMapping(value={"/cEmp"})
	public String employeeField(Employee employee, Model model) {
		employee = AppController.getEmployeeByName(employee);
		model.addAttribute("employee", employee);
		return"empInfo";
	}

	@PostMapping(value={"/admin"}, params={"CreateMenuItem"})
	public String cMenuItem(Model model) {
		model.addAttribute("employee", currEmployee);
		model.addAttribute("menuItem", new MenuItem());
		return "cMenu";
	}


	@PostMapping(value={"/cMenu"})
	public String menuField(MenuItem menuItem, Model model) {
		menuItem = AppController.getMenuItemByName(menuItem);
		model.addAttribute("menuItem", menuItem);
		return"mInfo";
	}

	@PostMapping(value={"/empInfo"})
	public String eReturn(Employee employee, Model model) {
		AppController.updateEmployee(employee);
		model.addAttribute("employee", employee);
		return "rEmp";
	}

	@PostMapping(value={"/mInfo"})
	public String rReturn(MenuItem menuItem, Model model) {
		AppController.updateMenuItem(menuItem);
		return "rMenu";
	}


}