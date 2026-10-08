package com.example.jpab01.repository.search;
/* "우리 게시판에는 이런 기능이 필요해." 라고 정의 (설계도) */
/* 구조 위치는 Service -> Repository -> SearchImpl -> DB */
import com.example.jpab01.domain.Board;
import com.example.jpab01.dto.BoardListAllDTO;
import com.example.jpab01.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types,
                          String keyword,
                          Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                      String keyword,
                                                      Pageable pageable);

//    Page<BoardListReplyCountDTO> searchWithAll(String[] types,
//                                               String keyword,
//                                               Pageable pageable);

    // 위 메소드에서 게시글 목록 조회에서 '게시글 정보 + 댓글 수 + 이미지 목록'까지 한 번에 DTO로 만들어서 보내려고
    // BoardListAllDTO 사용
    Page<BoardListAllDTO> searchWithAll(String[] types,
                                        String keyword,
                                        Pageable pageable);

    // 최종판 : 위 함수들 + 이미지에 대한 정보까지 추가
}
