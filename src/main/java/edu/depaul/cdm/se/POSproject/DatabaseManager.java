package edu.depaul.cdm.se.POSproject;

/*
*       Database Manager is a singleton that handles all the database operations.
*       Set to use generic types to handle all operations for all repositories.
* */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseManager {

    private static DatabaseManager instance;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // Dafault constructor
    public DatabaseManager() {
        instance = null;
        menuRepository = null;
        employeeRepository = null;
        transactionRepository = null;
    }

    //  Private constructor for singleton design pattern
    private DatabaseManager(String eFile, String mFile, EmployeeRepository e, MenuRepository m, TransactionRepository t) {
        setEmployeeRepository(e); setMenuRepository(m); setTransactionRepository(t);
        config(eFile, mFile);
    }

    //getters and setters for bean operations
    public static DatabaseManager getInstance(String eFile, String mFile, EmployeeRepository e, MenuRepository m, TransactionRepository t) {
        if (instance == null) {
            return new DatabaseManager(eFile, mFile, e, m, t);
        } else {
            return instance;
        }
    }
    public MenuRepository getMenuRepository() {
        return menuRepository;
    }

    public void setMenuRepository(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public EmployeeRepository getEmployeeRepository() {
        return employeeRepository;
    }

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    //  Initialize Menu and Employee repositories at first implimentation of application
    private void config(String eFile, String mFile) {
        JSONParser jp;
        JSONObject jo;
        JSONArray ja;
        FileReader in;
        Iterator<JSONObject> iterator;
        if (employeeRepository.count() == 0) {
            employeeRepository.deleteAll();
            menuRepository.deleteAll();
            transactionRepository.deleteAll();
            jp = new JSONParser();
            try {
                //read in employees
                in = new FileReader(eFile);
                jo = (JSONObject) jp.parse(in);
                ja = (JSONArray) jo.get("employees");
                iterator = ja.iterator();
                while (iterator.hasNext()) {
                    jo = iterator.next();
                    Employee em = new Employee((String) jo.get("firstName"), (String) jo.get("lastName"), employeeRepository.count(), jo.get("pay"), (String) jo.get("payType"), Integer.valueOf(jo.get("secLevel").toString()), (Boolean) jo.get("current"), (String) jo.get("password"));
                    employeeRepository.save(em);
                }
                in = new FileReader(mFile);
                jo = (JSONObject) jp.parse(in);
                ja = (JSONArray) jo.get("menuItems");
                iterator = ja.iterator();
                while (iterator.hasNext()) {
                    ArrayList ingredients = new ArrayList();
                    ArrayList allergies = new ArrayList();
                    jo = iterator.next();
                    JSONArray ja2 = (JSONArray) jo.get("ingredients");
                    for (int i = 0; i < ja2.size(); i++) {
                        ingredients.add(ja2.get(i).toString());
                    }
                    ja2 = (JSONArray) jo.get("allergies");
                    for (int i = 0; i < ja2.size(); i++) {
                        allergies.add(ja2.get(i).toString());
                    }
                    MenuItem mi = new MenuItem((String) jo.get("name"), menuRepository.count(), (String) jo.get("type"), (String) jo.get("subType"), Double.valueOf(jo.get("price").toString()), ingredients, allergies, (Boolean) jo.get("current"));
                    menuRepository.save(mi);
                }
                iterator.remove();
                in.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Could not find file!");
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    public long getCount(MongoRepository mr) {
        return mr.count();
    }

    public void saveItem(MongoRepository mr, Object o) {
        mr.save(o);
    }
}