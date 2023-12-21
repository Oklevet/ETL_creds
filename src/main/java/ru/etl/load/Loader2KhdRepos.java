package ru.etl.load;

import ru.etl.configuration.DataSource;
import ru.etl.model.Debt;
import ru.etl.model.PrCred;
import ru.etl.model.VidDebt;
import ru.etl.repository.SqlRecieveDBRepository;

import java.util.Collection;
import java.util.HashMap;

public class Loader2KhdRepos implements Loader2Khd {
    @Override
    public void process(Collection<PrCred> creds, Collection<Debt> debts, HashMap<Long, VidDebt> dirDebts,
                        DataSource dataSource) {
        SqlRecieveDBRepository recieveDBRep = new SqlRecieveDBRepository();
        recieveDBRep.insertAllCreds(creds);
        recieveDBRep.insertAllDebts(debts, dirDebts);
    }
}
