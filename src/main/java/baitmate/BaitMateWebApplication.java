package baitmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BaitMateWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(BaitMateWebApplication.class, args);
  }
}
