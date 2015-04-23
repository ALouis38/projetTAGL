package fr.gausta.dataagregator.internal;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Unbind;
import org.apache.felix.ipojo.annotations.Validate;
import org.news.agreg.SearchInfoItf;
import org.osgi.service.log.LogService;


@Component(name = "SampleConsumerComponent")
@Instantiate(name = "SampleConsumerComponentInstance")
public class SampleConsumerImpl {
    
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
        log.log(LogService.LOG_INFO , "started");
    }

    @Invalidate
    void stop() {
        log.log(LogService.LOG_INFO , "stopped");
    }
}
