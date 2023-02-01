package dataextractor;


import com.typesafe.config.Config;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DataExtractor {
    private URL Url;

    public DataExtractor(URL url) throws  IOException{
        Url =url;
    }

    protected synchronized ProductData extractedProductData(String href) throws IOException{
        if(href!=null && !href.isBlank()){
            ProductData productData = new ProductData();
            Map<String, String> productInformationMap= extractProductData(href);
            validateData(productData,productInformationMap);
            return productData;
        }
        return null;
    }

    private void validateData(ProductData productData,Map<String,String> productInformationMap){
        if(productInformationMap!=null){
            for(Map.Entry<String,String> productinfo:productInformationMap.entrySet()){
                String originalValue=Utilities.getOriginalKey(productinfo.getKey());
                switch (originalValue.toUpperCase()){
                    case "PRODUCTNAME":
                        productData.productName=productinfo.getValue();
                        break;
                    case "PRODUCTPRICE":
                        try {
                            productData.productPrice=Double.parseDouble(productinfo.getValue());
                            break;
                        }catch (NumberFormatException e){
                            break;
                        }
                    case "PRODUCTINFORMATION":
                        productData.productInformation=productinfo.getValue();
                        break;
                    case "ADDRESS":
                        String[] splittedAddress=productinfo.getValue().split(",");
                        String[] addressFormat=parseAddressFormat(splittedAddress);
                        for(int value=0;value<addressFormat.length;value++){
                            if(addressFormat[value].equalsIgnoreCase("company")){
                                productData.company=splittedAddress[value].strip();
                            }
                            if (addressFormat[value].equalsIgnoreCase("street")){
                                productData.street=splittedAddress[value].strip();
                            }
                            if (addressFormat[value].equalsIgnoreCase("pincode city")){
                                String[] subAdress=splittedAddress[value].strip().split(" ");
                                for(String val:subAdress){
                                    if(val.length()<4 && Character.isDigit(val.charAt(0))){
                                        subAdress=ArrayUtils.removeElement(subAdress,val);
                                    }
                                }
                                if(subAdress.length>=2){
                                    if(evaluatePincode(subAdress[0])){
                                        productData.pincode=subAdress[0];
                                        productData.city=subAdress[1];
                                    }else {
                                        productData.pincode=evaluatePincode(subAdress[1])? subAdress[1]:null;
                                        productData.city=subAdress[0];
                                    }

                                }
                            }
                            if (addressFormat[value].equalsIgnoreCase("country")){
                                productData.country=splittedAddress[value].strip();
                            }

                        }
                        break;
                    case "HANDELKLASSE":
                        try{
                            DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            LocalDateTime dateTime=LocalDateTime.parse(productData.mfdate,df);
                            productData.expdate=dateTime.plusDays(12).toString();
                        }catch (Exception e){
                            productData.expdate=null;
                        }
                        break;
                    case "PRODUCTIMAGE":
                        productData.productImage=productinfo.getValue();
                        break;
                }
            }

        }
    }

    private boolean evaluatePincode(String pincode){
        if(pincode.length()>=5){
            return pincode.strip().substring(0,5).matches("\\d{4,5}");
        }
        return false;
    }


    private String[] parseAddressFormat(String[] splittedArray){
        String[] addressFormat= Utilities.DEFAULT_ADDESS_FORMAT;
        if(splittedArray!=null){
            switch (splittedArray.length){
                case 1:
                    addressFormat=new String[]{Utilities.DEFAULT_ADDESS_FORMAT[0]};
                    break;
                case 2:
                    addressFormat=new String[]{Utilities.DEFAULT_ADDESS_FORMAT[0],Utilities.DEFAULT_ADDESS_FORMAT[2]};
                    break;
                case 3:
                    addressFormat=new String[]{Utilities.DEFAULT_ADDESS_FORMAT[0],Utilities.DEFAULT_ADDESS_FORMAT[2],Utilities.DEFAULT_ADDESS_FORMAT[3]};
                    break;
            }
        }
        return addressFormat;
    }

    private Map<String,String> extractProductData(String href) throws  IOException{
        if(href!=null){
            URL childURL= new URL(Url,href);
            Config config=Utilities.config;
            Map<String,String> productInformationMap= new HashMap<>();
            Document childDocument=Jsoup.connect(String.valueOf(childURL)).get();
            String productNameKey=config.getString("productName.filterKey");
            String productPriceKey=config.getString("productPrice.filterKey");
            String productDescriptionKey=config.getString("productInformation.filterKey");
            Elements productNameHtml=Utilities.extractElements(childDocument,config.getString("productName.filterBy"),productNameKey);
            Elements productPriceHtml=Utilities.extractElements(childDocument,config.getString("productPrice.filterBy"),productPriceKey);
            Elements productDescriptionHtml=Utilities.extractElements(childDocument,config.getString("productInformation.filterBy"),productDescriptionKey);
            String productName=extractProductName(productInformationMap,productNameKey,productNameHtml);
            extractProductPrice(productInformationMap,productPriceKey,productPriceHtml);
            extractProductImagePath(productInformationMap,productName,"productImage");
            extractProductDescription(productInformationMap,productDescriptionHtml);
            return productInformationMap;
        }
        return null;
    }
    private String extractProductName(Map<String,String> infoMap,String key,Elements productNameHtml){
        if(productNameHtml!=null && infoMap!=null && key!=null) {
            String productName=productNameHtml.text().strip();
            infoMap.put(key,productName);
            return productName;
        }
        return null;
    }
    private String extractProductPrice(Map<String,String> infoMap,String key,Elements priceHtml){
        if(priceHtml!=null && key!=null && priceHtml!=null){
            String price=priceHtml.text().strip();
            price=price.replace(",",".");
            price=price.replaceAll("[€$%&!#*]","");
            infoMap.put(key,price);
            return price;
        }
        return null;
    }



    private void extractProductImagePath(Map<String,String> infoMap, String productName,String key){
        if(infoMap!=null && productName!=null){
            infoMap.put(key,ImageFactory.getImagePath(productName));
        }

    }


    // fixme: The below function is more straight forward can be improved;
    private void extractProductDescription(Map<String,String> infoMap,Elements descriptionHtml){;
        if(descriptionHtml!=null){
            Elements keys=descriptionHtml.get(0).getElementsByTag("dt");
            Elements values=descriptionHtml.get(0).getElementsByTag("dd");
            if(keys.size()==values.size()){
                for(int i=0;i< keys.size();i++){
                    String key=keys.get(i).text();
                    if(validKey(key)){
                        infoMap.put(key,values.get(i).text());
                    }
                }
            }
        }

    }

    private boolean validKey(String key){
        try{
            return Utilities.parseKeys().contains(key);
        }catch(NullPointerException e){
            return false;
        }
    }




}
