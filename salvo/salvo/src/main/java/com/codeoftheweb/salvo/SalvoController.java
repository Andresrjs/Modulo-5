package com.codeoftheweb.salvo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.Collectors.toList;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gameplayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", player.makePlayerDTO());
        }
        dto.put("games", gameRepository.findAll()
                .stream()
                .map(Game -> makeGameDTO(Game))
                .collect(Collectors.toList()));

        return dto;
    }


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame (Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if (isGuest(authentication)) {
            return new ResponseEntity<>("NO ESTA AUTORIZADO", HttpStatus.UNAUTHORIZED);
        }

       Player player = playerRepository.findByUserName(authentication.getName());

        Game game = gameRepository.save(new Game());

        GamePlayer gamePlayer = gameplayerRepository.save(new GamePlayer(game, player));


        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()),HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Object> getGameViewByGamePlayerID(@PathVariable Long nn, Authentication authentication) {
        GamePlayer gameplayer = gameplayerRepository.findById(nn).get();

        if (isGuest(authentication)) {
            return new ResponseEntity<>("NO ESTA AUTORIZADO", HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = gameplayerRepository.findById(nn).orElse(null);

        if (player == null){
            return new ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == null){
            return new ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getId() != player.getId()){
            return new ResponseEntity<>(makeMap("error","Paso algo"),HttpStatus.CONFLICT);
        }



        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();
        hits.put("self", new ArrayList<>());
        hits.put("opponent", new ArrayList<>());

        dto.put("id", gameplayer.getGame().getId());
        dto.put("created", gameplayer.getGame().getGameDate());
        dto.put("gameState", getState(gamePlayer));
        dto.put("gamePlayers", gameplayer.getGame().getGamePlayers().
                stream().
                map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).
                collect(Collectors.toList()));

        dto.put("ships", gameplayer.getship().
                stream().
                map(ship -> ship.makeShipDTO()).
                collect(Collectors.toList()));

        dto.put("salvoes", gameplayer.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes().
                        stream().
                        map(salvo -> salvo.makeSalvoDTO()))
                .collect(Collectors.toList()));

        dto.put("hits", hits);

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @RequestMapping(path = "/game/{gameID}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame (@PathVariable Long gameID, Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No estas loggeado"),HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());

        Game gameToJoin = gameRepository.getOne(gameID);

        if (gameRepository.getOne(gameID) == null) {
            return new ResponseEntity<>(makeMap("error", "No such game"),HttpStatus.FORBIDDEN);
        }

        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "No such game"),HttpStatus.FORBIDDEN);
        }

        long gamePlayersCount = gameToJoin.getGamePlayers().size();

        if (gamePlayersCount == 1){
            GamePlayer gamePlayer = gameplayerRepository.save(new GamePlayer(gameToJoin, player));
            return new ResponseEntity<>(makeMap("gpid",gamePlayer.getId()),HttpStatus.CREATED);
        }

        else {
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(path = "/games/players/{gpid}/ships",  method = RequestMethod.POST)
    public ResponseEntity<Map>  addShip(@PathVariable long gpid, @RequestBody Set<Ship> ships, Authentication authentication){

        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player  player  = playerRepository.findByUserName(authentication.getName());
        GamePlayer  gamePlayer  = gameplayerRepository.getOne(gpid);

        if(player ==  null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer == null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getPlayer().getId() !=  player.getId()){
            return new ResponseEntity<>(makeMap("error","Los players no coinciden"), HttpStatus.FORBIDDEN);
        }

        if(!gamePlayer.getship().isEmpty()){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado ya tengo ships"), HttpStatus.UNAUTHORIZED);
        }

        ships.forEach(ship -> {
            ship.setGamePlayer(gamePlayer);
            shipRepository.save(ship);
        });

        return new ResponseEntity<>(makeMap("OK","Ship created"), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/games/players/{gpid}/salvoes",  method = RequestMethod.POST)
    public ResponseEntity<Map>  addSalvo(@PathVariable long gpid, @RequestBody Salvo salvo, Authentication authentication){

        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player  player  = playerRepository.findByUserName(authentication.getName());
        GamePlayer  gamePlayer  = gameplayerRepository.getOne(gpid);

        if(player ==  null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer == null){
            return new ResponseEntity<>(makeMap("error","NO esta autorizado"), HttpStatus.UNAUTHORIZED);
        }

        if(gamePlayer.getPlayer().getId() !=  player.getId()){
            return new ResponseEntity<>(makeMap("error","Los players no coinciden"), HttpStatus.FORBIDDEN);
        }

        if(gamePlayer.getSalvoes().isEmpty()){
            salvo.setGamePlayer(gamePlayer);
            salvo.setTurn(1);
            salvoRepository.save(salvo);
        }

        if (gamePlayer.getSalvoes().size() <= getopponent(gamePlayer).getSalvoes().size())
        {
            salvo.setGamePlayer(gamePlayer);
           salvo.setTurn(salvo.getTurn() +1);
            salvoRepository.save(salvo);
        }

        return new ResponseEntity<>(makeMap("OK","Ship created"), HttpStatus.CREATED);
    }

    @RequestMapping("/leaderboard")
    public Map<String, Object> getLeadeBoard() {
        Map<String, Object> dto = new HashMap();
        dto.put("leaderboard", playerRepository
                .findAll() //games
                .stream()
                .map(player -> playerStatisticsDTO(player))
                .collect(toList()));
        return dto;
    }

    public Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getGameDate().getTime());
        dto.put("gamePlayers", getAllGamePlayers(game.getGamePlayers()));
        dto.put("scores", game.scores.stream().map(score -> score.makeScoreDTO()).collect(Collectors.toList()));
        return dto;
    }

    public List<Map<String, Object>> getAllGamePlayers(Set<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(GamePlayer -> makeGamePlayerDTO(GamePlayer))
                .collect(Collectors.toList());
    }

    public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", gamePlayer.getPlayer().makePlayerDTO());
        return dto;
    }

    public Map<String, Object> playerStatisticsDTO(Player player) {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        double total = player.getScores().stream().mapToDouble(Score::getScore).sum();
        double won = player.getScores().stream().filter(score -> score.getScore() == 3).count();
        double lost = player.getScores().stream().filter(score -> score.getScore() == 0).count();
        double tied = player.getScores().stream().filter(score -> score.getScore() == 1).count();
        dto.put("score", total);
        dto.put("won", won);
        dto.put("lost", lost);
        dto.put("tied", tied);
        return dto;
    }


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public GamePlayer getopponent (GamePlayer gamePlayer){
            return gamePlayer.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1.getId()!= gamePlayer.getId()).findFirst().orElse(new GamePlayer());
    }

    public String   getState(GamePlayer gamePlayer){
        if(!gamePlayer.getship().isEmpty()){
            return "WAIT";
        }

        return "PLACESHIPS";
    }
}

