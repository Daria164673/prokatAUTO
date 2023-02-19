package org.voroniuk.prokat.entity.builders;

import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.User;

import java.util.Date;

public class OrderBuilder {
    private  int id;
    private Date date;
    private User user;
    private Car car;
    private String passportData;
    private boolean withDriver;
    private int term;
    private Order.State state;
    private  double amount;
    private Date returnDate;
    private String reject_reason;

    public OrderBuilder id(int id) {
        this.id = id;
        return this;
    }

    public OrderBuilder date(Date date) {
        this.date = date;
        return this;
    }

    public OrderBuilder user(User user) {
        this.user = user;
        return this;
    }

    public OrderBuilder car(Car car) {
        this.car = car;
        return this;
    }

    public OrderBuilder passportData(String passportData) {
        this.passportData = passportData;
        return this;
    }

    public OrderBuilder withDriver(boolean withDriver) {
        this.withDriver = withDriver;
        return this;
    }

    public OrderBuilder term(int term) {
        this.term = term;
        return this;
    }

    public OrderBuilder state(Order.State state) {
        this.state = state;
        return this;
    }

    public OrderBuilder amount(double amount) {
        this.amount = amount;
        return this;
    }

    public OrderBuilder returnDate(Date returnDate) {
        this.returnDate = returnDate;
        return this;
    }

    public OrderBuilder reject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
        return this;
    }

    public Order build() {
        Order order = new Order();
        order.setId(id);
        order.setDate(date);
        order.setUser(user);
        order.setCar(car);
        order.setPassportData(passportData);
        order.setWithDriver(withDriver);
        order.setTerm(term);
        order.setState(state);
        order.setAmount(amount);
        order.setReturnDate(returnDate);
        order.setReject_reason(reject_reason);
        return order;
    }
}
