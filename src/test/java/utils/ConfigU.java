package utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ConfigU {

    private static String readProps(String key) throws IOException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        props.load(fis);

        return props.getProperty(key);
    }


    private static String readPropsUser(String value){
        Dotenv dotenv = Dotenv.configure().load();
        return dotenv.get(value);

    }


    public static String getEmailUser(){
        return readPropsUser("USER_EMAIL");
    }


    public static String getPassUser(){
        return readPropsUser("USER_PASS");
    }


    public static String getBrowser() throws IOException {
        return readProps("browser");
    }


    public static String getEnv() throws IOException {
        return readProps("env");
    }


    public static String getURL(String key) throws IOException {
        return readProps(key);
    }











}
