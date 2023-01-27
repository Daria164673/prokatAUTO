package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.BrandDAO;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Brand Data Access Object implementation
 * provided methods for
 * finding, creating Brands
 *
 * @author D. Voroniuk
 */
public class BrandDAOimp implements BrandDAO {
    private final Logger LOG = Logger.getLogger(BrandDAOimp.class);

    @Override
    public List<Brand> findAllBrands() {
        List<Brand> res = new LinkedList<>();
        String sql =    "SELECT brands.id, brands.name " +
                "FROM brands as brands ";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createBrand(resultSet));
                }
            } catch (Exception e) {
                LOG.warn(e);
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return res;
    }

    private Brand createBrand(ResultSet resultSet) throws SQLException {

        Brand brand = new Brand();
        brand.setId(resultSet.getInt("id"));

        brand.setName(resultSet.getString("name"));

        return brand;
    }

    @Override
    public Brand findBrandById(int id) {
        String sql =    "SELECT brands.id, brands.name FROM brands " +
                "WHERE id=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, id);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createBrand(resultSet);
                } else {
                    LOG.info("Can't find brand " + id);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return null;
    }
}
