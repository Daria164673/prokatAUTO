package org.voroniuk.prokat.entity;

import org.voroniuk.prokat.entity.builders.CarBuilder;
import org.voroniuk.prokat.entity.builders.UserBuilder;
import org.voroniuk.prokat.utils.Utils;

import java.io.*;
import java.util.Base64;

/**
 * Car entity
 *
 * @author D. Voroniuk
 */
public class Car implements Serializable {
    private int id;

    private String model;
    private Brand brand;
    private String car_number;

    private QualityClass qualityClass;

    private double price;

    private String imgPath;
    private String base64Image;

    private State curr_state;


    public Car() {
     }

    public Car(String model, Brand brand) {
        this.model = model;
        this.brand = brand;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public QualityClass getQualityClass() {
        return qualityClass;
    }

    public void setQualityClass(QualityClass qualityClass) {
        this.qualityClass = qualityClass;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public State getCurr_state() {
        return curr_state;
    }

    public void setCurr_state(State curr_state) {
        this.curr_state = curr_state;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
        try (FileInputStream fis = new FileInputStream(Utils.getImgPath() + File.separator + imgPath)) {

            byte[] all = fis.readAllBytes();

            Base64.Encoder encoder = Base64.getEncoder();
            String encoded = encoder.encodeToString(all);

            setBase64Image(encoded);

        } catch (Exception e) {

        }
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public static CarBuilder builder() {
        return new CarBuilder();
    }

    public enum State implements Serializable{
        FREE,
        ORDERED,
        ON_REPAIR,
    }

}
