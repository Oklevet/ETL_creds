package ru.etl.repository;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.etl.configuration.Connect2DB;
import ru.etl.configuration.DataSource;
import ru.etl.model.Debt;
import ru.etl.model.PrCred;
import ru.etl.model.VidDebt;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;

@RequiredArgsConstructor
@NoArgsConstructor
public class SqlRecieveDBRepository implements Connect2DB, RecieveDBRepository {

    @NonNull
    private DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Connection initConnection(DataSource dataSource) {
        Connection connection = null;
        try {
            Class.forName(dataSource.getDriver());
            connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        } catch (Exception e) {
            LOG.error("При подключении к БД ЦФТ произошла ошибка: " + e.fillInStackTrace());
        }
        return connection;
    }

    @Override
    public void closeCon(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            LOG.error("При закрытии подключения к БД ЦФТ произошла ошибка: " + e.fillInStackTrace());
        }
    }

    @Override
    public void insertAllCreds(Collection<PrCred> creds) {
        Connection connection = initConnection(dataSource);
        int countBatch = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("""
                      INSERT INTO PR_CRED (ID, NUM_DOG, DEBTS) VALUES (?, ?, ?)""");

            for (PrCred cred : creds) {
                statement.setLong(1, cred.getId());
                statement.setString(2, cred.getNumDog());
                statement.setLong(3, cred.getCollectionDebts());

                statement.addBatch();
                countBatch++;

                if (countBatch % 1000 == 0 || countBatch == creds.size()) {
                    statement.executeBatch();
                }
            }
        } catch (Exception e) {
            LOG.error("При вставке кредитов произошла ошибка: " + e.fillInStackTrace());
        } finally {
            closeCon(connection);
        }
    }

    @Override
    public void insertAllDebts(Collection<Debt> debts, HashMap<Long, VidDebt> dirDebts) {
        Connection connection = initConnection(dataSource);
        int countBatch = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("""
                      INSERT INTO DEBTS (CODE, SUMMA, collection_id) VALUES (?, ?, ?)""");

            for (Debt debt : debts) {
                statement.setString(1, dirDebts.get(debt.getId()).getCode());
                statement.setDouble(2, debt.getSumma());
                statement.setLong(3, debt.getCollectionId());

                statement.addBatch();
                countBatch++;

                if (countBatch % 1000 == 0 || countBatch == debts.size()) {
                    statement.executeBatch();
                }
            }
        } catch (Exception e) {
            LOG.error("При вставке задолженностей произошла ошибка: " + e.fillInStackTrace());
        } finally {
            closeCon(connection);
        }
    }
}