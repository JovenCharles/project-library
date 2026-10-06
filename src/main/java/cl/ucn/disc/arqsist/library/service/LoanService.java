/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Provides operations related to loans.
 */
public final class LoanService {

    /**
     * DAO used to access loan data.
     */
    private final LoanDao loanDao;

    /**
     * DAO used to access book data.
     */
    private final BookDao bookDao;

    /**
     * Creates the loan service.
     *
     * @param loanDao the loan DAO
     * @param bookDao the book DAO
     */
    public LoanService(LoanDao loanDao, BookDao bookDao) {
        this.loanDao = loanDao;
        this.bookDao = bookDao;
    }

    /**
     * Returns all loans.
     *
     * @return the list of loans
     * @throws SQLException if the database operation fails
     */
    public List<Loan> findAll() throws SQLException {
        return loanDao.findAll();
    }

    /**
     * Returns a borrowed book and updates the loan information.
     *
     * @param loanId the loan identifier
     * @return the updated loan, or null if the loan does not exist
     * @throws SQLException if the database operation fails
     */
    public Loan returnLoan(int loanId) throws SQLException {
        Loan loan = loanDao.findById(loanId);

        if (loan == null || loan.isReturned()) {
            return loan;
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        LocalDate due = loan.getDueDate();
        LocalDate today = LocalDate.now();

        if (today.isAfter(due)) {
            long daysOverdue = ChronoUnit.DAYS.between(due, today);
            loan.setOverdueFee(daysOverdue * LoanPolicy.FEE_PER_DAY);
        }

        loanDao.update(loan);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookDao.update(book);

        return loan;
    }

    /**
     * Returns all open loans whose due date has passed.
     *
     * @return the list of overdue loans
     * @throws SQLException if the database operation fails
     */
    public List<Loan> overdueLoans() throws SQLException {
        LocalDate today = LocalDate.now();

        return loanDao.findAll().stream()
                .filter(loan -> !loan.isReturned())
                .filter(loan -> loan.getDueDate().isBefore(today))
                .toList();
    }
}