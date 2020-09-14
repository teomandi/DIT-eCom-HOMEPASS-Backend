package com.exercise.mybnb;

import com.exercise.mybnb.model.User;
import com.exercise.mybnb.repository.PlaceRepo;
import com.exercise.mybnb.repository.UserRepo;
import com.exercise.mybnb.utils.Utils;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private PlaceRepo placeRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

//    @Bean
//    public void dataGenerator(){
//        System.out.println("~Data Generator~");
//        System.out.println("Current users in DB: : " + userRepo.count());
//        System.out.println("Current places in DB: : " + placeRepo.count());
//        if(userRepo.count() > 10)
//            return;
//        // 1
//        User user1 = new User();
//        user1.setUsername("user1");
//        user1.setPassword(passwordEncoder.encode("111"));
//        user1.setEmail("11@11.11");
//        user1.setFirstName("prwtos");
//        user1.setLastName("xristis");
//        user1.setPhone("111");
//        user1.setHost(true);
//        user1.setImageName("default_user.jpg");
//        user1.setAddress("1111");
//        userRepo.save(user1);
//        // 2
//        User user2 = new User();
//        user2.setUsername("user2");
//        user2.setPassword(passwordEncoder.encode("222"));
//        user2.setEmail("22@22.22");
//        user2.setFirstName("deuteros");
//        user2.setLastName("xristis");
//        user2.setPhone("222");
//        user2.setHost(true);
//        user2.setImageName("default_user.jpg");
//        user2.setAddress("222");
//        userRepo.save(user2);
//        // 3
//        User user3 = new User();
//        user3.setUsername("user3");
//        user3.setPassword(passwordEncoder.encode("333"));
//        user3.setEmail("22@22.22");
//        user3.setFirstName("tritos");
//        user3.setLastName("xristis");
//        user3.setPhone("333");
//        user3.setHost(true);
//        user3.setImageName("default_user.jpg");
//        user3.setAddress("333");
//        userRepo.save(user3);
//        // 4
//        User user4 = new User();
//        user4.setUsername("user4");
//        user4.setPassword(passwordEncoder.encode("444"));
//        user4.setEmail("44@44.44");
//        user4.setFirstName("tetartos");
//        user4.setLastName("xristis");
//        user4.setPhone("444");
//        user4.setHost(true);
//        user4.setImageName("default_user.jpg");
//        user4.setAddress("4444");
//        userRepo.save(user4);
//        // 5
//        User user5 = new User();
//        user5.setUsername("user5");
//        user5.setPassword(passwordEncoder.encode("555"));
//        user5.setEmail("55@55.55");
//        user5.setFirstName("pemptos");
//        user5.setLastName("xristis");
//        user5.setPhone("555");
//        user5.setHost(true);
//        user5.setImageName("default_user.jpg");
//        user5.setAddress("5555");
//        userRepo.save(user5);
//    }

}
