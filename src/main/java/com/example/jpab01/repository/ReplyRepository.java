package com.example.jpab01.repository;
/* 댓글 DB 작업을 사용하는 청구 (DB와 연결) */
import com.example.jpab01.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository
        extends JpaRepository<Reply, Long> {
    // (1) JpaRepository<Board, Long> : JPA가 제공하는 기본 CRUD기능
    // ex) save() findById() findAll() deleteByID() delete() count()
    // (2) BoardSearch : 우리가 만든 검색 기능(복잡)

    // JPQL 직접 작성 : "특정 게시글의 댓글만 가져와라."
    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);
    // ex) listOfBoard(1L, pageable) : 1번 게시글의 댓글들만 가져와라.
    // r.board.bno : Reply가 가지고 있는 Board의 bno

    void deleteByBoard_Bno(Long bno);
    // 게시글 지우면 댓글도 함께 지우는 기능
}