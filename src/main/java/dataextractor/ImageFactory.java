package dataextractor;
;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class ImageFactory {
    private static Map<String,String> images = new TreeMap<>();
    private static final String DEFAULT_DESTINATION="."+ File.separator +"resources";
    private static final String DEFAULT_IMAGE="Default Image";
    private static final String DEFAULT_IMAGE_EXTENSION=".png";
    private static final String DefaultImagePath=DEFAULT_DESTINATION+File.separator+DEFAULT_IMAGE+File.separator+DEFAULT_IMAGE_EXTENSION;

    static{
        putDefaultImage();
    }
    private static void putDefaultImage(){
        images.put(DEFAULT_IMAGE,DefaultImagePath);
    }

    public static  void putImage(String imageName, URL imageUrl, boolean saveFig) throws IOException{
        if(imageName!=null && imageUrl!=null){
            try{
                String resolvedimagePath=parseImagePath(imageName, imageUrl, saveFig);
                if(resolvedimagePath!=null && !resolvedimagePath.isBlank()){
                    images.put(imageName,resolvedimagePath);
                }
            }catch (Exception e){
                images.put(imageName,DefaultImagePath);
            }
        }
    }



    public static LinkedList<String> imagesKeys(){
        if(images!=null){
            return new LinkedList<>(images.keySet());
        }
        return null;
    }

    public static String getImagePath(String productName){
        if(imagesKeys().contains(productName)){
            return images.get(productName);
        }
        return null;
    }

    public static int imagesCount(){
        return images.size();
    }

    private static String parseImagePath(String imageName,URL imageURL, boolean saveFig) throws IOException {
        if(imageURL!=null){
            String imageNamePng=String.join("_",imageName.split(" ")).replaceAll("[.*:]","")+DEFAULT_IMAGE_EXTENSION;
            StringBuilder destinationPath=new StringBuilder(DEFAULT_DESTINATION);
            destinationPath.append(File.separator);
            destinationPath.append(imageNamePng);
            if(saveFig){
                File outputFilePath= Paths.get(destinationPath.toString()).toFile();
                ReadableByteChannel readImage = Channels.newChannel(imageURL.openStream());
                FileOutputStream writeImage = new FileOutputStream(outputFilePath);
                writeImage.getChannel().transferFrom(readImage, 0, Long.MAX_VALUE);
                writeImage.close();
                readImage.close();

            }
            return destinationPath.toString();
        }
        return null;
    }

}
