package app;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;



/**
 * SQL Connection
 * @author: Daniel Casper
 **/

public class DBConnection {

    /***
     * This opens the connection to the database
     * @return connection
     */
    @SuppressWarnings("exports")
    public static Connection connectDB(){
        String url = "urlGoesHere"; 
        String username = "username";
        String password = "paassword";
        //ArrayList<String> tasks = new ArrayList<String>();
        Connection connection;
        try{
            connection = DriverManager.getConnection(url, username, password);
        }catch (SQLException e) {
            throw new IllegalStateException("Cannot connect!", e);
        }
        return connection;
    }

    public static void closeDB(@SuppressWarnings("exports") Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException("Cannot close database", e);
        }

    }

    public static int getSeriesIDByTitle(String SeriesTitle){
        int SeriesID;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM Series WHERE SeriesTitle=\""+SeriesTitle+"\";");

            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            
            if (rs.getRow()==0){
                return 0;
            } 
            SeriesID= rs.getInt("SeriesID");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return SeriesID;
    }
    
    public static String getPublisherByID(int seriesID){
        String Publisher;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Publisher FROM Series WHERE seriesID="+seriesID+";");
            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            if (rs.getRow()==0){
                return "";
            } 
            Publisher= rs.getString("Publisher");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return Publisher;
    }

    public static boolean entryExists(int SeriesID, String issueName, String dateString, int month, int day, int year){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueID FROM Comic WHERE SeriesID="+SeriesID+" AND issueName='"+issueName+"' AND dateString='"+dateString+"' AND month="+month+" AND day="+day+" AND year="+year+";");

            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            
            if (rs.getRow()==0){
                return false;
            } 
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return true;
    }
    
    public static int getNumSeries(){
        int count;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM SERIES;");

            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            
            if (rs.getRow()==0){
                return 0;
            }
            
            count= rs.getInt("count");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return count;
    }
    
    public static int getDateByString(String dateString){
        int dateID;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT dateID FROM COMIC WHERE dateString=\""+dateString+"\";");

            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            
            if (rs.getRow()==0){
                return 0;
            } 
            dateID= rs.getInt("dateID");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return dateID;
    }
    
    public static int updateIssueCount(int SeriesID){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE Series SET issueID=issueID+1 WHERE SeriesID="+SeriesID+";";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            

            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return SeriesID;
    }
    
    public static int createSeries(String SeriesTitle, int issueID, String Publisher, boolean xmen){
        Connection connection = connectDB();
        int SeriesID = 0;
        try{
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO Series(SeriesTitle, issueID, Publisher, xmen) VALUES('"+SeriesTitle+"', "+issueID+", '"+Publisher+"', "+xmen+" );";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                SeriesID = resultSet.getInt(1);
                System.out.println("Generated primary key: " + SeriesID);
            }
            
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return SeriesID;
    }

    public static boolean isXmenByID(int SeriesID){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT xmen FROM Series WHERE SeriesID="+SeriesID+";");

            //rs.next() must be performed here because otherwise you get an SQLException Error. This still returns the first instance of "name"

            rs.next();
            
            if (rs.getRow()==0){
                return false;
            } 
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return true;
    }

    public static int getTotal(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM Series;");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            } 
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }
    
    public static int getTotalMonth(int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE month="+month+" AND year="+year+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            } 
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    public static int getNumPublisher(String Publisher){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM Series WHERE Publisher='"+Publisher+"';");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            } 
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }
    
    public static int getNumByID(int seriesID, int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE month="+month+" AND year="+year+" and seriesID="+seriesID+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            } 
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }
    
    public static int getNumXmenByID(int seriesID, int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE month="+month+" AND year="+year+" and seriesID="+seriesID+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            } 
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }
    
    public static int getNumXMen(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM Series WHERE xmen=1;");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            } 
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    public static int addIssue(int SeriesID, String issueName, String dateString, int month, int day, int year){
        int issueID = -1;
        Connection connection = connectDB();
        try{
            Statement statement = connection.createStatement();
            String sql = "insert into Comic(SeriesID, issueName, dateString, month, day, year) values("+SeriesID+", '"+issueName+"','"+dateString+"', "+month+", "+day+", "+year+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                issueID = resultSet.getInt(1);
                System.out.println("Generated primary key: " + issueID);
            }
            updateIssueCount(SeriesID);
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return issueID;
    }
    
    public static int newDate(String dateString, int day, int month, int year){
        Connection connection = connectDB();
        int dateID = 0;
        try{
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO Date(dateString,month,day, year) VALUES('"+dateString+"', "+month+", "+day+", "+year+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                dateID = resultSet.getInt(1);
                System.out.println("Generated primary key: " + dateID);
            }
            
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return dateID;
    }




}

