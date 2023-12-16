package Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import Database.Mapping;

public class Generation {

    // public static void main(String[] args) {
    //     Properties properties = new Properties();
    //     try {
    //         // Charger les configurations depuis java.conf
    //         properties.load(new FileInputStream("java.conf"));

    //         // Récupérer les valeurs spécifiques à partir des configurations
    //         String importStatement = properties.getProperty("%%import%%");
    //         String packageStatement = properties.getProperty("%%%package%%%");
    //         String publicClassStatement = properties.getProperty("%public class%");

    //         // Charger le modèle de template depuis temp.tp
    //         String templateContent = ""; // Chargez le contenu de temp.tp depuis le fichier

    //         // Appliquer les configurations au modèle de template
    //         String modifiedTemplate = templateContent
    //                 .replace("%%import%%", importStatement)
    //                 .replace("%%%package%%%", packageStatement);
    //                 // .replace("%public class%", publicClassStatement);

    //         // Utiliser le modèle modifié
    //         System.out.println(modifiedTemplate);

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    // public void JavaClassGeneration(String packagename, String classname,ArrayList<String> imports,ArrayList<Mapping> metadata){

    //     String ClassName = Character.toUpperCase(classname.charAt(0)) + classname.substring(1);

    //     Properties properties = new Properties();

    //     try {
    //         properties.load(new FileInputStream("java.conf"));

            
    //         String packageStatement = properties.getProperty("%%%package%%%");
    //         String packageName = properties.getProperty("%%namepackage%%");
    //         String pclassStatement = properties.getProperty("%publicClass%");
    //         String attr = properties.getProperty("%%attributes%%");

    //         StringBuilder templateContentBuilder = new StringBuilder();
    //         BufferedReader reader = new BufferedReader(new FileReader("Template.tp"));
    //         String line;

    //         while ((line = reader.readLine()) != null) {
    //             templateContentBuilder.append(line).append("\n");
    //         }
    //         reader.close();

    //         String templateContent = templateContentBuilder.toString();

    //         String newTemplate;

    //         templateContent.replace("%%%package%%%", packageStatement);
    //                         // .replace("%%namepackage%%",packageName);

    //         System.out.println(templateContent);

    //         // if (!imports.isEmpty()) {
    //         //     for (int i = 0; i < imports.size(); i++) {
    //         //         newTemplate = templateContent.replace("%%import%%", imports.get(i));
    //         //     }
    //         // }

    //         templateContent.replace("%publicClass%", pclassStatement)
    //                 .replace("%%classname%%", ClassName+"{");

    //                 System.out.println(templateContent);

    //         // if (!metadata.isEmpty()) {
    //         //     for (int i = 0; i < imports.size(); i++) {
    //         //         newTemplate = templateContent.replace("%%attributes%%", metadata.get(i).getMetadataType()+" "+ metadata.get(i).getColumnName()+";");
    //         //     }
    //         // }

            
    //             newTemplate = templateContent;

    //         BufferedWriter writer = new BufferedWriter(new FileWriter( ClassName+".java"));
    //         writer.write(newTemplate);
    //         writer.close();
            
    //     } catch (Exception e) {
    //         // TODO: handle exception
    //         e.printStackTrace();
    //     }

    // }

