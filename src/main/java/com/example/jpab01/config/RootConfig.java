package com.example.jpab01.config;
/* Spring 설정 클래스 */
/* Modelmapper : DTO <-> Entity 변환할 때 쓰는 라이브러리 */

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
                // ModelMapper가 DTO와 Entity 필드 이름을 비교할 때, 비슷한 구조라면 매칭할 수 있도록 느슨하게

        return modelMapper;
        // -> @Bean으로 Spring Bean 등록, 어디서든 modelMapper 가져다 쓸 수 있음
    }
}
