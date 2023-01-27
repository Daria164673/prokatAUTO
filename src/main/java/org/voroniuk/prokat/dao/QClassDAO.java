package org.voroniuk.prokat.dao;

import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.QualityClass;

import java.util.List;

/**
 * Quality class Data Access Object interface
 *
 * @author D. Voroniuk
 */
public interface QClassDAO {

    public List<QualityClass> findAllQClasses();

    public QualityClass findQClassById(int id);

}
