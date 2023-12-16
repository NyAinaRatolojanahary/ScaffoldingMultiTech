package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Mapping {
    
    private String metadataType;
    private String columnName;


    public String getMetadataType() {
        return metadataType;
    }

    public void setMetadataType(String metadataType) {
        this.metadataType = metadataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Mapping(){}

    public Mapping(String dataType,String columnName){
        this.metadataType = dataType;
        this.columnName = columnName;
    }

    public ArrayList<Mapping> getMetadataNamePostgresql(Connection c,String sgbd, String user, String password,String tableName) throws Exception{

        ArrayList<Mapping> mapping = new ArrayList<Mapping>();
        
        String sql = "SELECT data_type,column_name from information_schema.columns where table_name='"+tableName+"';";
        try {
            ConnectBase con = new ConnectBase();
            c = con.connectToDataBase(sgbd, user, password);
            Statement st = c.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while (rst.next()) {
                Mapping mp = new Mapping();
                mp.setMetadataType(rst.getString("data_type"));
                mp.setColumnName(rst.getString("column_name"));
                mapping.add(mp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return mapping;

    }

    public ArrayList<Mapping> getMetadataNameMysql(Connection c, String sgbd, String user, String password,String database,String tableName)throws Exception{
        
        ArrayList<Mapping> mapping = new ArrayList<Mapping>();
        String sql = "SELECT data_type,column_name from information_schema.columns where table_schema='"+database+"' and table_name='"+ tableName+"';";
        try {
            ConnectBase con = new ConnectBase();
            c = con.connectToDataBase(sgbd, user, password);
            Statement st = c.createStatement();
            ResultSet rst = st.executeQuery(sql);
            while (rst.next()) {
                Mapping mp = new Mapping();
                mp.setMetadataType(rst.getString("data_type"));
                mp.setColumnName(rst.getString("column_name"));
                mapping.add(mp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping;
    }

    public ArrayList<Mapping> metaDataBaseToAttributClass(ArrayList<Mapping> map){
        ArrayList<Mapping> newMapping = new ArrayList<Mapping>();

        for (int i = 0; i < map.size(); i++) {
            Mapping mp = new Mapping();
            switch (map.get(i).getMetadataType()) {
                case "int":
                    mp.setMetadataType("int");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "integer":
                    mp.setMetadataType("int");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "decimal":
                    mp.setMetadataType("double");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "numeric":
                    mp.setMetadataType("double");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "float":
                    mp.setMetadataType("float");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "double":
                    mp.setMetadataType("double");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "double precision":
                    mp.setMetadataType("double");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "character":
                    mp.setMetadataType("String");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "text":
                    mp.setMetadataType("String");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "varchar":
                    mp.setMetadataType("String");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "date":
                    mp.setMetadataType("Date");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "datetime":
                    mp.setMetadataType("Timestamp");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "timestamp":
                    mp.setMetadataType("Timestamp");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "timestamp without time zone":
                    mp.setMetadataType("Timestamp");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;
                case "boolean":
                    mp.setMetadataType("boolean");
                    mp.setColumnName(map.get(i).getColumnName());
                    newMapping.add(mp);
                    break;                
            }
            
        }

        return newMapping;
    }

    public ArrayList<String> importFromAttributes(ArrayList<Mapping> map){

        ArrayList<String> allImports = new ArrayList<String>();

        for (int i = 0; i < map.size(); i++) {
            String imp = "";

            switch (map.get(i).getMetadataType()) {
                case "Timestamp":
                    imp = "import java.sql.Timestamp;";
                    allImports.add(imp);
                    break;
                case "Date":
                    imp = "import java.sql.Date;";
                    allImports.add(imp);
                    break;
            }
        }
        
        return allImports;

    }

    
    

    public static void main(String[] args) {
        Mapping mp = new Mapping();
        ArrayList<Mapping> mpl = new ArrayList<Mapping>();
        try {
            Connection c = null;
            mpl = mp.getMetadataNamePostgresql(c, "postgresql", "postgres", "root","meteo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(mpl.size());
        // for (int i = 0; i < mpl.size(); i++) {
        //     System.out.println(mpl.get(i).getMetadataType()+":"+mpl.get(i).getColumnName());
        // }
        ArrayList<Mapping> newMap = new ArrayList<Mapping>();
        newMap = mp.metaDataBaseToAttributClass(mpl);
        // for (int i = 0; i < newMap.size(); i++) {
        //     System.out.println(newMap.get(i).getMetadataType()+":"+newMap.get(i).getColumnName());
        // }

        ArrayList<String> imprt = mp.importFromAttributes(newMap);
        System.out.println(imprt.size());
        for (int i = 0; i < imprt.size(); i++) {
            System.out.println(imprt.get(i));
        }

    }
    
    

}
