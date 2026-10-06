/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides operations related to reservations.
 */
public final class ReservationService {

    /**
     * DAO used to access reservation data.
     */
    private final ReservationDao reservationDao;

    /**
     * DAO used to access book data.
     */
    private final BookDao bookDao;

    /**
     * DAO used to access member data.
     */
    private final MemberDao memberDao;

    /**
     * DAO used to access loan data.
     */
    private final LoanDao loanDao;

    /**
     * Creates the reservation service.
     *
     * @param reservationDao the reservation DAO
     * @param bookDao the book DAO
     * @param memberDao the member DAO
     * @param loanDao the loan DAO
     */
    public ReservationService(
            ReservationDao reservationDao,
            BookDao bookDao,
            MemberDao memberDao,
            LoanDao loanDao
    ) {
        this.reservationDao = reservationDao;
        this.bookDao = bookDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
    }

    /**
     * Creates a reservation for a member and a book.
     *
     * @param bookId the book identifier
     * @param memberId the member identifier
     * @return the created reservation
     * @throws SQLException if the database operation fails
     */
    public Reservation reserve(int bookId, int memberId) throws SQLException {
        Book book = bookDao.findById(bookId);
        Member member = memberDao.findById(memberId);

        Reservation reservation = new Reservation(
                member,
                book,
                LocalDate.now()
        );

        reservationDao.create(reservation);
        return reservation;
    }

    /**
     * Returns all reservations.
     *
     * @return the list of reservations
     * @throws SQLException if the database operation fails
     */
    public List<Reservation> findAll() throws SQLException {
        return reservationDao.findAll();
    }

    /**
     * Fulfills a reservation and creates a loan.
     *
     * @param reservationId the reservation identifier
     * @return the created loan
     * @throws IllegalStateException if the reservation does not exist or was already fulfilled
     * @throws SQLException if the database operation fails
     */
    public Loan fulfill(int reservationId) throws SQLException {
        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null || reservation.isFulfilled()) {
            throw new IllegalStateException("Reservation not available");
        }

        reservation.setFulfilled(true);
        reservationDao.update(reservation);

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LoanPolicy.computeDueDate(today);

        Loan loan = new Loan(
                reservation.getMember(),
                reservation.getBook(),
                today,
                dueDate
        );

        loanDao.create(loan);
        return loan;
    }
}