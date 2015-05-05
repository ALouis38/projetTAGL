package fr.gausta.googledatafinder.internal;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.annotations.Property;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.news.agreg.SearchInfoItf;
import org.osgi.service.log.LogService;

@Component(name = "DataFinderGoogleComponent")
@Provides(specifications = SearchInfoItf.class)
@Instantiate(name = "DataFinderGoogleComponentInstance")
public class DataFinderGoogleImpl implements SearchInfoItf {

    @Requires(optional = false, id = "logger")
    private LogService log;
    
    @Property(name=SearchInfoItf.PROP_TYPE,value=SearchInfoItf.TYPE_DATAFINDER)
    private String type;

    public Map<URL, String> search(String query) {
        HashMap<URL, String> result;
        String url = getUrlFromQuery(query);
        result = getLinksAndDesc(url);
        return result;
    }

    private String getUrlFromQuery(String query) {
        String url = "https://www.google.fr/search?hl=fr&gl=fr&tbm=nws&authuser=0&q=";
        String[] separatedQuery = query.split(" ");
        for (String s : separatedQuery) {
            url += s + "+";
        }
        url = url.substring(0, url.length()-1);
        url += "&gws_rd=ssl"; 
        return url;
    }

    private HashMap<URL, String> getLinksAndDesc(String urlString) {
        try {
            HashMap<URL, String> resultat = new HashMap<URL, String>();
            Document document = Jsoup.connect(urlString).userAgent("Mozilla").get();
            Elements answerers = document.select("li.g table tbody tr td a");
            for (Element answerer : answerers) {
                if(!(answerer.text().equals("")) && !(answerer.text() == null)
                        && !(answerer.attr("href").equals("")) && !(answerer.attr("href") == null)) {
                    String[] resulurl = answerer.attr("href").split("http://");
                    resultat.put(new URL("http://"+resulurl[1]), answerer.text());
                }
            }

            return resultat;
        } catch (IOException ex) {
            Logger.getLogger(DataFinderGoogleImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Bind(id = "logger")
    private void bindLogger(LogService log) {
    }

    @Unbind(id = "logger")
    private void unbindLogger(LogService log) {
    }

    @Validate
    public void validate() {
        System.out.println("Google impl start");
        log.log(LogService.LOG_INFO, "SampleProviderComponent start");
    }

    @Invalidate
    public void invalidate() {
        System.out.println("Google impl stop");
        log.log(LogService.LOG_INFO, "SampleProviderComponent stop");
    }

}
