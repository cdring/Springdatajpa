package com.cdring.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "article",path="articles")
public interface ArticleRepository extends JpaRepository<Article,Long> {

    Article findByAuthor(String author);

}