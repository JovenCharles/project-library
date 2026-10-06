/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.controller;

import cl.ucn.disc.arqsist.library.dao.BookDao;
import cl.ucn.disc.arqsist.library.model.Book;
import cl.ucn.disc.arqsist.library.service.BookService;
import io.javalin.config.JavalinConfig;

/**
 * Registers HTTP routes related to books.
 */
public final class BookController {

    /**
     * Service used for book operations.
     */
    private final BookService service;

    /**
     * DAO used to access book data.
     */
    private final BookDao dao;

    /**
     * Creates the book controller.
     *
     * @param service the book service
     * @param dao the book DAO
     */
    public BookController(BookService service, BookDao dao) {
        this.service = service;
        this.dao = dao;
    }

    /**
     * Registers the book routes in the Javalin configuration.
     *
     * @param config the Javalin configuration
     */
    public void register(JavalinConfig config) {
        config.routes.get(
                "/books",
                ctx -> ctx.json(dao.findAll())
        );

        config.routes.get(
                "/books/{id}",
                ctx -> ctx.json(
                        service.findById(
                                Integer.parseInt(ctx.pathParam("id"))
                        )
                )
        );

        config.routes.post(
                "/books",
                ctx -> ctx.json(
                        service.create(
                                ctx.bodyAsClass(Book.class)
                        )
                )
        );
    }
}