    public void JavaClassGeneration(String packagename, String classname, ArrayList<String> imports, ArrayList<Mapping> metadata) {

        String ClassName = Character.toUpperCase(classname.charAt(0)) + classname.substring(1);
    
        Properties properties = new Properties();
    
        try {
            properties.load(new FileInputStream("java.conf"));
    
            String packageStatement = properties.getProperty("%%%package%%%");
            String packageName = properties.getProperty("%%namepackage%%");
            String pclassStatement = properties.getProperty("%publicClass%");
            String attr = properties.getProperty("%%attributes%%");
    
            StringBuilder templateContentBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("Template.tp"));
            String line;
    
            while ((line = reader.readLine()) != null) {
                templateContentBuilder.append(line).append("\n");
            }
            reader.close();
    
            String templateContent = templateContentBuilder.toString();

            // Remplacer les imports

            if (!imports.isEmpty()) {
                for (String importStatement : imports) {
                    templateContent = templateContent.replace("%%import%%", importStatement);
                }
            }
    
            // Remplacer les métadonnées
            if (!metadata.isEmpty()) {
                StringBuilder attributesBuilder = new StringBuilder();
                StringBuilder gettersBuilder = new StringBuilder();
                StringBuilder settersBuilder = new StringBuilder();
                for (Mapping mapping : metadata) {
                    String attributeName = mapping.getColumnName();
                    String attributeType = mapping.getMetadataType();
    
                    // Génération de l'attribut dans la classe
                    attributesBuilder.append("\n private ").append(attributeType).append(" ").append(attributeName).append(";\n");
    
                    // Génération du getter correspondant
                    String capitalizedAttributeName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
                    gettersBuilder.append("\n public ").append(attributeType)
                                    .append(" get").append(capitalizedAttributeName).append("() { ")
                                    .append(" return this.").append(attributeName).append("; } \n");
    
                    // Génération du setter correspondant
                    settersBuilder.append("\n public void set").append(capitalizedAttributeName)
                                    .append("(").append(attributeType).append(" ").append(attributeName).append(") { ")
                                    .append(" this.").append(attributeName).append(" = ").append(attributeName).append("; } \n");
                }
    
                // Génération du constructeur vide (par défaut)
                String defaultConstructor = "\n public " + ClassName + "() { } \n";
    
                // Génération du constructeur de classe
                StringBuilder classConstructor = new StringBuilder("\n public " + ClassName + "(");
                for (int i = 0; i < metadata.size(); i++) {
                    Mapping mapping = metadata.get(i);
                    String attributeName = mapping.getColumnName();
                    String attributeType = mapping.getMetadataType();
    
                    classConstructor.append(attributeType).append(" ").append(attributeName);
                    if (i < metadata.size() - 1) {
                        classConstructor.append(", ");
                    }
                }
                classConstructor.append(") { ");
                for (Mapping mapping : metadata) {
                    String attributeName = mapping.getColumnName();
                    classConstructor.append("this.").append(attributeName).append(" = ").append(attributeName).append("; ");
                }
                classConstructor.append("} \n");
    
                // Remplacer les balises dans le modèle de template
                templateContent = templateContent.replace("%%%package%%%", packageStatement);
                templateContent = templateContent.replace("%%namepackage%%", packagename + ";");
                templateContent = templateContent.replace("%publicClass%", pclassStatement);
                templateContent = templateContent.replace("%%classname%%", ClassName + "{");
                templateContent = templateContent.replace("%%attributes%%", attributesBuilder.toString());
                templateContent = templateContent.replace("%%getters%%", gettersBuilder.toString());
                templateContent = templateContent.replace("%%setters%%", settersBuilder.toString());
                templateContent = templateContent.replace("%%defaultConstructor%%", defaultConstructor);
                templateContent = templateContent.replace("%%classConstructor%%", classConstructor.toString());
            }
    
            templateContent += "}\n";
    
            BufferedWriter writer = new BufferedWriter(new FileWriter(ClassName + ".java"));
            writer.write(templateContent);
            writer.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DotnetClassGeneration(String packagename, String classname, ArrayList<String> imports, ArrayList<Mapping> metadata) {

        String ClassName = Character.toUpperCase(classname.charAt(0)) + classname.substring(1);
    
        Properties properties = new Properties();
    
        try {
            properties.load(new FileInputStream("dotnet.conf"));
    
            String packageStatement = properties.getProperty("%%%package%%%");
            String packageName = properties.getProperty("%%namepackage%%");
            String pclassStatement = properties.getProperty("%publicClass%");
            String attr = properties.getProperty("%%attributes%%");
    
            StringBuilder templateContentBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("Template.tp"));
            String line;
    
            while ((line = reader.readLine()) != null) {
                templateContentBuilder.append(line).append("\n");
            }
            reader.close();
    
            String templateContent = templateContentBuilder.toString();

            // Remplacer les imports

            if (!imports.isEmpty()) {
                for (String importStatement : imports) {
                    templateContent = templateContent.replace("%%import%%", importStatement);
                }
            }
    
            // Remplacer les métadonnées
            if (!metadata.isEmpty()) {
                StringBuilder attributesBuilder = new StringBuilder();
                StringBuilder gettersBuilder = new StringBuilder();
                StringBuilder settersBuilder = new StringBuilder();
                for (Mapping mapping : metadata) {
                    String attributeName = mapping.getColumnName();
                    String attributeType = mapping.getMetadataType();
    
                    // Génération de l'attribut dans la classe avec les get et set
                    attributesBuilder.append("\n \t \t private ").append(attributeType).append(" ").append(attributeName).append("{ get;set;}\n");
    
                }
    
                // Génération du constructeur vide (par défaut)
                String defaultConstructor = "\n public " + ClassName + "() { } \n";
    
                // Génération du constructeur de classe
                StringBuilder classConstructor = new StringBuilder("\n public " + ClassName + "(");
                for (int i = 0; i < metadata.size(); i++) {
                    Mapping mapping = metadata.get(i);
                    String attributeName = mapping.getColumnName();
                    String attributeType = mapping.getMetadataType();
    
                    classConstructor.append(attributeType).append(" ").append(attributeName);
                    if (i < metadata.size() - 1) {
                        classConstructor.append(", ");
                    }
                }
                classConstructor.append(") { ");
                for (Mapping mapping : metadata) {
                    String attributeName = mapping.getColumnName();
                    classConstructor.append("this.").append(attributeName).append(" = ").append(attributeName).append("; ");
                }
                classConstructor.append("} \n");
    
                // Remplacer les balises dans le modèle de template
                templateContent = templateContent.replace("%%%package%%%", packageStatement);
                templateContent = templateContent.replace("%%namepackage%%", packagename + "{");
                templateContent = templateContent.replace("%publicClass%", "\t"+pclassStatement);
                templateContent = templateContent.replace("%%classname%%", ClassName + "{");
                templateContent = templateContent.replace("%%attributes%%", attributesBuilder.toString());
                templateContent = templateContent.replace("%%getters%%", gettersBuilder.toString());
                templateContent = templateContent.replace("%%setters%%", settersBuilder.toString());
                templateContent = templateContent.replace("%%defaultConstructor%%", defaultConstructor);
                templateContent = templateContent.replace("%%classConstructor%%", classConstructor.toString());
            }
    
            templateContent += "\t}\n";
            templateContent += "}\n";
    
            BufferedWriter writer = new BufferedWriter(new FileWriter(ClassName + ".cs"));
            writer.write(templateContent);
            writer.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        ArrayList<Mapping> newMap = new ArrayList<Mapping>();
        newMap = mp.metaDataBaseToAttributClass(mpl);
        ArrayList<String> imprt = mp.importFromAttributes(newMap);
        Generation gnr = new Generation();
        gnr.JavaClassGeneration("Test", "meteo", imprt, newMap);
        gnr.DotnetClassGeneration("Test", "meteo", imprt, newMap);
    }
    
}
