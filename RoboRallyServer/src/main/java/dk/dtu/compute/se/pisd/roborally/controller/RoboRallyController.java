package dk.dtu.compute.se.pisd.roborally.controller;

import java.io.File;
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

/**
 * @Author Tobias Borgstrøm s184810
 */
@RestController
public class RoboRallyController
{

    ArrayList<OngoingGameResponse> ongoingGameResponses;
    private FileHandler fileHandler;

    /**
     * Constructor for controller
     */
    public RoboRallyController(){
        ongoingGameResponses = new ArrayList<>();
    }

    /**
     * @param boardInfo is NewGameRequest
     * @return Json of gameController that has an unique id
     */
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
        String gameJson = fileHandler.startGame(gameController);
        return ResponseEntity.ok().body(gameJson);

    }

    /**
     * @param id of the game that are wanted
     * @return the game controller loaded from json file
     */
    //could be modified a bit and used to load game
    @GetMapping("/getgame/{id}")
    public ResponseEntity<String> getGame(@PathVariable String id){
        Gson gson = new Gson();

        GameController gameController= fileHandler.getOngoingGame(id);
        String responseJson = gson.toJson(gameController, GameController.class);
        return ResponseEntity.ok().body(responseJson);
    }

    /**
     * add player to a game
     * @param id game id
     * @param request json of AddPlayerRequest
     * @return Json of the Player
     */
    @PostMapping("/addplayer/{id}")
    public ResponseEntity<String> addPlayer(@PathVariable String id, @RequestBody String request){
        Gson gson = new Gson();

        AddPlayerRequest playerRequest = gson.fromJson(request, AddPlayerRequest.class);
        GameController gameController= fileHandler.getOngoingGame(id);
        if(gameController.board.getPlayersNumber() < gameController.getNumberOfPlayers()) {
            AddPlayerResponse response = gameController.board.addPlayer(playerRequest.name, gameController);
            System.out.println("players in game: " + gameController.board.getPlayersNumber());

            if (fileHandler.gameUpdated(gameController)) {
                System.out.println("Game updated");
            }

            String responseJson = gson.toJson(response, AddPlayerResponse.class);
            return ResponseEntity.ok().body(responseJson);
        }
        else {
            return ResponseEntity.badRequest().body("Players are full on this server");
        }
    }

    /**
     * @return Boards on the server
     */
    @GetMapping("/boards")
    public ResponseEntity<List<String>> getBoards(){
        return ResponseEntity.ok().body(LoadBoard.getBoardNames());
    }

    /**
     * Checks wheter all players have joined the game
     * @param id game id
     * @return string boolean true if ready
     */
    @GetMapping("/gameready/{id}")
    public ResponseEntity<String> gameReady(@PathVariable String id){

        GameController gameController= fileHandler.getOngoingGame(id);

        if(gameController.getNumberOfPlayers() == gameController.board.getPlayersNumber()){
            System.out.println("game ready");
            return ResponseEntity.ok().body("true");
        }
        else {
            System.out.println("game not ready");
            return ResponseEntity.ok().body("false");
        }

    }

    /**
     * Gets ongoing joinable games
     * @return ongoing joinable games
     */
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
            return  ResponseEntity.badRequest().body("There's no joinable games");
        }
        return ResponseEntity.ok(gson.toJson(gameResponses, ArrayList.class));
    }

    /**
     * Stops an ongoingGame
     * @param id the game
     * @return responseEntity
     */
    @DeleteMapping("/stopgame/{id}")
    public ResponseEntity<String> stopGame(@PathVariable String id){
        for (OngoingGameResponse gameResponse: ongoingGameResponses){
            boolean bool = gameResponse.getId().equals(id);
            if(bool){
                ongoingGameResponses.remove(gameResponse);
                if(fileHandler.stopGame(id)) return new ResponseEntity<>(id, HttpStatus.OK);
                else return new ResponseEntity<>(id, HttpStatus.BAD_GATEWAY);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Saves a game on server
     * @param id the game
     * @return ResponseEntity.ok() with body("success") or ResponseEntity.internalServerError() with body("game not saved");
     */
    @GetMapping("/savegame/{id}")
    public ResponseEntity<String> saveGame(@PathVariable String id){
        System.out.println("saving game");
        if(fileHandler.saveGame(id)) return ResponseEntity.ok().body("success");
        else {
            return ResponseEntity.internalServerError().body("game not saved");
        }
    }

    @GetMapping("/savedgames")
    public ResponseEntity<String> getSavedGames(){
        Gson gson = new Gson();
        List<String> s_arr = fileHandler.getSavedGames();
        if (s_arr.isEmpty()){
            return  ResponseEntity.badRequest().body("There's no joinable games");
        }
        return ResponseEntity.ok(gson.toJson(s_arr, ArrayList.class));
    }

}