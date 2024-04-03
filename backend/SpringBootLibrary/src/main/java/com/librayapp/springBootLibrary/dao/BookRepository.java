package com.librayapp.springBootLibrary.dao;

import com.librayapp.springBootLibrary.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // jpql
    // sql (@Query(value ='', nativeQuery=true, countQuery for pagination)
    @Query(value = "SELECT b FROM Book b WHERE b.title LIKE %?1%")
    Page<Book> findByTitleContaining(String title, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.category = ?1")
    Page<Book> findByCategory(String category, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.id IN :bookIdList")
    List<Book> findBooksByBookIds(List<Long> bookIdList);

    @Query(value = "SELECT b from Book b")
    Page<Book> findAllCustom(Pageable pageable);
}
