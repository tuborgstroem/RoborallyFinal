package dk.dtu.compute.se.pisd.roborally.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.controller.gameRequests.*;
import dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.loadBoard;

@RestController
public class RoboRallyController
{

    ArrayList<OngoingGameResponse> ongoingGameResponses;

    public RoboRallyController(){
        ongoingGameResponses = new ArrayList<>();
    }

    @PostMapping("/newgame")
    public ResponseEntity<String > newGame(@NotNull @RequestBody NewGameRequest boardInfo) {
        System.out.println(boardInfo.boardname);

        GameController gameController = new GameController(Objects.requireNonNull(loadBoard((boardInfo.boardname))),
                UUID.randomUUID().toString(), boardInfo.playerNumber);
       AddPlayerRequest hostRequest = new AddPlayerRequest();
        hostRequest.name = boardInfo.hostName;
        AddPlayerResponse player = gameController.board.addPlayer(boardInfo.hostName, gameController);
        gameController.board.setCurrentPlayer(player.getPlayer());
        ongoingGameResponses.add(new OngoingGameResponse(boardInfo.boardname, boardInfo.hostName, gameController.board.getPlayersNumber(), boardInfo.playerNumber, gameController.gameId));
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

    @GetMapping("/ongoinggames")
    public ResponseEntity<String> getOngoingGames(){
        Gson gson = new Gson();
        ArrayList<OngoingGameResponse> gameResponses = new ArrayList<>();
        for (OngoingGameResponse gameResponse :ongoingGameResponses ){
            if(!gameResponse.isFull()){
                gameResponses.add(gameResponse);
            }
        }
        if (gameResponses.isEmpty()){
            ResponseEntity res = ResponseEntity.badRequest().body("There's no joinable games");

            return null;
        }
        return ResponseEntity.ok(gson.toJson(gameResponses, ArrayList.class));
    }

    @DeleteMapping("/stopgame/{id}")
    public ResponseEntity<String> stopGame(@PathVariable String id){
        for (OngoingGameResponse gameResponse: ongoingGameResponses){
            boolean bool = gameResponse.getId().equals(id);
            if(bool){
                ongoingGameResponses.remove(gameResponse);
                if(FileHandler.stopGame(id)) return new ResponseEntity<>(id, HttpStatus.OK);
                else return new ResponseEntity<>(id, HttpStatus.BAD_GATEWAY);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/savegame/{id}")
    public ResponseEntity<String> saveGame(@PathVariable String id){
        if(FileHandler.saveGame(id)) return ResponseEntity.ok().body("success");
        else {
            return ResponseEntity.internalServerError().body("game not saved");
        }
    }

}