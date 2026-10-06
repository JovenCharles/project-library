/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.dao.LoanDao;
import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.model.Loan;
import cl.ucn.disc.arqsist.library.model.Member;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides operations related to members and loan checkout.
 */
public final class MemberService {

    /**
     * DAO used to access member data.
     */
    private final MemberDao memberDao;

    /**
     * DAO used to access book data.
     */
    private final BookDao bookDao;

    /**
     * DAO used to access loan data.
     */
    private final LoanDao loanDao;

    /**
     * Creates the member service.
     *
     * @param memberDao the member DAO
     * @param bookDao the book DAO
     * @param loanDao the loan DAO
     */
    public MemberService(MemberDao memberDao, BookDao bookDao, LoanDao loanDao) {
        this.memberDao = memberDao;
        this.bookDao = bookDao;
        this.loanDao = loanDao;
    }

    /**
     * Registers a new member.
     *
     * @param member the member to register
     * @return the registered member
     * @throws SQLException if the database operation fails
     */
    public Member register(Member member) throws SQLException {
        memberDao.create(member);
        return member;
    }

    /**
     * Returns all registered members.
     *
     * @return the list of members
     * @throws SQLException if the database operation fails
     */
    public List<Member> findAll() throws SQLException {
        return memberDao.findAll();
    }

    /**
     * Creates a loan for the selected member and book.
     *
     * @param memberId the member identifier
     * @param bookId the book identifier
     * @return the created loan
     * @throws SQLException if the database operation fails
     */
    public Loan checkout(int memberId, int bookId) throws SQLException {
        Member member = memberDao.findById(memberId);
        Book book = bookDao.findById(bookId);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDao.update(book);

        LocalDate today = LocalDate.now();
        LocalDate dueDate = LoanPolicy.computeDueDate(today);

        Loan loan = new Loan(member, book, today, dueDate);
        loanDao.create(loan);
        return loan;
    }
}