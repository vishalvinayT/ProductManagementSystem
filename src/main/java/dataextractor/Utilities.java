package dataextractor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

// This is a Utility class for this package all filters and defaults  are stored here

public class Utilities {
    public static final Config config = ConfigFactory.parseFile(new File("scrapeConfig.json"));
    protected static final String DEFAULT_COMPANY="Warehouse-56697368616c";
    protected static final String DEFAULT_COUNTRY="Germany";
    protected static final Double DEFAULT_PRICE=1.00;
    protected static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    protected static final String DEFAULT_MFD_DATE= formatter.format(new Date());
    protected static final String DEFAULT_IMAGE=ImageFactory.getImagePath("Default Image");

    protected static final String[] DEFAULT_ADDESS_FORMAT=new String[]{"company","street","pincode city","country"};

    private static Map<String,String> filterKeys=new HashMap<>();

    public static void putKeyPair(String parseKey,String originalKey){
        if(originalKey!=null && parseKey!=null) {
            filterKeys.put(parseKey.strip(),originalKey.strip());
        }
    }
    public static LinkedList<String> parseKeys(){
        if(filterKeys!=null){
            return new LinkedList<>(filterKeys.keySet());
        }
        return null;
    }


    public static String getOriginalKey(String parseKey){
        if(parseKey!=null){
            if(parseKeys().contains(parseKey)){
                return filterKeys.get(parseKey);
            }
        }
        return null;
    }

    public static Elements extractElements(Document document, String extractBy, String extractKey){

        switch (extractBy.toLowerCase()){
            case "tag":
                return document.getElementsByTag(extractKey);
            case "class":
                return document.getElementsByClass(extractKey);
        }
        return null;
    }

}
