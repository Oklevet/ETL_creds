package ru.etl.load;

import ru.etl.configuration.DataSource;
import ru.etl.model.Debt;
import ru.etl.model.PrCred;
import ru.etl.model.VidDebt;

import java.util.Collection;
import java.util.HashMap;

public interface Loader2Khd {
    void process(Collection<PrCred> creds, Collection<Debt> debts, HashMap<Long, VidDebt> dirDebts,
                 DataSource dataSource);
}
