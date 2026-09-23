package com.example.jpab01.repository;
/* 게시판 DB 작업을 사용하는 창구 (DB와 연결) */
import com.example.jpab01.domain.Board;
import com.example.jpab01.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository
        extends JpaRepository<Board, Long>, BoardSearch {
    // (1) JpaRepository<Board, Long> : JPA가 제공하는 기본 CRUD기능
    //      ex) save() findById() delete()
    // (2) 내가 직접 선언한 메서드
    // (3) 내가 직접 JPQL 작성
    // (4) BoardSearch : QueryDSL 등으로 직접 구현, 복잡한 기능 검색


    /* (2) 내가 직접 선언한 메서드, 내용 없어도 JPA가 알아서 이름 분석해서 쿼리 만들어 줌 */
    List<Board> findByWriter(String writer);
    // 대략 SELECT * FROM board WHERE writer = '이순신' 같은 쿼리 생성
    // -> writer가 이순신인 게시글 저장
    Page<Board> findByTitleContainingOrderByBnoDesc(String keyword,
                                                    Pageable pageable);
    // 대략 SELECT * FROM board WHERE title like '%keyword%' ORDER BY bno desc 같은 쿼리 만들어지고
    // -> Page<Board>에 저장

    long countByWriter(String writer);
    // 대략 SELECT COUNT(*) FROM board WHERE writer = '홍길동' 같은 쿼리 생성
    // -> 홍길동이 작성한 게시글 개수 반환


    /* (3) 내가 직접 JPQL 작성 */
    @Query("select b from Board b where b.title like concat('%', :keyword, '%') order by b.bno desc")
    Page<Board> findByKeyword(String keyword, Pageable pageable);
    // @Query : "Spring아, 이 메서드가 실행되면 괄호 안 쿼리(JPQL)를 사용해."
    // Controller에서 boardRepository.findByKeyword("스프링", pageable); 호출하면
    // keyword = "스프링"이 들어감 -> :keyword가 "스프링"이라는 값으로 JPQL 실행됨
    // -> 게시글을 Page 형태로 반환


    /* Lazy 로딩 무시하고 BoardImage같이 조회 */
    @EntityGraph(attributePaths = {"imageSet"})
    // @EntityGraph : "이번 조회에서는 어떤 연관 데이터를 같이 가져올지 지정하는 것"
    // 특정 번호의 Board를 가져오면서, 그 Board의 imageSet도 같이 가져와!" //
    // 즉, Board의 fetch=FetchType.LAZY 때문에 BoardImage는 아직 안 가져온 상태.
    @Query("select b from Board b where b.bno = :bno")
    // "Board 중에서 bno가 전달받은 값과 같은 Board를 하나 찾아라."
    Optional<Board> findByIdWithImages(Long bno);
    // 이번에만 이 메서드로 인해 bno가 bno인 Board를 찾고, + imageSet 같이 가져옴
}
