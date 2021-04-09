package edu.depaul.cdm.se.POSproject;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuItem implements Serializable {
    
    //private static final long serialVersionUID = 1L;
    private String name;
	private Long id;
    private String type;
    private String subType;
    private Double price;
    private ArrayList<String> ingredients;
    private ArrayList<String> allergies;
    private Boolean currentm;
    
    public MenuItem() {
        this.name = "";
		this.id = null;
        this.type = "";
        this.subType = "";
        this.price = 0.00;
        this.ingredients = new ArrayList<String>();
        this.allergies = new ArrayList<String>();
        this.currentm = false;
    }
    
    public MenuItem(String name, Long id, String type, String subType, Double price, ArrayList<String> ingredients, ArrayList<String> allergies, Boolean currentm) {
        setName(name); setId(id); setType(type); setSubType(subType); setPrice(price);
        setIngredients(ingredients);
        setAllergies(allergies);
         setCurrent(currentm);
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
	
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }   
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }    
    
    public String getSubType() {
        return subType;
    }
    
    public void setSubType(String subType) {
        this.subType = subType;
    }    
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }   
    
    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }    
    
    public ArrayList<String> getAllergies() {
        return allergies;
    }
    
    public void setAllergies(ArrayList<String> allergies) {
        this.allergies = allergies;
    }
    
    public Boolean getCurrent() {
        return currentm;
    }
    
    public void setCurrent(Boolean currentm) {
        this.currentm = currentm;
    }
    
    public String mIToString() {
        StringBuilder retString = new StringBuilder();
        retString.append("Menu Item: ");
        retString.append(name);
		retString.append("Item id: ");
		retString.append(id);
        retString.append("Type: ");
        retString.append(type);
        retString.append("SubType: ");
        retString.append(subType);
        retString.append("Price: ");
        retString.append(price);
        retString.append("Ingredients: ");
        retString.append(ingredients.toString());
        retString.append("Allergies: ");
        retString.append(allergies.toString());
        retString.append("Cuurent: ");
        retString.append(currentm);
        
        return retString.toString();
    }
	public void createMenuItem(MenuItem menuItem) {
        menuItem.setId(AppController.getInstanceDM().getMenuRepository().count());
        menuItem.setCurrent(true);
        AppController.getInstanceDM().saveItem(AppController.getInstanceDM().getMenuRepository(), menuItem);
	}
}
