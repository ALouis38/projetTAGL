package org.news.datafinderagregator.internal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.felix.ipojo.annotations.*;
import org.news.agreg.SearchInfoItf;
import org.osgi.service.log.LogService;
import org.apache.felix.service.command.Descriptor;


@Provides(specifications = DataFinderAgregator.class)
@Component(name = "DataFinderAgregatorComponent")
@Instantiate(name = "DataFinderAgregatorComponentInstance")
public class DataFinderAgregator implements SearchInfoItf{
    
    @Requires(optional = false, id = "sampleInterface")
    private SearchInfoItf svc;
    
    @Requires(optional = false, id = "logger")
    private LogService log;

    @Property(name=SearchInfoItf.PROP_TYPE,value=SearchInfoItf.TYPE_DATAAGREG)
    private String type;
    
    @ServiceProperty(name = "osgi.command.scope", value = "afficherrecherche")
    String scope;

    @ServiceProperty(name = "osgi.command.function", value = "{}")
    String[] function = new String[] { "afficherrecherche" };
    
//    @ServiceProperty(name = "osgi.command.scope", value = "test")
//    String scope;
//
//    @ServiceProperty(name = "osgi.command.function", value = "{}")
//    String[] function = new String[] { "test" };

    @Descriptor("test")
    public void test() {
        System.out.println("test!");
    }
    
    @Bind(id = "logger")
    private void bindLogger(LogService log) {
    }
    
    @Unbind(id = "logger")
    private void unbindLogger(LogService log) {
    }
    
    @Bind(id = "sampleInterface")
    void setService(SearchInfoItf service) {
        log.log(LogService.LOG_INFO , "Got service");
        //svc.add(service);
        svc = service;
    }

    @Unbind(id = "sampleInterface")
    void clearService() {
        svc = null;
    }

    @Validate
    void validate() {
        System.out.println("Agregator start");
        log.log(LogService.LOG_INFO , "started");
    }

    @Invalidate
    void invalidate() {
        System.out.println("Agregator stop");
        log.log(LogService.LOG_INFO , "stopped");
    }

    public Map<URL, String> search(String query) {
        HashMap<URL, String> resultats = new HashMap<URL, String>();
       /* for(SearchInfoItf finder : svc) {
            resultats.putAll(finder.search(query));
        }*/
        resultats.putAll(svc.search(query));
        return resultats;
    }
    
    @Descriptor("afficherrecherche")
    public void afficherrecherche(/*String query*/) {
        for(Map.Entry<URL, String> entry : search("android").entrySet()) {
            System.out.println(entry);
        }
    }
}
