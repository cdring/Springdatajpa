package com.cdring.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "reader",path="readers")
public interface ReaderRepository extends JpaRepository<Reader,Long> {


}