package edu.depaul.cdm.se.POSproject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Transaction implements Serializable {
    
    private Long id;
    private Long eID;
    private Integer table;
    private String[] ordered;
    private Double netTotal;
    private Double grossTotal;
    private String paymentType;
    
    public Transaction() {
        this.id = null;
        this.eID = null;
        this.table = null;
        this.ordered = new String[0];
        this.netTotal = null;
        this.grossTotal = null;
        this.paymentType = "";
    }
    
    public Transaction(Long id, Long eID, Integer table, String[] ordered, Double netTotal, Double grossTotal, String pmType) {
        this.id = id; this.eID = eID;this.table = table; this.ordered = ordered; this.netTotal = netTotal; this.grossTotal = grossTotal; this.paymentType = pmType;
    }
    
    public void setId(Long id){
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setEID(Long eID){
        this.eID = eID;
    }
    public Long getEID() {
        return eID;
    }
    public void setTable(Integer table){
        this.table = table;
    }
    public Integer getTable() {
        return table;
    }
    public void setOrdered(String[] ordered){
        this.ordered = ordered;
    }
    public String[] getOrdered() {
        return ordered;
    }
    public void setNetTotal(MenuRepository mRepository){
        Double total = 0.00;
		List<MenuItem> currItems = mRepository.findAll();
		int i = 0;
		do {
			for(MenuItem m : currItems) {
				if(ordered[i].equals(m.getName())) {
					total += m.getPrice();
				}
			}
			i++;
		}while(i<ordered.length);
		this.netTotal = total;
    }
    public Double getNetTotal() {
        return netTotal;
    }
    public void setGrossTotal(){
        this.grossTotal = this.netTotal*.0875 + this.netTotal;
    }
    public Double getGrossTotal() {
        return grossTotal;
    }
    public void setPaymentType(String paymentType){
        this.paymentType = paymentType;
    }
    public String getPaymentType() {
        return paymentType;
    }
    
    public String tToString() {
        StringBuilder retString = new StringBuilder();
        retString.append("Transaction: ");
        retString.append(id);
        retString.append(" Employee: ");
        retString.append(eID);
        retString.append(" Table: ");
        retString.append(table);
        retString.append(" Ordered: ");
        retString.append(orderedToString());
        retString.append(" Net Total: ");
        retString.append(netTotal);
        retString.append(" Gross Total: ");
        retString.append(grossTotal);
        retString.append(" Payment Type: ");
        retString.append(paymentType);
        return retString.toString();
    }

    public String orderedToString() {
        String toReturn = "";
        if (!(ordered == null)) {
            for (int i = 0; i < ordered.length; i++) {
                toReturn += " " +ordered[i];
            }
        }
        return toReturn;
    }

	public void createTransaction(Transaction transaction) {
        transaction.setId(AppController.getInstanceDM().getTransactionRepository().count());
        AppController.getInstanceDM().saveItem(AppController.getInstanceDM().getTransactionRepository(), transaction);
	}

	// Update transaction with newly ordered items
    public Transaction updateTransactionOrder(Transaction t, Transaction t1) {
        String[] aOrdered = t1.getOrdered();
        String[] nOrdered = t.getOrdered();
        t.setOrdered(Stream.concat(Arrays.stream(aOrdered), Arrays.stream(nOrdered)).toArray(String[]::new));
        t.setNetTotal(AppController.getInstanceDM().getMenuRepository());
        t.setGrossTotal();
        return t;
    }
}
