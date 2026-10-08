package com.example.jpab01.service;
/* 게시판에서 필요한 기능의 목록/규칙 */
import com.example.jpab01.domain.Board;
import com.example.jpab01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

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
    // ★ 위 함수들의 종결판 : 게시글의 이미지와 댓글의 숫자까지 처리

    /* DTO -> Entity (게시물 등록) */
    default Board dtoToEntity(BoardDTO boardDTO){
    // 게시글 등록하는 핵심 비즈니스 로직이라기보다는 BoardDTO를 Board Entity로 변환하는 과정 (이전에는 mapper 사용)
    // ModelMapper가 단순 필드 변환에는 편하지만, Board와 BoardImage 관계설정 같은 복잡한 변환은 직접 처리하는게 명확해서
    // default를 붙이면 interface 안에서도 메서드의 실제 내용 작성 가능
    // DTO <-> Entity 변환 코드를 공통 메서드로 만들어 재사용하려는 의도

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null){ // DTO에 첨부파일 목록이 있다면? BoardImage도 만들어야 함
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_"); // UUID와 실제 파일명을 분리하는 코드 -> arr[0]=랜덤의 긴 숫자, arr[1]=aaa.jpg
                board.addImage(arr[0], arr[1]); // Board의 addImage()
            });
        }
        return board; // 마지막엔 첨부파일 정보까지 들어있음
    }

    /* Entity -> DTO 역변환 메소드 (게시물 조회) */
    default BoardDTO entityToDTO(Board board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSet().stream().sorted().map(boardImage ->
                                boardImage.getUuid()+"_"+boardImage.getFileName())
                        .collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}

// ★★★ 괄호 안 = 입력값, 메서드 이름 앞 = 출력값
// ex) ReplyDTO read(Long rno) : 댓글 번호를 넣으면 댓글DTO를 돌려준다.
// ex) void remove(Long rno) : 댓글 번호를 넣으면 삭제하고 아무것도 돌려주지 않는다.
// ex) Long register(ReplyDTO replyDTO) : 댓글 정보를 넣으면 등록하고 Long 값을 돌려준다.