package com.exercise.mybnb;

import com.exercise.mybnb.utils.Utils;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


@SpringBootApplication
@EnableJpaAuditing
public class MybnbApplication {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public static void main(String[] args) {
        SpringApplication.run(MybnbApplication.class, args);
    }

    @Bean
    public void initApp(){
        System.out.println("Is this executed?");
        String absPath = new File("").getAbsolutePath();
        System.out.println("Absolute path: " + absPath);
        String storage = absPath + "/storage/";
        if(Files.notExists(Paths.get(storage)))
            new File(storage).mkdir();
        String usersDir = storage + "users";
        String placesDir = storage + "places";
        if(Files.notExists(Paths.get(usersDir)))
            new File(usersDir).mkdir();
        if(Files.notExists(Paths.get(placesDir)))
            new File(placesDir).mkdir();
        Utils.setMainPath(storage);
        System.out.println("MainPath set to: " + Utils.getMainPath());
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }
    private Connector redirectConnector(){
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

}
