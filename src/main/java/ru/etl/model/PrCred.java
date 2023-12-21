package ru.etl.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class PrCred {

    private long id;

    @EqualsAndHashCode.Include
    private String numDog;

    @EqualsAndHashCode.Include
    private String val;

    private long collectionFO;

    private long collectionPO;

    private long collectionDebts;
}