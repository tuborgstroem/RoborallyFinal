package dk.dtu.compute.se.pisd.roborally.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.controller.gameRequests.AddPlayerRequest;
import dk.dtu.compute.se.pisd.roborally.controller.gameRequests.AddPlayerResponse;
import dk.dtu.compute.se.pisd.roborally.controller.gameRequests.NewGameRequest;
import dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import static com.google.common.io.Resources.getResource;
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
    public ResponseEntity<String > newGame(@NotNull @RequestBody NewGameRequest boardInfo) {
        System.out.println(boardInfo.boardname);

        GameController gameController = new GameController(Objects.requireNonNull(loadBoard((boardInfo.boardname))),
                UUID.randomUUID().toString(), boardInfo.playerNumber);
        String gameJson = FileHandler.startGame(gameController);
        return ResponseEntity.ok().body(gameJson);

    }

    @GetMapping("/getgame/{id}")
    public ResponseEntity<String> getGame(@PathVariable String id){
        Gson gson = new Gson();

        GameController gameController= FileHandler.getOngoingGame(id);
        String responseJson = gson.toJson(gameController, GameController.class);
        return ResponseEntity.ok().body(responseJson);
    }

    @PostMapping("/addplayer/{id}")
    public ResponseEntity<String> addPlayer(@PathVariable String id, @RequestBody String request){
        Gson gson = new Gson();

        AddPlayerRequest playerRequest = gson.fromJson(request, AddPlayerRequest.class);
        GameController gameController= FileHandler.getOngoingGame(id);
        if(gameController.board.getPlayersNumber() < gameController.getNumberOfPlayers()) {
            AddPlayerResponse response = gameController.board.addPlayer(playerRequest.name, gameController);
            if(gameController.board.getPlayersNumber() == 1){
                gameController.board.setCurrentPlayer(response.getPlayer());
                System.out.println(response.getPlayer().getName() + "starts");
            }
            System.out.println("players in game: " + gameController.board.getPlayersNumber());

            if (FileHandler.gameUpdated(gameController)) {
                System.out.println("Game updated");
            }

            String responseJson = gson.toJson(response, AddPlayerResponse.class);
            return ResponseEntity.ok().body(responseJson);
        }
        else {
            return ResponseEntity.badRequest().body("Players are full on this server");
        }
    }
    @GetMapping("/boards")
    public ResponseEntity<List<String>> getBoards(){
        return ResponseEntity.ok().body(LoadBoard.getBoardNames());
    }

    @GetMapping("/gameready/{id}")
    public ResponseEntity<String> gameReady(@PathVariable String id){

        GameController gameController= FileHandler.getOngoingGame(id);

        if(gameController.getNumberOfPlayers() == gameController.board.getPlayersNumber()){
            System.out.println("game ready");
            return ResponseEntity.ok().body("true");
        }
        else {
            System.out.println("game not ready");
            return ResponseEntity.ok().body("false");
        }

    }





//    @GetMapping("/products/{id}")
//    public ResponseEntity<Product> getProductById(@PathVariable int id) {
//        Product p = productService.getProductById(id);
//        return ResponseEntity.ok().body(p);
//    }

}