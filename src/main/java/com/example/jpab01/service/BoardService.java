package com.example.jpab01.service;
/* 게시판에서 필요한 기능의 목록/규칙 */
import com.example.jpab01.dto.BoardDTO;
import com.example.jpab01.dto.BoardListReplyCountDTO;
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
}
