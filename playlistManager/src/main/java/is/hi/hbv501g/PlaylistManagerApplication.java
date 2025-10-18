package is.hi.hbv501g;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// this is team member Valentin

@SpringBootApplication(scanBasePackages = "is.hi.hbv501g")
@EnableJpaRepositories(basePackages = "is.hi.hbv501g.repository")
@EntityScan(basePackages = "is.hi.hbv501g.domain")
public class PlaylistManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaylistManagerApplication.class, args);
    }
}
