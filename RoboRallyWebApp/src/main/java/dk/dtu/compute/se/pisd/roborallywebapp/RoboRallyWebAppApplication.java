package dk.dtu.compute.se.pisd.roborallywebapp;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoboRallyWebAppApplication {

    public static void main(String[] args)  {
        Application.launch(RoboRally.class, args);

    }



}
