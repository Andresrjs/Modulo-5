package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.*;

import java.util.stream.Collectors;
import javax.persistence.OneToMany;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date GameDate;

    @JsonIgnore
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @JsonIgnore
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    Set<Score> scores = new HashSet<>();


    public Game() {
        this.GameDate = new Date();
    }

    public Game(Date GameDate) {
        this.GameDate = GameDate;
    }
    @JsonIgnore
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    @JsonIgnore
    public Set<Score> getScores() { return scores; }

    public long getId() {
        return id;
    }

    public Date getGameDate() {
        return GameDate;
    }

    public void setGameDate(Date gameDate) {
        this.GameDate = gameDate;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Map<String, Object> makeGameDTO () {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getGameDate().getTime());
        dto.put("gamePlayers", getAllGamePlayers((Set<GamePlayer>) this.getGamePlayers()));
        dto.put("score",this.getScores()
                .stream()
                .map(score -> score.makeScoreDTO())
                .collect(Collectors.toList()));
        return dto;
    }

    public List<Map<String, Object>> getAllGamePlayers(Set<GamePlayer> gamePlayers) {
        return gamePlayers
                .stream()
                .map(GamePlayer -> GamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList());
    }

    }



