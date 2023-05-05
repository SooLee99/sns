package com.example.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// DataSourceAutoConfiguration
// => Spring Boot에서 데이터베이스를 사용하려면 일반적으로 데이터베이스 드라이버 의존성과 데이터베이스 연결 정보를 포함하는 구성 파일이 필요합니다.
// 	  그러나 DataSourceAutoConfiguration은 이러한 작업을 자동으로 수행하므로, 데이터베이스 연결 설정과 관련된 코드를 작성하지 않아도 됩니다.
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnsApplication.class, args);
	}

}
