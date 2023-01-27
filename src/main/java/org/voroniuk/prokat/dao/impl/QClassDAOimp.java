package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.BrandDAO;
import org.voroniuk.prokat.dao.QClassDAO;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.QualityClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Quality class Data Access Object implementation
 * provided methods for
 * finding, creating Quality classes
 *
 * @author D. Voroniuk
 */

public class QClassDAOimp implements QClassDAO {
    private final Logger LOG = Logger.getLogger(QClassDAOimp.class);

    @Override
    public List<QualityClass> findAllQClasses() {
        List<QualityClass> res = new LinkedList<>();
        String sql =    "SELECT q_classes.id, q_classes.name " +
                "FROM q_classes as q_classes ";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createQClass(resultSet));
                }
            } catch (Exception e) {
                LOG.warn(e);
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return res;
    }

    private QualityClass createQClass(ResultSet resultSet) throws SQLException {

        QualityClass qualityClass = new QualityClass();
        qualityClass.setId(resultSet.getInt("id"));

        qualityClass.setName(resultSet.getString("name"));

        return qualityClass;
    }

    @Override
    public QualityClass findQClassById(int id) {
        String sql =    "SELECT q_classes.id, q_classes.name FROM q_classes " +
                "WHERE id=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, id);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createQClass(resultSet);
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

