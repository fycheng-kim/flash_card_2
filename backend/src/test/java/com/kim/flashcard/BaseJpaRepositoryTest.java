package com.kim.flashcard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
public abstract class BaseJpaRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariadb = new MariaDBContainer<>("mariadb:latest");

    @Autowired
    protected TestEntityManager entityManager;
}