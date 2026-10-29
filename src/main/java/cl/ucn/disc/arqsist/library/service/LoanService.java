/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.time.Clock;
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
     * DAO used to access member data.
     */
    private final MemberDao memberDao;

    /**
     * Service used to manage book inventory.
     */
    private final BookService bookService;

    /**
     * Clock used to obtain the current date.
     */
    private final Clock clock;

    /**
     * Creates the loan service.
     *
     * @param loanDao the loan DAO
     * @param memberDao the member DAO
     * @param bookService the book service
     * @param clock the clock used to obtain the current date
     */
    public LoanService(
            LoanDao loanDao,
            MemberDao memberDao,
            BookService bookService,
            Clock clock
    ) {
        this.loanDao = loanDao;
        this.memberDao = memberDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Returns all loans with accrued fees calculated for open overdue loans.
     *
     * @return the list of loans
     */
    public List<Loan> findAll() {
        return withAccruedFees(loanDao.findAll());
    }

    /**
     * Creates a loan for a member and a book.
     *
     * @param memberId the member identifier
     * @param bookId the book identifier
     * @return the created loan
     * @throws SQLException if the transaction cannot be completed
     * @throws NotFoundException if the member or book does not exist
     * @throws IllegalStateException if the book has no available copies
     */
    public Loan checkout(int memberId, int bookId) throws SQLException {
        Member member = memberDao.findById(memberId);

        if (member == null) {
            throw new NotFoundException(
                    "Member not found: " + memberId
            );
        }

        Book book = bookService.findById(bookId);

        if (book == null) {
            throw new NotFoundException(
                    "Book not found: " + bookId
            );
        }

        LocalDate today = LocalDate.now(clock);
        LocalDate dueDate = LoanPolicy.computeDueDate(today);

        return loanDao.transaction(() -> {
            bookService.borrow(bookId);

            Loan loan = new Loan(
                    member,
                    book,
                    today,
                    dueDate
            );

            loanDao.create(loan);

            return loan;
        });
    }

    /**
     * Returns a borrowed book and updates the corresponding loan.
     *
     * @param loanId the loan identifier
     * @return the updated loan
     * @throws SQLException if the transaction cannot be completed
     * @throws NotFoundException if the loan does not exist
     * @throws IllegalStateException if the loan was already returned
     */
    public Loan returnLoan(int loanId) throws SQLException {
        Loan loan = loanDao.findById(loanId);

        if (loan == null) {
            throw new NotFoundException(
                    "Loan not found: " + loanId
            );
        }

        if (loan.isReturned()) {
            throw new IllegalStateException(
                    "Loan already returned: " + loanId
            );
        }

        LocalDate today = LocalDate.now(clock);

        loan.setReturned(true);
        loan.setReturnDate(today);

        if (today.isAfter(loan.getDueDate())) {
            loan.setOverdueFee(
                    ChronoUnit.DAYS.between(
                            loan.getDueDate(),
                            today
                    ) * LoanPolicy.FEE_PER_DAY
            );
        }

        return loanDao.transaction(() -> {
            loanDao.update(loan);
            bookService.returnCopy(loan.getBook().getId());

            return loan;
        });
    }

    /**
     * Returns all open loans whose due date has passed.
     *
     * @return the list of overdue loans
     */
    public List<Loan> overdueLoans() {
        LocalDate today = LocalDate.now(clock);

        return loanDao.findAll().stream()
                .filter(loan -> !loan.isReturned())
                .filter(loan -> loan.getDueDate().isBefore(today))
                .toList();
    }

    /**
     * Calculates accrued fees for every open overdue loan.
     * The calculated fees are not persisted in the database.
     *
     * @param loans the loans to process
     * @return the loans with their accrued fees calculated
     */
    private List<Loan> withAccruedFees(List<Loan> loans) {
        LocalDate today = LocalDate.now(clock);

        loans.stream()
                .filter(loan -> !loan.isReturned())
                .filter(loan -> loan.getDueDate().isBefore(today))
                .forEach(loan -> loan.setOverdueFee(accruedFee(loan)));

        return loans;
    }

    /**
     * Calculates the accrued overdue fee for a loan.
     *
     * @param loan the loan
     * @return the accrued overdue fee
     */
    private double accruedFee(Loan loan) {
        LocalDate today = LocalDate.now(clock);

        return ChronoUnit.DAYS.between(
                loan.getDueDate(),
                today
        ) * LoanPolicy.FEE_PER_DAY;
    }
}