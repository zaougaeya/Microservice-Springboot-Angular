package com.example.gestionmatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "statistiques_equipe")
public class StatistiqueEquipes {

    @Id
    private String id;


    @Transient
    private Equipe equipe;  // L'équipe associée aux statistiques
    private String matchId;
    private String equipeId;
    private int fautes;
    private int cartonsJaunes;
    private int cartonsRouges;
    private int but;

    private StatistiqueEquipes(Builder builder) {
        this.matchId = builder.matchId;
        this.equipeId = builder.equipeId;
        this.cartonsJaunes = builder.cartonsJaunes;
        this.cartonsRouges = builder.cartonsRouges;
        this.but = builder.but;
    }

    public static class Builder {
        private String matchId;
        private String equipeId;
        private int cartonsJaunes;
        private int cartonsRouges;
        private int but;

        public Builder setMatchId(String matchId) {
            this.matchId = matchId;
            return this;
        }

        public Builder setCartonsJaunes(int cartonsJaunes) {
            this.cartonsJaunes = cartonsJaunes;
            return this;
        }

        public Builder setCartonsRouges(int cartonsRouges) {
            this.cartonsRouges = cartonsRouges;
            return this;
        }

        public Builder setBut(int but) {
            this.but = but;
            return this;
        }

        public Builder setEquipeId(String equipeId) {
            this.equipeId = equipeId;
            return this;
        }

        public StatistiqueEquipes build() {
            return new StatistiqueEquipes(this);
        }
    }
}
