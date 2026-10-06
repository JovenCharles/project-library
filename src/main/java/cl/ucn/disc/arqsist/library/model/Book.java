/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents a book available in the library.
 */
@DatabaseTable(tableName = "books")
public final class Book {

    /**
     * Book identifier.
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * Book title.
     */
    @DatabaseField(canBeNull = false)
    private String title;

    /**
     * Book author.
     */
    @DatabaseField(canBeNull = false)
    private String author;

    /**
     * Book ISBN.
     */
    @DatabaseField(canBeNull = false)
    private String isbn;

    /**
     * Total number of copies of the book.
     */
    @DatabaseField(canBeNull = false)
    private int totalCopies;

    /**
     * Number of currently available copies.
     */
    @DatabaseField(canBeNull = false)
    private int availableCopies;

    /**
     * Empty constructor required by ORMLite.
     */
    public Book() {
    }

    /**
     * Creates a new book.
     *
     * @param title the book title
     * @param author the book author
     * @param isbn the book ISBN
     * @param totalCopies the total number of copies
     */
    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    /**
     * Returns the book identifier.
     *
     * @return the book identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the book identifier.
     *
     * @param id the book identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the book title.
     *
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the book title.
     *
     * @param title the book title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the book author.
     *
     * @return the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the book author.
     *
     * @param author the book author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the book ISBN.
     *
     * @return the book ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the book ISBN.
     *
     * @param isbn the book ISBN
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the total number of copies.
     *
     * @return the total number of copies
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Sets the total number of copies.
     *
     * @param totalCopies the total number of copies
     */
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    /**
     * Returns the number of available copies.
     *
     * @return the number of available copies
     */
    public int getAvailableCopies() {
        return availableCopies;
    }

    /**
     * Sets the number of available copies.
     *
     * @param availableCopies the number of available copies
     */
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}