package dk.dtu.compute.se.pisd.roborally.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.Exceptions.NotFoundException;
import dk.dtu.compute.se.pisd.roborally.fileaccess.FileHandler;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.GameHandler;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.AddPlayerRequest;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.AddPlayerResponse;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.GameResponse;
import dk.dtu.compute.se.pisd.roborally.model.gameRequests.NewGameRequest;
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

    private FileHandler fileHandler;
    private GameHandler gameHandler;

    /**
     * Constructor for controller
     */
    public RoboRallyController(){
        fileHandler = new FileHandler();
        gameHandler = new GameHandler();

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
        gameHandler.addToList(gameController, true);
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

        GameController gameController= fileHandler.getGame(id, true);
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
        GameController gameController= fileHandler.getGame(id, true);
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

        GameController gameController= fileHandler.getGame(id, true);
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
    public ResponseEntity<List<String>> getOngoingGames(){
        return ResponseEntity.ok(gameHandler.getGames(true));
    }

    /**
     * Stops an ongoingGame
     * @param id the game
     * @return responseEntity
     */
    @DeleteMapping("/stopgame/{id}")
    public ResponseEntity<String> stopGame(@PathVariable String id){
        try {
            if (gameHandler.stopGame(id)) return ResponseEntity.ok("success");
        }
        catch (NotFoundException e ){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<List<String>> getSavedGames(){
        return ResponseEntity.ok(gameHandler.getGames(false));
    }

}