package com.daroch.tickets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfiguration {}
// Why we need this
//
// This is basically enabling JPA Auditing in Spring Boot.
// Spring Data JPA has an auditing feature — it automatically keeps track of certain fields in your entities, like:
// Created date
// Last modified date
// Created by
// Last modified by
// Instead of manually writing logic to store timestamps, Spring can do it for you.
