package com.example.jpab01.repository;
/* 게시판 DB 작업을 사용하는 청구 (DB와 연결) */
import com.example.jpab01.domain.Board;
import com.example.jpab01.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository
        extends JpaRepository<Board, Long>, BoardSearch {
    // (1) JpaRepository<Board, Long> : JPA가 제공하는 기본 CRUD기능 ex) save() findById() delete()
    // (2) BoardSearch : 우리가 만든 검색 기능(복잡)

    // 메소드 이름으로 쿼리 생성
    List<Board> findByWriter(String writer);

    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword,
                                                    Pageable pageable);

    long countByWriter(String writer);

    // JPQL 직접 작성
    @Query("select b from Board b where b.title like concat('%', "
            + ":keyword, '%') order by b.bno desc")
    Page<Board> findByKeyword(String keyword, Pageable pageable);
}
