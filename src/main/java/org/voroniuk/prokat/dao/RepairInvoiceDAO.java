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

    public boolean saveRepairInvoice(RepairInvoice repairInvoice);

    public List<RepairInvoice> findRepairInvoices(int start, int offset);

    public  int countRepairInvoices();

    public RepairInvoice findRepairInvoiceById(int id);

    public boolean updateReturnFromRepair(int repair_id, int car_id);


}
