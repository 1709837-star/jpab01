package com.example.jpab01.repository.search;
/* BoardSearch에 적어놓은 검색 기능을 실제로 구현하는 곳 */
import com.example.jpab01.domain.Board;
import com.example.jpab01.domain.QBoard;
import com.example.jpab01.domain.QReply;
import com.example.jpab01.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support
        .QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport
        implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        // "Board를 대상으로 QueryDSL 검색을 시작할게."
        // Page<Board> : 게시글 목록 + 페이징 정보를 함께 담아주는 객체
        // Pageable : 페이징 정보를 전달 ex) 1페이지 10개씩, 3페이지 20개씩

        BooleanBuilder builder = new BooleanBuilder(); // 밑의 검색 조건을 담아놓은 상자
        builder.or(board.title.contains("1")); // -> title에 "1" 포함
        builder.or(board.content.contains("1")); // -> content에 "1" 포함
        query.where(builder);
        query.where(board.bno.gt(0L));

        getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // 검색 결과
        long count = query.fetchCount(); // 전체 개수

        return new PageImpl<>(list, pageable, count);
        // QueryDSL에서 가져온 결과를 Spring Data의 Page 형태로 포장
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword,
                                 Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        // "Board를 대상으로 QueryDSL 검색을 시작할게."

        if (types != null && types.length > 0 && keyword != null) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t" -> builder.or(board.title.contains(keyword));
                    case "c" -> builder.or(board.content.contains(keyword));
                    case "w" -> builder.or(board.writer.contains(keyword));
                    default -> { }
                }
            }
            query.where(builder);
        }
        query.where(board.bno.gt(0L));

        getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // 검색 결과
        long count = query.fetchCount(); // 전체 개수

        return new PageImpl<>(list, pageable, count);
        // QueryDSL에서 가져온 결과를 Spring Data의 Page 형태로 포장
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> query = from(board);

        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        if( (types != null && types.length > 0) && keyword != null ){

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
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

        //bno > 0
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(
                BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")
        ));

        this.getQuerydsl().applyPagination(pageable,dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
