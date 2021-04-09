package edu.depaul.cdm.se.POSproject;

/*
*       AppController is a singleton that initializes the database manager as well as an ArrayList for
*       current employees, menu items and transactions for use in the application.
* */

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;
public class AppController {

    private static AppController instance /*= null*/;
    private static DatabaseManager instanceDM;
    private static ArrayList<Employee> empAL = new ArrayList<Employee>();
    private static ArrayList<MenuItem> menuAL = new ArrayList<MenuItem>();
    private static ArrayList<Transaction> transAL = new ArrayList<Transaction>();

    //  Default contructor for AppController
    private AppController() {
        instance = null;
        instanceDM = null;
        empAL = new ArrayList<Employee>();
        menuAL = new ArrayList<MenuItem>();
        transAL = new ArrayList<Transaction>();

    }

    //  Singleton constructor
    private AppController(EmployeeRepository e, MenuRepository m, TransactionRepository t, String eFile, String mFile) {
        setInstanceDM(eFile, mFile, e, m, t);
        setMenuAL(instanceDM.getMenuRepository());
        setEmpAL(instanceDM.getEmployeeRepository());
    }

    //  Singleton instance returned to ensure only one per application
    public static AppController getInstance(EmployeeRepository e, MenuRepository m, TransactionRepository t, String eFile, String mFile) {
        if (instance == null) {
            instance = new AppController(e, m, t, eFile, mFile);
        }
        return instance;
    }

    //      Getters and setters

    public static ArrayList<Employee> getEmpAL() {
        return empAL;
    }

    // Create list of current employees to check password for current session
    public static void setEmpAL(EmployeeRepository eRepository) {
        ArrayList<Employee> cEmployees = new ArrayList();
        List<Employee> list = eRepository.findAll();
        for(Employee e : list) {
            if(e.getCurrent() == true) {
                cEmployees.add(e);
            }

        }
        empAL = cEmployees;
    }

    public  static ArrayList<MenuItem> getMenuAL() {
        return menuAL;
    }

    public  static ArrayList<Transaction> getTransAL() {
        return transAL;
    }


    // Create list of current menu items for current session
    public static void setMenuAL(MenuRepository mRepository) {
        ArrayList<MenuItem> cMenuItems = new ArrayList();
        List<MenuItem> list = mRepository.findAll();
        for (MenuItem m : list) {
            if (m.getCurrent() == true)
                cMenuItems.add(m);
        }
        menuAL = cMenuItems;
    }

    // Add open transaction to array list
    public static void addTrans(Transaction t) {
        transAL.add(t);
    }

    // Get open transaction -------------------------------------------------------FIX RETURN
    public static Transaction getOpenTransaction(Transaction t, Employee e, String type) {
        for (Transaction trans : transAL) {
            // check if transaction t is in open transaction list
            if (trans.getTable() == t.getTable()) {
                if (transAL.get(transAL.indexOf(trans)).getEID().equals(e.getId())) {
                    t = trans;

                } else {
                    System.out.println("\n Wrong employee");
                }
            }
        }
        // check to see if dto should be returned or actual reference to be used inside class
        if (type == "obj") {
            return t;
        } else {
            t = new Transaction(t.getId(), t.getEID(), t.getTable(), t.getOrdered(), t.getNetTotal(), t.getGrossTotal(), t.getPaymentType());
        }
        return t;
    }

    // Update an open transaction and return dto
    public static Transaction updateOpenTransaction(Transaction t1, Transaction t2) {
        Transaction t = new Transaction();
        for (Transaction trans : transAL) {
            // make sure the same transaction is being updated
            if (t2.getId() == trans.getId()) {
                int index = transAL.indexOf(trans);
                // add new ordered items to existing order
                t = t.updateTransactionOrder(t1, t2);
                transAL.set(index, t);
            }
        }
        return new Transaction(t.getId(),t.getEID(), t.getTable(), t.getOrdered(), t.getNetTotal(), t.getGrossTotal(), t.getPaymentType());
    }

