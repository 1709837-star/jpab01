package com.example.jpab01.service;
/* 게시판에서 필요한 기능의 목록/규칙 */
import com.example.jpab01.dto.*;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    // BoardDTO boardDTO : 등록할 게시글 정보
    // 안에 들어가는 정보 : title = "홍길동전" writer ="홍길동" content="조선"
    // 앞에 Long이 있으니까 등록한 게시글 번호(bno)를 반환하는 구조

    BoardDTO readOne(Long bno);
    // 게시글 번호(bno)를 입력하면 게시글 정보(BoardDTO) 반환

    void modify(BoardDTO boardDTO);
    // 수정에 필요한 정보가 필요하니까 BoardDTO 통째로 받음
    // void - 수정 결과를 따로 반환하지 않는다

    void remove(Long bno);
    // 삭제할 게시글 번호만 알면 되니까 bno

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    // PageRequestDTO : 페이지 정보를 담고 있는 DTO ex) "게시글 목록 2페이지를 10개씩 가져와."
    // PageResponseDTO<BoardDTO> - 결과물의 형태
    // 즉, "게시글(BoardDTO) 목록을 페이징 정보와 함께 담아서 반환한다."

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
    // 위의 list에서 댓글이 몇개 달려있는지 개수까지 같이 가져오는 기능
    // 즉, "게시글 정보에 댓글 개수까지 들어있는 DTO들을 페이지 단위로 묶어서 반환한다."

    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);
}

// ★★★ 괄호 안 = 입력값, 메서드 이름 앞 = 출력값
// ex) ReplyDTO read(Long rno) : 댓글 번호를 넣으면 댓글DTO를 돌려준다.
// ex) void remove(Long rno) : 댓글 번호를 넣으면 삭제하고 아무것도 돌려주지 않는다.
// ex) Long register(ReplyDTO replyDTO) : 댓글 정보를 넣으면 등록하고 Long 값을 돌려준다.