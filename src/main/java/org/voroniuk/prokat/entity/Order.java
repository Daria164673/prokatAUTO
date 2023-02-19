package org.voroniuk.prokat.entity;

import org.voroniuk.prokat.entity.builders.OrderBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Order entity
 *
 * @author D. Voroniuk
 */

public class Order implements Serializable {
    private  int id;
    private Date date;
    private User user;
    private Car car;
    private String passportData;
    private boolean withDriver;
    private int term;
    private State state;
    private  double amount;
    private Date returnDate;
    private String reject_reason;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public boolean getWithDriver() {
        return withDriver;
    }

    public void setWithDriver(boolean withDriver) {
        this.withDriver = withDriver;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public enum State {
        NEW,
        PAID,
        REJECTED,
        FINISHED
    }
}
