package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectBase{
     
    private String[] DriverName = {"org.postgresql.Driver","oracle.jdbc.driver.OracleDriver","com.mysql.cj.jdbc.Driver"};
    private String[] sgbdUrl = {"jdbc:postgresql://localhost:5432/solaire","jdbc:oracle:thin:@localhost:1521:orcl","jdbc:mysql://localhost:3306/kiraro?user=root&password=root"};
    
    public String[] getDriverName(){
        return DriverName;
    }

    public String[] getSgbdUrl(){
        return sgbdUrl;
    }
    
    // sgbd = postgresql ou oracle ou mysql
    public Connection connectToDataBase(String sgbd,String user,String password) throws Exception,SQLException, ClassNotFoundException{
        Connection c= null;
        String bdd = sgbd.toLowerCase();
        switch (bdd) {
            case "postgresql":
                try {
                    Class.forName(this.getDriverName()[0]);
                    c = DriverManager.getConnection(this.getSgbdUrl()[0],user,password);
                } catch (Exception e) {
                    throw e;
                }
                break;
            case "oracle":
                try {
                    Class.forName(this.getDriverName()[1]);
                    c = DriverManager.getConnection(this.getSgbdUrl()[1],user,password);
                } catch (Exception e) {
                    throw e;
                }
                break;
            case "mysql":
                try {
                    Class.forName(this.getDriverName()[2]);
                    c = DriverManager.getConnection(this.getSgbdUrl()[2],user,password);
                } catch (Exception e) {
                    throw e;
                }
                break;
        }
        return c;
    }

}
