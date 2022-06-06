package dk.dtu.compute.se.pisd.roborallywebapp.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/game")
public class RoboRallyController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RoboRally getRally(){

        try {
            RoboRally rally = new RoboRally();
            rally.init();
            return rally;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
