package com.example.jpab01.service;
/* 댓글 기능에서 필요한 업무 목록을 정의해놓은 인터페이스 */
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;
import com.example.jpab01.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

}
