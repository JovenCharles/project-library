/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents a member registered in the library.
 */
@DatabaseTable(tableName = "members")
public final class Member {

    /**
     * Member identifier.
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * Member name.
     */
    @DatabaseField(canBeNull = false)
    private String name;

    /**
     * Member email address.
     */
    @DatabaseField(canBeNull = false)
    private String email;

    /**
     * Empty constructor required by ORMLite.
     */
    public Member() {
    }

    /**
     * Creates a new member.
     *
     * @param name the member name
     * @param email the member email address
     */
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the member identifier.
     *
     * @return the member identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the member identifier.
     *
     * @param id the member identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member name.
     *
     * @return the member name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the member name.
     *
     * @param name the member name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the member email address.
     *
     * @return the member email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the member email address.
     *
     * @param email the member email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}