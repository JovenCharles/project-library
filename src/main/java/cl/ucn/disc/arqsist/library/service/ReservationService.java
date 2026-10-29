/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.model.Reservation;

import java.sql.SQLException;
import java.time.Clock;
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
     * DAO used to access member data.
     */
    private final MemberDao memberDao;

    /**
     * DAO used to access loan data.
     */
    private final LoanDao loanDao;

    /**
     * Service used to manage books and inventory.
     */
    private final BookService bookService;

    /**
     * Clock used to obtain the current date.
     */
    private final Clock clock;

    /**
     * Creates the reservation service.
     *
     * @param reservationDao the reservation DAO
     * @param memberDao the member DAO
     * @param loanDao the loan DAO
     * @param bookService the book service
     * @param clock the clock used to obtain the current date
     */
    public ReservationService(
            ReservationDao reservationDao,
            MemberDao memberDao,
            LoanDao loanDao,
            BookService bookService,
            Clock clock
    ) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.loanDao = loanDao;
        this.bookService = bookService;
        this.clock = clock;
    }

    /**
     * Creates a reservation for a member and a book.
     *
     * @param bookId the book identifier
     * @param memberId the member identifier
     * @return the created reservation
     * @throws NotFoundException if the book or member does not exist
     */
    public Reservation reserve(int bookId, int memberId) {
        Book book = bookService.findById(bookId);

        if (book == null) {
            throw new NotFoundException(
                    "Book not found: " + bookId
            );
        }

        Member member = memberDao.findById(memberId);

        if (member == null) {
            throw new NotFoundException(
                    "Member not found: " + memberId
            );
        }

        Reservation reservation = new Reservation(
                member,
                book,
                LocalDate.now(clock)
        );

        reservationDao.create(reservation);

        return reservation;
    }

    /**
     * Returns all reservations.
     *
     * @return the list of reservations
     */
    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    /**
     * Fulfills a reservation and creates the corresponding loan.
     *
     * @param reservationId the reservation identifier
     * @return the created loan
     * @throws SQLException if the transaction cannot be completed
     * @throws NotFoundException if the reservation does not exist
     * @throws IllegalStateException if the reservation was already fulfilled
     */
    public Loan fulfill(int reservationId) throws SQLException {
        Reservation reservation =
                reservationDao.findById(reservationId);

        if (reservation == null) {
            throw new NotFoundException(
                    "Reservation not found: " + reservationId
            );
        }

        if (reservation.isFulfilled()) {
            throw new IllegalStateException(
                    "Reservation already fulfilled: " + reservationId
            );
        }

        LocalDate today = LocalDate.now(clock);
        LocalDate dueDate =
                LoanPolicy.computeDueDate(today);

        return reservationDao.transaction(() -> {
            reservation.setFulfilled(true);
            reservationDao.update(reservation);

            bookService.borrow(
                    reservation.getBook().getId()
            );

            Loan loan = new Loan(
                    reservation.getMember(),
                    reservation.getBook(),
                    today,
                    dueDate
            );

            loanDao.create(loan);

            return loan;
        });
    }
}