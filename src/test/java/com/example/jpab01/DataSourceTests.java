package com.example.jpab01;
/* Spring이 DataSource를 제대로 만들어 놨고, DB에 연결할 수 있는지 확인하는 테스트  */
/* DataSource : DB에 연결할 때 필요한 연결 정보를 가지고 있는 객체 */
/* ㄴ application.properties의 DB정보를 통해 객체를 만들어 줌 */

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Log4j2
public class DataSourceTests {

    @Autowired
    private DataSource dataSource;
    // "Spring이 만들어 놓은 DataSource 객체 하나 주세요."

    @Test
    public void testConnection() throws SQLException {
        @Cleanup
        Connection con = dataSource.getConnection();
        // DataSource를 이용해서 실제 DB연결 하나 얻어오기 (con : 연결 통로)

        log.info(con);
        Assertions.assertNotNull(con);
    }
}
