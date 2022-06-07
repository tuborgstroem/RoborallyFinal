package dk.dtu.compute.se.pisd.roborally.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jdi.request.ModificationWatchpointRequest;
import dk.dtu.compute.se.pisd.demo.Product;
import dk.dtu.compute.se.pisd.roborally.fileaccess.FieldActionAdapter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.fieldActions.FieldAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.loadBoard;

@RestController
public class RoboRallyController
{

//    @GetMapping(value = "/products")
//    public ResponseEntity<List<Product>> getProduct()
//    {
//        List<Product> products = productService.findAll();
//        return ResponseEntity.ok().body(products);
//    }

    @PostMapping("/newgame")
    public ResponseEntity<String > newGame(@RequestBody String boardName) {

        GameController gameController = new GameController(Objects.requireNonNull(loadBoard(boardName)));

        return ResponseEntity.ok().body(toJson(gameController));

    }

    @GetMapping("/boards")
    public ResponseEntity<List<String>> getBoards(){
        return ResponseEntity.ok().body(LoadBoard.getBoardNames());
    }

    private String toJson(GameController gameController)  {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionAdapter());
        Gson gson = gsonBuilder.create();
        String s =  gson.toJson(gameController);
        return s;

    }

//    @GetMapping("/products/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable int id) {
//        Product p = productService.getProductById(id);
//        return ResponseEntity.ok().body(p);
//    }

}