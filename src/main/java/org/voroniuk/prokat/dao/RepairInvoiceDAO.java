package org.voroniuk.prokat.dao;

import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.RepairInvoice;
import org.voroniuk.prokat.entity.User;

import java.util.List;

/**
 * Repair invoice Data Access Object interface
 *
 * @author D. Voroniuk
 */
public interface RepairInvoiceDAO {

    boolean saveRepairInvoice(RepairInvoice repairInvoice);

    List<RepairInvoice> findRepairInvoices(int start, int offset);

     int countRepairInvoices();

    RepairInvoice findRepairInvoiceById(int id);

    boolean updateReturnFromRepair(int repair_id, int car_id);

    CarDAO getCarDAO();

}
