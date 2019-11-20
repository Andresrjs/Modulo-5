package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private int turn;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_ID")
    private GamePlayer gamePlayer;


    @ElementCollection
    @Column (name = "location")
    private Set<String> SalvoLocation;

    //Constructor

    public Salvo(){

    }

    public Salvo(GamePlayer gamePlayer, Set<String> salvoLocation,  int turn) {
        this.gamePlayer = gamePlayer;
        this.SalvoLocation = salvoLocation;
        this.turn = turn;
    }

    public long getId() {
        return id;
    }


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }


    public Set<String> getSalvoLocation() {
        return SalvoLocation;
    }

    public void setSalvoLocation(Set<String> salvoLocation) {
        this.SalvoLocation = salvoLocation;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }


    public Object makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("location", this.getSalvoLocation());
        dto.put("turn", this.getTurn());
        dto.put("player", gamePlayer.getPlayer().makePlayerDTO());
        return dto;
    }


}