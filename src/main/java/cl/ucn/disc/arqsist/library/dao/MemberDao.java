/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.dao;

import cl.ucn.disc.arqsist.library.model.Member;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * DAO for Member entities.
 */
public final class MemberDao extends BaseDao<Member> {

    /**
     * Creates the member DAO.
     *
     * @param connectionSource database connection source
     */
    public MemberDao(ConnectionSource connectionSource) {
        super(connectionSource, Member.class);
    }

    /**
     * Finds members whose name contains the given query.
     *
     * @param query the name fragment to search
     * @return the matching members ordered by ID
     */
    public List<Member> searchByName(String query) {
        try {
            return dao.queryBuilder()
                    .orderBy("id", true)
                    .where()
                    .raw(
                            "instr(lower(name), lower(?)) > 0",
                            new SelectArg("name", query)
                    )
                    .query();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}