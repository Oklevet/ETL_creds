package ru.etl.repository;

import ru.etl.model.Debt;
import ru.etl.model.PrCred;
import ru.etl.model.VidDebt;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface RecieveDBRepository {

    void insertAllCreds(Collection<PrCred> creds);

    void insertAllDebts(Collection<Debt> debts, HashMap<Long, VidDebt> dirDebts);
}