/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.model;

import cl.ucn.disc.arqsist.library.db.LocalDatePersister;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;

/**
 * Represents a book loan made by a member.
 */
@DatabaseTable(tableName = "loans")
public final class Loan {

    /**
     * Loan identifier.
     */
    @DatabaseField(generatedId = true)
    private int id;

    /**
     * Member associated with the loan.
     */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Member member;

    /**
     * Book associated with the loan.
     */
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Book book;

    /**
     * Date when the loan was created.
     */
    @DatabaseField(canBeNull = false, persisterClass = LocalDatePersister.class)
    private LocalDate loanDate;

    /**
     * Date when the loan is due.
     */
    @DatabaseField(canBeNull = false, persisterClass = LocalDatePersister.class)
    private LocalDate dueDate;

    /**
     * Date when the book was returned.
     */
    @DatabaseField(persisterClass = LocalDatePersister.class)
    private LocalDate returnDate;

    /**
     * Indicates whether the book has been returned.
     */
    @DatabaseField
    private boolean returned;

    /**
     * Fee charged for overdue days.
     */
    @DatabaseField
    private double overdueFee;

    /**
     * Empty constructor required by ORMLite.
     */
    public Loan() {
    }

    /**
     * Creates a new loan.
     *
     * @param member the member borrowing the book
     * @param book the borrowed book
     * @param loanDate the loan date
     * @param dueDate the due date
     */
    public Loan(Member member, Book book, LocalDate loanDate, LocalDate dueDate) {
        this.member = member;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.overdueFee = 0.0;
    }

    /**
     * Returns the loan identifier.
     *
     * @return the loan identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the loan identifier.
     *
     * @param id the loan identifier
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the member associated with the loan.
     *
     * @return the member
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the member associated with the loan.
     *
     * @param member the member
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Returns the borrowed book.
     *
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the borrowed book.
     *
     * @param book the book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Returns the loan date.
     *
     * @return the loan date
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Sets the loan date.
     *
     * @param loanDate the loan date
     */
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * Returns the due date.
     *
     * @return the due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate the due date
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Returns the return date.
     *
     * @return the return date, or null if the book has not been returned
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the return date.
     *
     * @param returnDate the return date
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Indicates whether the loan has been returned.
     *
     * @return true if the book has been returned
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * Sets whether the loan has been returned.
     *
     * @param returned true if the book has been returned
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Returns the overdue fee.
     *
     * @return the overdue fee
     */
    public double getOverdueFee() {
        return overdueFee;
    }

    /**
     * Sets the overdue fee.
     *
     * @param overdueFee the overdue fee
     */
    public void setOverdueFee(double overdueFee) {
        this.overdueFee = overdueFee;
    }
}