package ru.etl.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.etl.configuration.Connect2DB;
import ru.etl.configuration.DataSource;
import ru.etl.model.PrCred;

import java.lang.invoke.MethodHandles;
import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class SqlCFTRepository implements Connect2DB, CFTRepository {

    @NonNull
    private final DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Connection initConnection(DataSource dataSource) {
        Connection connection = null;
        try {
            Class.forName(dataSource.getDriver());
            connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(),
                    dataSource.getPassword());
        } catch (Exception e) {
            LOG.error("При подключении к БД получателя произошла ошибка: " + e.fillInStackTrace());
        }
        return connection;
    }

    @Override
    public void closeCon(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            LOG.error("При закрытии подключения к БД получателя произошла ошибка: " + e.fillInStackTrace());
        }
    }

    @Override
    public Set<Long> getIDAllCredInSet() {
        Connection connection = initConnection(dataSource);
        Set<Long> setIds = new HashSet<>();
        try {
            ResultSet rs = connection.createStatement()
                        .executeQuery("select pr.id "
                                        + "from  \"Z#PR_CRED\" pr "
                                        + "where pr.C_COM_STATUS = 'WORK'");
            while (rs.next()) {
                setIds.add(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCon(connection);
        }
        return setIds;
    }

    @Override
    public PrCred getCred(long id) {
        Connection connection = initConnection(dataSource);
        PrCred cred = new PrCred();
        try {
            PreparedStatement statement = connection
                    .prepareStatement("select  pr.ID, pr.c_NUM_DOG, pr.C_FT_CREDIT, pr.c_LIST_PAY, pr.c_LIST_PLAN_PAY "
                                        + "from   \"Z#PR_CRED\" pr "
                                        + "where  pr.id = (?);");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();

            cred.setId(rs.getLong(1));
            cred.setNumDog(rs.getString(2));
            cred.setVal(rs.getString(3));
            cred.setCollectionFO(rs.getLong(4));
            cred.setCollectionPO(rs.getLong(5));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCon(connection);
        }
        return cred;
    }

    @Override
    public List<PrCred> getCreds(List<Long> listIds) {
        List<PrCred> creds = new ArrayList<>();
        Connection connection = initConnection(dataSource);
        try {
            String sql = String.format("select  pr.ID, pr.c_NUM_DOG, pr.C_FT_CREDIT, pr.c_LIST_PAY, pr.c_LIST_PLAN_PAY "
                    + "from   \"Z#PR_CRED\" pr "
                    + "where  pr.id in (" + StringUtils.join(listIds, ',') + ");");
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                PrCred cred = new PrCred();
                cred.setId(rs.getLong(1));
                cred.setNumDog(rs.getString(2));
                cred.setVal(rs.getString(3));
                cred.setCollectionFO(rs.getLong(4));
                cred.setCollectionPO(rs.getLong(5));
                creds.add(cred);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCon(connection);
        }
        return creds;
    }
}
