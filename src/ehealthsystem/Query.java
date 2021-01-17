package ehealthsystem;

import java.sql.*;

public class Query {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet rs;

    public ResultSet retrieveSqlQuery(String SELECT_QUERY) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
            statement = connection.createStatement();
            return statement.executeQuery(SELECT_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

    public void updateSqlQuery(String UPDATE_QUERY) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
            statement = connection.createStatement();
            statement.executeUpdate(UPDATE_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void close(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
