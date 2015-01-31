/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parametersapi;

/**
 *
 * @author Pete
 */
public final class Parameter {

    private final String id;
    private final String name;
    private final String description;
    private String value;

    public Parameter(String id, String name, String description, String value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
