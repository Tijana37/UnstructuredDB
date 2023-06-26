package mk.ukim.finki.NBNP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application-prod.properties")
public class NBNPProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NBNPProjectApplication.class, args);
    }



}
