package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.BookService;
import cl.ucn.disc.arqsist.library.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccruedFeeTest {

    @Test
    void findAllCalculatesAccruedFeeForOpenOverdueLoans() throws Exception {
        Database db = new Database("jdbc:sqlite::memory:");

        BookDao bookDao = new BookDao(db.connectionSource());
        MemberDao memberDao = new MemberDao(db.connectionSource());
        LoanDao loanDao = new LoanDao(db.connectionSource());

        BookService bookService = new BookService(bookDao);

        Clock clock = Clock.fixed(
                Instant.parse("2026-01-15T10:00:00Z"),
                ZoneOffset.UTC
        );

        LoanService loanService = new LoanService(
                loanDao,
                memberDao,
                bookService,
                clock
        );

        Book book = new Book(
                "Refactoring",
                "Martin Fowler",
                "9780201485677",
                2
        );
        bookDao.create(book);

        Member member = new Member(
                "Ada Lovelace",
                "ada@example.com"
        );
        memberDao.create(member);

        Loan overdueLoan = new Loan(
                member,
                book,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 22)
        );
        loanDao.create(overdueLoan);

        Loan activeLoan = new Loan(
                member,
                book,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 2, 1)
        );
        loanDao.create(activeLoan);

        List<Loan> loans = loanService.findAll();

        Loan reloadedOverdue = loans.stream()
                .filter(loan -> loan.getId() == overdueLoan.getId())
                .findFirst()
                .orElseThrow();

        Loan reloadedActive = loans.stream()
                .filter(loan -> loan.getId() == activeLoan.getId())
                .findFirst()
                .orElseThrow();

        assertEquals(
                24.0,
                reloadedOverdue.getOverdueFee()
        );

        assertEquals(
                0.0,
                reloadedActive.getOverdueFee()
        );
    }
}