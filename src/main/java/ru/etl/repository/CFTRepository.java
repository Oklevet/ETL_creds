package ru.etl.repository;

import ru.etl.model.PrCred;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public interface CFTRepository {

    Collection<Long> getIDAllCredInSet();

    PrCred getCred(long id);

    List<PrCred> getCreds(List<Long> listIds);
}
