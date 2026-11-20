/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.service;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.model.Member;

import java.util.List;

/**
 * Provides operations related to library members.
 */
public final class MemberService {

    /**
     * DAO used to access member data.
     */
    private final MemberDao memberDao;

    /**
     * Creates the member service.
     *
     * @param memberDao the member DAO
     */
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    /**
     * Registers a new member.
     *
     * @param member the member to register
     * @return the registered member
     */
    public Member register(Member member) {
        memberDao.create(member);
        return member;
    }

    /**
     * Returns all registered members.
     *
     * @return the list of members
     */
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    /**
     * Finds a member by ID.
     *
     * @param id the member identifier
     * @return the member
     * @throws NotFoundException if the member does not exist
     */
    public Member findById(int id) {
        Member member = memberDao.findById(id);

        if (member == null) {
            throw new NotFoundException(
                    "Member not found: " + id
            );
        }

        return member;
    }
    /**
     * Searches members by a name fragment.
     *
     * @param query the name fragment
     * @return matching members
     * @throws IllegalArgumentException if the query is blank
     */
    public List<Member> search(String query) {
        String trimmedQuery = query.trim();

        if (trimmedQuery.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query must not be blank"
            );
        }

        return memberDao.searchByName(trimmedQuery);
    }
}