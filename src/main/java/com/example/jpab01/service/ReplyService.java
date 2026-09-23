package com.example.jpab01.service;
/* 댓글 기능에서 필요한 업무 목록을 정의해놓은 인터페이스 */
// ( ) : 이 메서드가 일을 하기 위해 외부에서 받아와야 하는 값 (매개변수)
import com.example.jpab01.dto.PageRequestDTO;
import com.example.jpab01.dto.PageResponseDTO;
import com.example.jpab01.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);
    // ReplyDTO : 자료형, replyDTO : 변수 이름
    // 이 안에 들어가는 정보 : bno = 100 / replyText = "안녕하세요" / replyer = "홍길동"

    ReplyDTO read(Long rno);
    // Long : 자료형, rno : 댓글 번호를 담을 변수
    // 호출하면 replyService.read(101L)

    void modify(ReplyDTO replyDTO);
    // 이 안에 들어가는 정보 (rno, replyText) : 수정할 댓글 정보

    void remove(Long rno);
    // "이 댓글 삭제해줘."

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno,
                                             PageRequestDTO pageRequestDTO);
    // bno : 어느 게시글의 댓글을 가져올 것인지
    // PageRequestDTO : 페이징 정보를 전달하는 객체 ("100번 게시글의 댓글 중에서 2페이지를 가져와. 한 페이지에 10개씩.")

    // getListOfBoard(100L, pageRequestDTO) :
    // "100번 게시글의 댓글을 가져오는데, pageRequestDTO에 들어있는 페이지/개수 조건에 맞춰서 가져와라."


}

// ★★★ 괄호 안 = 입력값, 메서드 이름 앞 = 출력값
// ex) ReplyDTO read(Long rno) : 댓글 번호를 넣으면 댓글DTO를 돌려준다.
// ex) void remove(Long rno) : 댓글 번호를 넣으면 삭제하고 아무것도 돌려주지 않는다.
// ex) Long register(ReplyDTO replyDTO) : 댓글 정보를 넣으면 등록하고 Long 값을 돌려준다.