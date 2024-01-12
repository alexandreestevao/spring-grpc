package com.estevaoestevao.springgrpc.repository;

import com.estevaoestevao.springgrpc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
}
