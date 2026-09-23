package com.example.jpab01.repository.search;
/* BoardSearch에 적어놓은 검색 기능을 실제로 구현하는 곳 */

/* DB에 직접 SQL을 쓰는 건 아니고, QueryDSL이라는 도구를 이용해서 Java코드로 동적인 검색 쿼리 직접 조립 */
import com.example.jpab01.domain.Board;
import com.example.jpab01.domain.QBoard;
import com.example.jpab01.domain.QReply;
import com.example.jpab01.dto.BoardImageDTO;
import com.example.jpab01.dto.BoardListAllDTO;
import com.example.jpab01.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support
        .QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport
        implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class); // "Board Entity를 대상으로 QueryDSL 작업할 거야." -> 부모 클래스에게 연결 요청
    }

    /* 연습용 기본 검색 */
    @Override
    public Page<Board> search1(Pageable pageable) {
        // "게시글을 검색해서 Page<Board>로 반환하겠다."

        QBoard board = QBoard.board; // QueryDSL 객체 생성
        JPQLQuery<Board> query = from(board);
        // "Board를 대상으로 QueryDSL 검색을 시작할게."
        // Page<Board> : 게시글 목록 + 페이징 정보를 함께 담아주는 객체
        // Pageable : 페이징 정보를 전달 ex) 1페이지 10개씩, 3페이지 20개씩

        BooleanBuilder builder = new BooleanBuilder(); // 밑의 검색 조건을 담아놓은 상자
        builder.or(board.title.contains("1")); // -> 제목에 1이 들어있거나
        builder.or(board.content.contains("1")); // -> 내용에 1이 들어있는 게시글
        query.where(builder); // 아까 만든 조건 상자를 실제 쿼리에 넣음
        query.where(board.bno.gt(0L)); // 게시글 번호(bno)가 0보다 크면, (사실상 전체 게시글)

        getQuerydsl().applyPagination(pageable, query); // "아까 받은 Pageable 정보를 이 쿼리에 적용해."

        List<Board> list = query.fetch(); // 검색 결과
        long count = query.fetchCount(); // 전체 개수 (페이지 번호 계산용)

        return new PageImpl<>(list, pageable, count);
        // QueryDSL에서 가져온 결과를 Spring Data의 Page 형태로 포장
    }

    /* 제목/내용/작성자 조건을 받아서 검색 */
    @Override
    public Page<Board> searchAll(String[] types, // types : 검색할 영역 ex) 제목+내용,
                                 String keyword, // keyword : 검색어
                                 Pageable pageable) {  // pageable : 페이지 정보

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        // "Board를 대상으로 QueryDSL 검색을 시작할게."

        if (types != null && types.length > 0 && keyword != null) {
            // "types가 존재하는가? 검색 타입이 하나라도 있는가? 검색어가 존재하는가?"
            BooleanBuilder builder = new BooleanBuilder(); // 검색 조건을 담아놓는 통
            for (String type : types) { // types = ["t", "c", "w"], types 배열에 들어있는 값을 하나씩 꺼내면서 비교
                switch (type) { // 사용자가 선택하고 입력한대로 types=["t", "w"] keyword="스프링" 이렇게 들어옴
                    case "t" -> builder.or(board.title.contains(keyword)); // "t"이므로 결과적으로 제목에 "스프링" 포함 조건 생성
                    case "c" -> builder.or(board.content.contains(keyword));
                    case "w" -> builder.or(board.writer.contains(keyword));
                    default -> { }
                }
            }
            query.where(builder);
        }
        query.where(board.bno.gt(0L)); // 게시글 번호(bno)가 0보다 크면, (사실상 전체 게시글)

        getQuerydsl().applyPagination(pageable, query); // "아까 받은 Pageable 정보를 이 쿼리에 적용해."

        List<Board> list = query.fetch(); // 검색 결과
        long count = query.fetchCount(); // 전체 개수 (페이지 정보 계산용)

        return new PageImpl<>(list, pageable, count);
        // QueryDSL에서 가져온 결과를 Spring Data의 Page 형태로 포장
    }

    /* 검색 + 댓글 개수까지 가져오기 */
    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                             String keyword,
                                                             Pageable pageable) {

        QBoard board = QBoard.board; // 객체 생성
        QReply reply = QReply.reply;
        JPQLQuery<Board> query = from(board);
        // "Board를 대상으로 QueryDSL 검색을 시작할게."

        query.leftJoin(reply).on(reply.board.eq(board)); // ★ "게시글과 댓글을 연결해라."
        // 댓글이 0인 게시글도 있어서 leftjoin
        // reply.board.eq(board) : Reply가 가지고 있는 게시글과 현재 Board가 같은 경우

        query.groupBy(board); // 게시글 별로 그룹 만듦 -> 댓글을 하나로 묶기

        if( (types != null && types.length > 0) && keyword != null ){
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // 검색 조건을 담아놓는 통
            for(String type: types){
                switch (type){ // types = ["t", "c", "w"], types 배열에 들어있는 값을 하나씩 꺼내면서 비교
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break; // 위랑 같은 의미, 다른 형태
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery =
                query.select(Projections.bean(
                BoardListReplyCountDTO.class,
                board.bno, // DB에서 가져온 bno를 DTO의 bno로 넣음
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount") // "이 계산 결과를 "replyCount"라는 이름으로 취급해."
        )); // "DB에서 조회한 결과를 BoardListReplyCountDTO에 맞춰서 담아줘."

        this.getQuerydsl().applyPagination(pageable,dtoQuery); // "아까 받은 Pageable 정보를 이 쿼리에 적용해."

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch(); // 검색 결과
        long count = dtoQuery.fetchCount(); // 전체 개수 (페이지 정보 계산용)

        return new PageImpl<>(dtoList, pageable, count); // QueryDSL에서 가져온 결과를 Spring Data의 Page 형태로 포장
    }

    /* List 처리 테스트를 위한 메소드 */
    @Override
//    public Page<BoardListReplyCountDTO> searchWithAll(String[] types,
//                                                      String keyword,
//                                                      Pageable pageable) {

    public Page<BoardListAllDTO> searchWithAll(String[] types,
                                               String keyword,
                                               Pageable pageable) {

        QBoard board = QBoard.board; // 각 Entity를 QueryDSL에서 사용할 수 있도록 가져옴
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));
        // ★ 게시글과 댓글을 연결 : leftjoin - 댓글이 없는 게시글도 결과에 포함

        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); //paging

//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        boardList.forEach(board1 -> {
//            System.out.println(board1.getBno());
//            System.out.println(board1.getImageSet());
//            System.out.println("--------------------");
//        });

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();
        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {

            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            //BoardImage를 BoardImageDTO 처리할 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
