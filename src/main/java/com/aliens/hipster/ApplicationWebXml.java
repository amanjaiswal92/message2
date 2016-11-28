package com.aliens.hipster;

import com.aliens.hipster.web.Initialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * This is a helper Java class that provides an alternative to creating a web.xml.
 * This will be invoked only when the application is deployed to a servlet container like Tomcat, Jboss etc.
 */
@Slf4j
public class ApplicationWebXml extends SpringBootServletInitializer {

    @Autowired
    Initialize initialize;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        /**
         * set a default to use when no profile is configured.
         */
        //DefaultProfileUtil.addDefaultProfile(application.application());


          return application.profiles(addDefaultProfile())

                .sources(MycroftApp.class);
    }

    private String addDefaultProfile() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile != null) {
            log.info("Running with Spring profile(s) : {}", profile);
            return profile;
        }

        log.warn("No Spring profile configured, running with default configuration");
        return "dev";
    }

}
