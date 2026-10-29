package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.ReservationDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseSeedTest {

    @Test
    void seedCreatesConsistentDataAndIsIdempotent() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");

        db.seedIfEmpty();

        BookDao bookDao = new BookDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());
        ReservationDao reservationDao =
                new ReservationDao(db.connectionSource());

        List<Loan> loans = loanDao.findAll();

        assertFalse(
                loans.isEmpty(),
                "seed should create loans"
        );

        assertFalse(
                reservationDao.findAll().isEmpty(),
                "seed should create reservations"
        );

        assertTrue(
                loans.stream().anyMatch(
                        loan ->
                                !loan.isReturned()
                                        && loan.getDueDate()
                                        .isBefore(LocalDate.now())
                ),
                "seed should contain at least one overdue open loan"
        );

        for (Loan loan : loans) {
            if (!loan.isReturned()) {
                Book book =
                        bookDao.findById(
                                loan.getBook().getId()
                        );

                assertTrue(
                        book.getAvailableCopies()
                                < book.getTotalCopies(),
                        "books with open loans should have fewer available copies"
                );
            }
        }

        int loanCount = loans.size();
        int reservationCount =
                reservationDao.findAll().size();

        db.seedIfEmpty();

        assertEquals(
                loanCount,
                loanDao.findAll().size(),
                "seed should not create duplicate loans"
        );

        assertEquals(
                reservationCount,
                reservationDao.findAll().size(),
                "seed should not create duplicate reservations"
        );
    }
}