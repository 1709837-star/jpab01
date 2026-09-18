package com.example.jpab01.dto;
/* 페이지를 요청할 때 필요한 정보를 담는 DTO */
/* "이렇게 검색해주세요." */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type; // 검색의 종류 t,c, w, tc,tw, twc

    private String keyword;

    /* getTypes() */
    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split(""); // "tc"로 들어오면 ["t","c"]로 만들어 줌
    }

    /* getPageable : JPA에게 "몇 페이지를, 몇 개를, 어떤 순서로 가져올지" 알려줌 */
    public Pageable getPageable(String...props) {
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
        // Spring 내부에서는 0페이지 부터 시작, descending() 괄호 안을 기준으로 내림차순 (controller에서)
    }

    /* getLink() : 페이지 이동 링크를 만들기 위한 문자열 */
    private String link;

    public String getLink() {

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);


            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            link = builder.toString();
        }

        return link;
    }



}
