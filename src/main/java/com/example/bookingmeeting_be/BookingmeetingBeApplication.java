package com.example.bookingmeeting_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class BookingmeetingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingmeetingBeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowser() {
        String url = "http://localhost:8080/users/home"; 
        
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        
        try {
            if (os.contains("mac")) {
                rt.exec(new String[]{"open", url});
            } else if (os.contains("win")) {
                rt.exec(new String[]{"cmd", "/c", "start", url});
            } else if (os.contains("nix") || os.contains("nux")) {
                rt.exec(new String[]{"xdg-open", url});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}