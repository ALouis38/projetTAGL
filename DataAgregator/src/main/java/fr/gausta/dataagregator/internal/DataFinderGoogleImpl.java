package fr.gausta.dataagregator.internal;

import fr.gausta.dataagregator.DataFinderIntf;
import java.io.IOException;
import java.util.HashMap;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.osgi.service.log.LogService;

@Component(name = "DataFinderGoogleComponent")
@Provides(specifications = DataFinderIntf.class)
@Instantiate(name = "DataFinderGoogleComponentInstance")
public class DataFinderGoogleImpl implements DataFinderIntf {

    @Requires(optional = false, id = "logger")
    private LogService log;

    public HashMap<String, String> findData(String query) {
        HashMap<String, String> result;
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

    private HashMap<String, String> getLinksAndDesc(String urlString) {
        try {
            HashMap<String, String> resultat = new HashMap<String, String>();
            Document document = Jsoup.connect(urlString).userAgent("Mozilla").get();
            Elements answerers = document.select("li.g table tbody tr td a");
            for (Element answerer : answerers) {
                if(!(answerer.text().equals("")) && !(answerer.text() == null)
                        && !(answerer.attr("href").equals("")) && !(answerer.attr("href") == null)) {
                    String[] resulurl = answerer.attr("href").split("http://");
                    resultat.put(resulurl[1], answerer.text());
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
        log.log(LogService.LOG_INFO, "SampleProviderComponent start");
    }

    @Invalidate
    public void invalidate() {
        log.log(LogService.LOG_INFO, "SampleProviderComponent stop");
    }

}
