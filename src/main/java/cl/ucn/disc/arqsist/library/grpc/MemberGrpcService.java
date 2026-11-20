/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */

package cl.ucn.disc.arqsist.library.grpc;

import cl.ucn.disc.arqsist.library.model.Member;
import cl.ucn.disc.arqsist.library.service.MemberService;
import cl.ucn.disc.arqsist.library.service.NotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.List;

/**
 * The gRPC adapter for the Member Service.
 *
 * The class extends the base class that protoc generates from member.proto.
 * Each unary RPC of the proto file maps to one method below.
 */
public final class MemberGrpcService
        extends MemberServiceGrpc.MemberServiceImplBase {

    /**
     * The address of the gRPC server.
     */
    public static final String HOST = "127.0.0.1";

    /**
     * The port of the gRPC server.
     */
    public static final int PORT = 50051;

    /**
     * The member service.
     */
    private final MemberService memberService;

    /**
     * Creates the gRPC adapter.
     *
     * @param memberService the member service
     */
    public MemberGrpcService(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Registers a new member.
     *
     * @param request the registration data
     * @param observer the reply observer
     */
    @Override
    public void registerMember(
            RegisterMemberRequest request,
            StreamObserver<MemberMessage> observer
    ) {
        try {
            Member member = memberService.register(
                    new Member(
                            request.getName(),
                            request.getEmail()
                    )
            );

            observer.onNext(toMessage(member));
            observer.onCompleted();

        } catch (RuntimeException e) {
            observer.onError(toError(e));
        }
    }

    /**
     * Lists all members.
     *
     * @param request the empty request
     * @param observer the reply observer
     */
    @Override
    public void listMembers(
            ListMembersRequest request,
            StreamObserver<ListMembersReply> observer
    ) {
        try {
            List<MemberMessage> members =
                    memberService.findAll()
                            .stream()
                            .map(MemberGrpcService::toMessage)
                            .toList();

            observer.onNext(
                    ListMembersReply.newBuilder()
                            .addAllMembers(members)
                            .build()
            );

            observer.onCompleted();

        } catch (RuntimeException e) {
            observer.onError(toError(e));
        }
    }

    /**
     * Gets one member by ID.
     *
     * @param request the request containing the member ID
     * @param observer the reply observer
     */
    @Override
    public void getMember(
            GetMemberRequest request,
            StreamObserver<MemberMessage> observer
    ) {
        try {
            observer.onNext(
                    toMessage(
                            memberService.findById(
                                    request.getId()
                            )
                    )
            );

            observer.onCompleted();

        } catch (RuntimeException e) {
            observer.onError(toError(e));
        }
    }

    /**
     * Maps a domain exception to a gRPC status exception.
     *
     * @param e the domain exception
     * @return the status exception
     */
    private static StatusRuntimeException toError(RuntimeException e) {
        Status status = switch (e) {
            case NotFoundException x ->
                    Status.NOT_FOUND;

            case IllegalArgumentException x ->
                    Status.INVALID_ARGUMENT;

            case IllegalStateException x ->
                    Status.FAILED_PRECONDITION;

            default ->
                    Status.INTERNAL;
        };

        return status
                .withDescription(e.getMessage())
                .withCause(e)
                .asRuntimeException();
    }

    /**
     * Converts a domain member to a protobuf message.
     *
     * @param member the domain member
     * @return the protobuf message
     */
    private static MemberMessage toMessage(Member member) {
        return MemberMessage.newBuilder()
                .setId(member.getId())
                .setName(member.getName())
                .setEmail(member.getEmail())
                .build();
    }
}