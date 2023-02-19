package org.voroniuk.prokat.entity.builders;

import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.RepairInvoice;

import java.util.Date;

public class RepairInvoiceBuilder {

    private int id;
    private Date date;
    private Car car;
    private  double amount;
    private String contractor;
    private String repairInfo;
    private Date returnDate;

    public RepairInvoiceBuilder id(int id) {
        this.id = id;
        return this;
    }

    public RepairInvoiceBuilder date(Date date) {
        this.date = date;
        return this;
    }

    public RepairInvoiceBuilder car(Car car) {
        this.car = car;
        return this;
    }

    public RepairInvoiceBuilder amount(double amount) {
        this.amount = amount;
        return this;
    }

    public RepairInvoiceBuilder contractor(String contractor) {
        this.contractor = contractor;
        return this;
    }

    public RepairInvoiceBuilder repairInfo(String repairInfo) {
        this.repairInfo = repairInfo;
        return this;
    }

    public RepairInvoiceBuilder returnDate(Date returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public RepairInvoice build() {
        RepairInvoice newDoc = new RepairInvoice();
        newDoc.setId(id);
        newDoc.setDate(date);
        newDoc.setCar(car);
        newDoc.setAmount(amount);
        newDoc.setContractor(contractor);
        newDoc.setRepairInfo(repairInfo);
        newDoc.setReturnDate(returnDate);
        return newDoc;
    }
}
