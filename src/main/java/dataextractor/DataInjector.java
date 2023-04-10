package dataextractor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.typesafe.config.Config;
import dbtables.ProductData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;



public class DataInjector {
    private static List<ProductData> productDataList= new LinkedList<>();
    public void URLData(){
        Config config=Utilities.config;
        String URl1=config.getString("URL");
        try {
            Utilities.putKeyPair(config.getString("productName.parseKey"),"productName");
            Utilities.putKeyPair(config.getString("productPrice.parseKey"),"productPrice");
            Utilities.putKeyPair(config.getString("productInformation.parseKey"),"productInformation");
            Utilities.putKeyPair(config.getString("productInformation.subInformation.address.parseKey"),"address");
            Utilities.putKeyPair(config.getString("productInformation.subInformation.handelklasse.parseKey"),"handelKlasse");
            Utilities.putKeyPair(config.getString("productImage.parseKey"),"productImage");
            URL Url=new URL(URl1);
            Document document= Jsoup.connect(Url.toString()).timeout(60*1000).get();
            DataExtractor extractor=new DataExtractor(Url);
            Elements imgClass=Utilities.extractElements(document,config.getString("productImage.parentFilterBy"),config.getString("productImage.parentFilterKey"));
            injectImages(imgClass,config.getString("productImage.filterKey"), true);
            Elements productClass= Utilities.extractElements(document,config.getString("productHref.filterBy"),config.getString("productHref.filterKey"));
            for(int i=0;i<productClass.size();i++){
                try {
                    if(i%100==0){
                        System.out.printf("done..%d",i);
                        System.out.println();
                    }
                    addProductData(productClass,extractor,i);
                }catch (SocketTimeoutException e){
                    continue;
                }catch (IOException f){
                    f.printStackTrace();
                }
            }
            exposeJsonObject(productDataList);
              System.out.println("Done....");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void addProductData(Elements productClass,DataExtractor extractor,int index) throws IOException{
        String href=productClass.get(index).getAllElements().attr("href");
        ProductData productData=extractor.extractedProductData(href);
        if(productData!=null){
            productDataList.add(productData);
        }

    }

    private void injectImages(Elements fromCLass, String tagName, boolean savefig) throws IOException {
        if(fromCLass!=null && tagName!=null ){
            for (Element imageElement: fromCLass){
                Elements htmlImages= imageElement.getElementsByTag(tagName);
                for(Element imagePath: htmlImages){
                    try{
                        URL imageURL= new URL(imagePath.attr("src"));
                        String imageName= imagePath.attr("title").strip();
                        if(imageURL!=null && !imageName.isBlank()){
                            ImageFactory.putImage(imageName,imageURL,savefig);
                        }
                    }catch (MalformedURLException e){
                        continue;
                    }

                }
            }
        }
    }


    private void exposeJsonObject(List<ProductData> productDataList) throws IOException{
        BufferedWriter file= new BufferedWriter(new FileWriter("./configs/output_data.json"));
        Gson gson=new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(productDataList,file);
        file.flush();
        file.close();
    }

}
