package br.com.ermig.patrimonio;

import org.junit.jupiter.api.Test;

class PatrimonioApiApplicationTests {

	@Test
	void contextLoads() {
		PatrimonioApiApplication.main(new String[] {
			"--spring.profiles.active=test",
			"--spring.main.web-environment=false",
			"--spring.cloud.discovery.enabled=false",
			"--logging.level.root=OFF",
			"--logging.level.org.hibernate.type=OFF"
        });
	}

}
