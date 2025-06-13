package com.example.gestionmatch.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Reponse<T> {

    private Header header;

    private T content;

    public Reponse(Header header) {

        super();

        this.header = header;

    }


}
