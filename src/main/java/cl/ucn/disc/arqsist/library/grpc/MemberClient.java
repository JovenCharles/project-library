/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.grpc;

import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.NotFoundException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The gRPC client of the Member Service.
 *
 * The client owns one ManagedChannel and one blocking stub.
 * Every method converts between the domain model and protobuf messages.
 */
public final class MemberClient implements AutoCloseable {

    /**
     * The deadline of each call, in seconds.
     */
    private static final long DEADLINE_SECONDS = 5;

    /**
     * The channel to the server.
     */
    private final ManagedChannel channel;

    /**
     * The blocking stub.
     */
    private final MemberServiceGrpc.MemberServiceBlockingStub stub;

    /**
     * Creates a client connected to the given host and port.
     *
     * @param host the server host
     * @param port the server port
     */
    public MemberClient(String host, int port) {
        this(
                ManagedChannelBuilder
                        .forAddress(host, port)
                        .usePlaintext()
                        .build()
        );
    }

    /**
     * Creates a client using an existing channel.
     *
     * @param channel the channel to the server
     */
    public MemberClient(ManagedChannel channel) {
        this.channel = channel;
        this.stub =
                MemberServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Registers a new member.
     *
     * @param member the member to register
     * @return the registered member
     */
    public Member register(Member member) {
        try {
            MemberMessage reply =
                    stub.withDeadlineAfter(
                                    DEADLINE_SECONDS,
                                    TimeUnit.SECONDS
                            )
                            .registerMember(
                                    RegisterMemberRequest
                                            .newBuilder()
                                            .setName(member.getName())
                                            .setEmail(member.getEmail())
                                            .build()
                            );

            return toMember(reply);

        } catch (StatusRuntimeException e) {
            throw toException(e);
        }
    }

    /**
     * Returns all members.
     *
     * @return the list of members
     */
    public List<Member> findAll() {
        try {
            ListMembersReply reply =
                    stub.withDeadlineAfter(
                                    DEADLINE_SECONDS,
                                    TimeUnit.SECONDS
                            )
                            .listMembers(
                                    ListMembersRequest
                                            .getDefaultInstance()
                            );

            return reply
                    .getMembersList()
                    .stream()
                    .map(MemberClient::toMember)
                    .toList();

        } catch (StatusRuntimeException e) {
            throw toException(e);
        }
    }

    /**
     * Finds a member by ID.
     *
     * @param id the member identifier
     * @return the member
     * @throws NotFoundException if the member does not exist
     */
    public Member findById(int id) {
        try {
            MemberMessage reply =
                    stub.withDeadlineAfter(
                                    DEADLINE_SECONDS,
                                    TimeUnit.SECONDS
                            )
                            .getMember(
                                    GetMemberRequest
                                            .newBuilder()
                                            .setId(id)
                                            .build()
                            );

            return toMember(reply);

        } catch (StatusRuntimeException e) {
            throw toException(e);
        }
    }

    /**
     * Closes the channel and waits for running calls to finish.
     */
    @Override
    public void close() {
        channel.shutdown();

        try {
            if (!channel.awaitTermination(
                    DEADLINE_SECONDS,
                    TimeUnit.SECONDS
            )) {
                channel.shutdownNow();
            }
        } catch (InterruptedException e) {
            channel.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Maps a gRPC status error to a domain exception.
     *
     * @param e the status error
     * @return the mapped exception
     */
    private static RuntimeException toException(
            StatusRuntimeException e
    ) {
        String description =
                e.getStatus().getDescription();

        return switch (e.getStatus().getCode()) {
            case NOT_FOUND ->
                    new NotFoundException(description);

            case INVALID_ARGUMENT ->
                    new IllegalArgumentException(description);

            case FAILED_PRECONDITION ->
                    new IllegalStateException(description);

            default ->
                    e;
        };
    }

    /**
     * Converts a protobuf message to a domain member.
     *
     * @param message the protobuf message
     * @return the domain member
     */
    private static Member toMember(
            MemberMessage message
    ) {
        Member member =
                new Member(
                        message.getName(),
                        message.getEmail()
                );

        member.setId(message.getId());

        return member;
    }
}