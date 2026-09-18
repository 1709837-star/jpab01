package com.example.jpab01.domain;
/* 여러 Entity가 공통으로 가지고 있어야 하는 날짜 정보를 한 곳에서 관리하는 것 */
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
// "이 클래스 자케를 테이블로 만들지는 말고, 자식 entity(현재:Board)에게 필드를 물려줘"
@EntityListeners(AuditingEntityListener.class)
// JPA Entity의 생성/수정 같은 이벤트를 감시해서 날짜를 자동으로 넣어주는 역할
@Getter
public abstract class BaseEntity {

    @CreatedDate // 등록 날짜 : "Entity가 처음 생성될 때 날짜/시간을 자동으로 기록해줘."
    @Column(name = "regdate", updatable = false) // "한 번 등록된 regDate는 수정하지마."
    private LocalDateTime regDate;

    @LastModifiedDate // 수정 날짜 : "Entity가 수정될 때 마지막 수정 시간을 자동으로 기록해줘."
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
