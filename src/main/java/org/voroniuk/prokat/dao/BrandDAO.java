package org.voroniuk.prokat.dao;

import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.User;

import java.util.List;

/**
 * Brand Data Access Object interface
 *
 * @author D. Voroniuk
 */

public interface BrandDAO {

    public List<Brand> findAllBrands();

    public Brand findBrandById(int id);


}
