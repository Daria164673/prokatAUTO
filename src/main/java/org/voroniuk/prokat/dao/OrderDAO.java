package org.voroniuk.prokat.dao;

import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Order Data Access Object interface
 *
 * @author D. Voroniuk
 */
public interface OrderDAO {
    boolean saveOrder(Order order);

    Order findOrderById(int id);

    List<Order> findOrders(int user_id, Order.State state, int start, int offset);

    int countOrders(int user_id, Order.State state);

    boolean updateRejectOrder(int order_id, int car_id, String reject_reason);

    boolean updateReturnOrder(int order_id, int car_id);

    boolean updateOrderState(int order_id, Order.State state);

    CarDAO getCarDAO();
}
