package com.estevaoestevao.springgrpc.controller;

import com.alexandreestevao.v1.user.*;
import com.estevaoestevao.springgrpc.domain.User;
import com.estevaoestevao.springgrpc.repository.IUserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class UserController extends UserServiceGrpc.UserServiceImplBase {

    private final IUserRepository repository;

    public UserController(IUserRepository repository) {
        this.repository = repository;
    }

    // onNext(value) - Returns the class object, example UserRes.
    // onError(Throwable t) - Catch an error exception for handling.
    // onCompleted - Returning success in onNext, onCompleted ends the flow.

    @Override
    public void create(UserReq request, StreamObserver<UserRes> responseObserver) {
        User user = new User(request.getName(), request.getName());
        User saved = repository.save(user);
        UserRes userRes = UserRes.newBuilder()
                .setId(saved.getId())
                .setName(saved.getName())
                .setEmail(saved.getEmail())
                .build();
        responseObserver.onNext(userRes);
        responseObserver.onCompleted();
    }

    @Override
    public void getAll(EmptyReq request, StreamObserver<UserResList> responseObserver) {
        List<User> users = repository.findAll();
        List<UserRes> userRes = users.stream()
                .map(user -> UserRes.newBuilder()
                        .setId(user.getId())
                        .setName(user.getName())
                        .setEmail(user.getEmail())
                        .build())
                .toList();
        UserResList userResList = UserResList.newBuilder().addAllUsers(userRes).build();
        responseObserver.onNext(userResList);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllServerStream(EmptyReq request, StreamObserver<UserRes> responseObserver) {
        repository.findAll().forEach(user -> {
            UserRes userRes = UserRes.newBuilder()
                    .setId(user.getId())
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            responseObserver.onNext(userRes);
        });
        responseObserver.onCompleted();;
    }
}
