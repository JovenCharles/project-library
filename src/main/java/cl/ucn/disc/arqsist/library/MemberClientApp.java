/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library;

import cl.ucn.disc.arqsist.library.grpc.MemberClient;
import cl.ucn.disc.arqsist.library.grpc.MemberGrpcService;
import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.NotFoundException;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class of the member gRPC client.
 */
public final class MemberClientApp {

    /**
     * The logger.
     */
    private static final Logger log =
            LoggerFactory.getLogger(MemberClientApp.class);

    /**
     * Lists all members and then reads the members whose IDs are
     * provided as command-line arguments.
     *
     * @param args the member IDs to read
     */
    public static void main(String[] args) {

        try (MemberClient client =
                     new MemberClient(
                             MemberGrpcService.HOST,
                             MemberGrpcService.PORT
                     )) {

            for (Member member : client.findAll()) {
                log.info(
                        "member {}: {} <{}>",
                        member.getId(),
                        member.getName(),
                        member.getEmail()
                );
            }

            for (String arg : args) {
                Member member =
                        client.findById(
                                Integer.parseInt(arg)
                        );

                log.info(
                        "member {}: {} <{}>",
                        member.getId(),
                        member.getName(),
                        member.getEmail()
                );
            }

        } catch (NotFoundException e) {
            log.error(
                    "member not found: {}",
                    e.getMessage()
            );
            System.exit(1);

        } catch (IllegalArgumentException e) {
            log.error(
                    "invalid argument: {}",
                    e.getMessage()
            );
            System.exit(1);

        } catch (StatusRuntimeException e) {
            log.error(
                    "the gRPC call failed: {}",
                    e.getStatus()
            );
            System.exit(1);
        }
    }
}