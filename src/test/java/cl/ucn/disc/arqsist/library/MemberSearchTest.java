/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.dao.MemberDao;
import cl.ucn.disc.arqsist.library.db.Database;
import cl.ucn.disc.arqsist.library.grpc.MemberClient;
import cl.ucn.disc.arqsist.library.grpc.MemberGrpcService;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.MemberService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests member search through the gRPC round trip.
 */
final class MemberSearchTest {

    /**
     * Verifies all member search acceptance cases.
     *
     * @throws Exception if the in-process server fails
     */
    @Test
    void searchMembersMatchesAcceptanceCases() throws Exception {

        Database db =
                new Database("jdbc:sqlite::memory:");

        MemberDao memberDao =
                new MemberDao(db.connectionSource());

        MemberService memberService =
                new MemberService(memberDao);

        memberService.register(
                new Member(
                        "Ada Lovelace",
                        "ada@example.com"
                )
        );

        memberService.register(
                new Member(
                        "Grace Hopper",
                        "grace@example.com"
                )
        );

        memberService.register(
                new Member(
                        "Alan Turing",
                        "alan@example.com"
                )
        );

        String serverName =
                InProcessServerBuilder.generateName();

        Server server =
                InProcessServerBuilder
                        .forName(serverName)
                        .directExecutor()
                        .addService(
                                new MemberGrpcService(memberService)
                        )
                        .build()
                        .start();

        ManagedChannel channel =
                InProcessChannelBuilder
                        .forName(serverName)
                        .directExecutor()
                        .build();

        try (MemberClient client =
                     new MemberClient(channel)) {

            List<Member> ada =
                    client.search("ada");

            assertEquals(1, ada.size());
            assertEquals(
                    "Ada Lovelace",
                    ada.getFirst().getName()
            );

            List<Member> upperAda =
                    client.search("ADA");

            assertEquals(1, upperAda.size());
            assertEquals(
                    "Ada Lovelace",
                    upperAda.getFirst().getName()
            );

            List<Member> trimmedAda =
                    client.search(" ada ");

            assertEquals(1, trimmedAda.size());
            assertEquals(
                    "Ada Lovelace",
                    trimmedAda.getFirst().getName()
            );

            List<Member> allWithA =
                    client.search("a");

            assertEquals(3, allWithA.size());
            assertEquals(
                    "Ada Lovelace",
                    allWithA.get(0).getName()
            );
            assertEquals(
                    "Grace Hopper",
                    allWithA.get(1).getName()
            );
            assertEquals(
                    "Alan Turing",
                    allWithA.get(2).getName()
            );

            List<Member> noMatch =
                    client.search("zzz");

            assertEquals(
                    0,
                    noMatch.size()
            );

            List<Member> percent =
                    client.search("%");

            assertEquals(
                    0,
                    percent.size()
            );

            List<Member> underscore =
                    client.search("_");

            assertEquals(
                    0,
                    underscore.size()
            );

            assertThrows(
                    IllegalArgumentException.class,
                    () -> client.search("")
            );

            assertThrows(
                    IllegalArgumentException.class,
                    () -> client.search(" ")
            );

        } finally {
            server.shutdownNow();
        }
    }
}