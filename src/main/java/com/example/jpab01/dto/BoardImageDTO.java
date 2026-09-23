package com.example.jpab01.dto;
/* BoardImage의 데이터를 화면에 전달하기 위한 DTO */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO {

    private String uuid;

    private String fileName;

    private int ord;
}