    //close an open transaction
    public static String closeTransaction(Transaction t, String action, Employee e) {
        t = getOpenTransaction(t,e, "obj");
        t.setPaymentType(action);
        instanceDM.saveItem(instanceDM.getTransactionRepository(), t);
        transAL.remove(t);
        //  return message to display on new page
        return "Transaction " + t.getId() + " has been closed with payment of " + t.getPaymentType() + " in the amount of " + t.getGrossTotal() + ".";
    }

    //  get an employee dto's
    public static Employee getEmployeeByPassword(String s) {
        Employee employee = new Employee();
        for(Employee e : empAL) {
            if (s.equals(e.getPassword())) {
                employee = new Employee(e.getFirstName(), e.getLastName(), e.getId(), e.getPay(), e.getPayType(), e.getSecLevel(), e.getCurrent(),
                   e.getPassword());
                break;
            }
        }
        return employee;
    }

    public static Employee getEmployeeByID(Long id) {
        Employee employee = new Employee();
        for(Employee e : empAL) {
            if (id.equals(e.getId())) {
                employee = new Employee(e.getFirstName(), e.getLastName(), e.getId(), e.getPay(), e.getPayType(), e.getSecLevel(), e.getCurrent(),
                        e.getPassword());;
                break;
            }
        }
        return employee;
    }

    public static Employee getEmployeeByName(Employee employee) {
        for(Employee e : empAL) {
            if (employee.getLastName().equals(e.getLastName())) {
                if (employee.getFirstName().equals(e.getFirstName())) {
                    employee = e;
                    break;
                }
            }
        }
        employee = new Employee(employee.getFirstName(), employee.getLastName(), employee.getId(), employee.getPay(), employee.getPayType(),
                employee.getSecLevel(), employee.getCurrent(), employee.getPassword());
        return employee;
    }

    // get employee object to modify info in arrayList
    private static Employee getEmployeeUpdate(Employee employee) {
        for(Employee e : empAL) {
            if (employee.getPassword().equals(e.getPassword())) {
                employee = e;
                break;
            }
        }
        return employee;
    }

    public static void updateEmployee(Employee employee) {
        Employee oldEmployee = getEmployeeUpdate(employee);
        int index = empAL.indexOf(oldEmployee);
        if(index == -1) {
            employee.createEmployee(employee);
            empAL.add(employee);
        } else {
            employee.setId(oldEmployee.getId());
            empAL.set(index, employee);
            getInstanceDM().saveItem(instanceDM.getEmployeeRepository(), employee);
        }
    }
    ///////////////////////////////////MenuItemUpdate and Create Items///////////////////////////////////////////
    public static MenuItem getMenuItemByName(MenuItem menuItem) {
        for(MenuItem m : menuAL) {
            if (menuItem.getName().equals(m.getName())) {
                menuItem = m;
                break;
            }
        }
        menuItem = new MenuItem(menuItem.getName(), menuItem.getId(), menuItem.getType(), menuItem.getSubType(), menuItem.getPrice(), menuItem.getIngredients(), menuItem.getAllergies(), menuItem.getCurrent());
        return menuItem;
    }

    // get employee object to modify info in arrayList
    private static MenuItem getMenuItemUpdate(MenuItem menuItem) {
        for(MenuItem m : menuAL) {
            if (menuItem.getName().equals(m.getName())) {
                menuItem = m;
                break;
            }
        }
        return menuItem;
    }

    public static void updateMenuItem(MenuItem menuItem) {
        MenuItem oldMenuItem = getMenuItemUpdate(menuItem);
        int index = menuAL.indexOf(oldMenuItem);
        if(index == -1) {
            menuItem.createMenuItem(menuItem);
            menuAL.add(menuItem);
        } else {
            menuItem.setId(oldMenuItem.getId());
            menuItem.setCurrent(true);
            menuAL.set(index, menuItem);
            getInstanceDM().saveItem(instanceDM.getMenuRepository(), menuItem);
        }
    }


    //  Initialize the database manager for application
    public static void setInstanceDM(String eFile, String mFile, EmployeeRepository e, MenuRepository m, TransactionRepository t) {
        instanceDM = DatabaseManager.getInstance(eFile, mFile, e, m, t);
    }

    public static DatabaseManager getInstanceDM() {
        return instanceDM;
    }
}
