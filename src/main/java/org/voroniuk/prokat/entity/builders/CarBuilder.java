package org.voroniuk.prokat.entity.builders;

import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.QualityClass;
import org.voroniuk.prokat.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class CarBuilder {
    private int id;

    private String model;
    private Brand brand;
    private String car_number;

    private QualityClass qualityClass;

    private double price;

    private String imgPath;
    private String base64Image;

    private Car.State curr_state;

    public CarBuilder id(int id) {
        this.id = id;
        return this;
    }

    public CarBuilder model(String model) {
        this.model = model;
        return this;
    }

    public CarBuilder brand(Brand brand) {
        this.brand = brand;
        return this;
    }

    public CarBuilder car_number(String car_number) {
        this.car_number = car_number;
        return this;
    }

    public CarBuilder qualityClass(QualityClass qualityClass) {
        this.qualityClass = qualityClass;
        return this;
    }

    public CarBuilder price(double price) {
        this.price = price;
        return this;
    }

    public CarBuilder imgPath(String imgPath) {
        this.imgPath = imgPath;
        try (FileInputStream fis = new FileInputStream(Utils.getImgPath() + File.separator + imgPath)) {

            byte[] all = fis.readAllBytes();
            Base64.Encoder encoder = Base64.getEncoder();
            this.base64Image = encoder.encodeToString(all);

        } catch (Exception e) {
        }
        return this;
    }

    public CarBuilder base64Image(String base64Image) {
        this.base64Image = base64Image;
        return this;
    }

    public CarBuilder curr_state(Car.State curr_state) {
        this.curr_state = curr_state;
        return this;
    }

    public Car build() {
        Car car = new Car();
        car.setId(id);
        car.setModel(model);
        car.setBrand(brand);
        car.setCar_number(car_number);
        car.setQualityClass(qualityClass);
        car.setPrice(price);
        car.setImgPath(imgPath);
        car.setBase64Image(base64Image);
        car.setCurr_state(curr_state);

        return car;
    }
}
