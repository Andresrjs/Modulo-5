package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String userName;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private String password;

    public float Wins;
    public float Losses;
    public float Drows;
    public float totalScore;

    public Player() {
    }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Player(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public float getWins(Set<Score> puntajes) {
        return puntajes.stream().filter(puntaje -> puntaje.getScore() ==1).count();
    }

    public float getLosses(Set<Score> puntajes) {
        return puntajes.stream().filter(puntaje -> puntaje.getScore() ==0).count();
    }

    public float getDrows(Set<Score> puntajes) {
        return puntajes.stream().filter(puntaje -> puntaje.getScore() ==0.5).count();
    }

    public float getTotalScore() {
        float ganadas = getWins(this.getScores())*1;
        float empates = getDrows(this.getScores())*(float) 0.5;
        float perdidas = getLosses(this.getScores())*0;

        return ganadas + empates + perdidas;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());
        return dto;
    }

    public Map<String, Object> makeLeaderBoardDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",this.getId());
        dto.put("userName", this.getUserName());
        dto.put("score", getScoreList());
        return dto;
    }

    private Map<String, Object> getScoreList (){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("juegosGanados",this.getWins(this.getScores()));
        dto.put("juegosPerdidos",this.getLosses(this.getScores()));
        dto.put("juegosEmpatados",this.getDrows(this.getScores()));
        dto.put("puntajeTotal", getTotalScore());
        return dto;
    }



}