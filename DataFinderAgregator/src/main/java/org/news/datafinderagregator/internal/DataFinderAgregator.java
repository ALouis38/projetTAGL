package org.news.datafinderagregator.internal;
import org.apache.felix.ipojo.annotations.*;
import org.news.agreg.SearchInfoItf;
import org.osgi.service.log.LogService;


@Component(name = "DataFinderAgregatorComponent")
@Instantiate(name = "DataFinderAgregatorComponentInstance")
public class DataFinderAgregator {
    
    @Requires(optional = false, id = "sampleInterface")
    private SearchInfoItf svc;
    
    @Requires(optional = false, id = "logger")
    private LogService log;

    @Bind(id = "logger")
    private void bindLogger(LogService log) {
    }
    
    @Unbind(id = "logger")
    private void unbindLogger(LogService log) {
    }
    
    @Bind(id = "sampleInterface")
    void setService(SearchInfoItf service) {
        log.log(LogService.LOG_INFO , "Got service");
        svc = service;
    }

    @Unbind(id = "sampleInterface")
    void clearService() {
        svc = null;
    }

    @Validate
    void start() {
        // do something
        log.log(LogService.LOG_INFO , "started");
    }

    @Invalidate
    void stop() {
        // do something
        log.log(LogService.LOG_INFO , "stopped");
    }
}
