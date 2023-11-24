package DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

public class DataBaseConnector {

    private static final String configSettings = "config/config.settings";
    private SQLServerDataSource dataSource;

    public DataBaseConnector() throws Exception
    {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(new File(configSettings)));
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(databaseProperties.getProperty("Server"));
        dataSource.setDatabaseName(databaseProperties.getProperty("Database"));
        dataSource.setUser(databaseProperties.getProperty("User"));
        dataSource.setUser(databaseProperties.getProperty("Password"));
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }

    public Connection getConnection() throws SQLServerException
    {
        return dataSource.getConnection();
    }
}
