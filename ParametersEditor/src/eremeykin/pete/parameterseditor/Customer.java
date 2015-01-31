/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pete
 */
public class Customer {

    private String name;

    private String city;

    private int age;

    private boolean married;

    public Customer(String name, String city, int age, boolean married) {
        this.name = name;
        this.city = city;
        this.age = age;
        this.married = married;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the married
     */
    public boolean isMarried() {
        return married;
    }

    /**
     * @param married the married to set
     */
    public void setMarried(boolean married) {
        this.married = married;
    }

}
