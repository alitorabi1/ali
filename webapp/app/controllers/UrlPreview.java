
/**
 * Created by chota_don on 15-02-21.
 */
package controllers;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlPreview {
    private String description=null;
    private String image=null;
    private String title=null;

    public String returnDescription(){
        return this.description;
    }

    public String returnImage(){
        return this.image;
    }

    public String returnTitle(){
        return this.title;
    }

    public void returnDescription(String url) throws Exception{
        Document doc;
        Connection con=Jsoup.connect(url);
        doc=con.get();
        String text=null;
        Elements metaDescription=doc.select("meta[property=og:description]");
        Elements metaImage=doc.select("meta[property=og:image]");
        Elements metaTitle=doc.select("meta[property=og:title]");
        if(metaDescription!=null && metaImage!=null && metaTitle!=null){
            this.description=metaDescription.attr("content");
            this.image=metaImage.attr("content");
            this.title=metaTitle.attr("content");
        }
        else{
            text=doc.title();
        }

    }
}











//
//
//package controllers;
// import org.jsoup.Connection; 
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document; 
//import org.jsoup.select.Elements; 
//import play.api.libs.json.Json; 
//
//
//
//
//
//
//
//public class UrlPreview { 
//    private static String description=null;
//     private static String img=null;
//    private static String title=null;
//     public static String getDescription(){ 
//        return description; //    } 
//    public static String getImg(){ 
//        return img; //    } 
//    public static String getTitle(){
//         return title; //    } 
//     public static void getUrl(String str){ 
//     try { 
//        Connection con = Jsoup.connect("http://"+str); 
//        Document doc = con.get(); 
//        String text = null; 
//        Elements metaDescription = doc.select("meta[property=og:description]"); 
//        Elements metaImage = doc.select("meta[property=og:image]"); 
//        Elements metaTitle = doc.select("meta[property=og:title]"); 
//        //Elements metaUrl = doc.select("meta[property=og:description]"); 
//        if(metaDescription != null && metaImage!=null && metaTitle!=null ) { 
//            description = metaDescription.attr("content"); 
//            img = metaImage.attr("content"); 
//            title = metaTitle.attr("content"); 
//        }
//else { 
//            text = doc.title();
//       } 
//    } catch (Exception var6) {
//         var6.printStackTrace();
//     } 
//    }
// }