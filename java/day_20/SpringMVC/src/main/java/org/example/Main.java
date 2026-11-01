package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.example.config.webcongif;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class Main {

    public static void main(String[] args) throws LifecycleException {

        // 1. Create Embedded Tomcat
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);
        tomcat.getConnector();

        // 2. Create Tomcat Servlet Context
        String contextPath = "";

        String baseDir = new File("src/main/webapp").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, baseDir);

        // 3. Create Spring WebApplicationContext (IoC Container)
        AnnotationConfigWebApplicationContext springContext =
                new AnnotationConfigWebApplicationContext();

        // 4. Register Spring Configuration
        springContext.register(webcongif.class);

        // 5. Connect Spring with Tomcat ServletContext
        springContext.setServletContext(context.getServletContext());

        // 6. Initialize Spring Container
        springContext.refresh();

        // 7. Create DispatcherServlet
        DispatcherServlet dispatcherServlet =
                new DispatcherServlet(springContext);

        // 8. Register DispatcherServlet with Tomcat
        Wrapper wrapper = Tomcat.addServlet(
                context,
                "dispatcherServlet",
                dispatcherServlet
        );

        wrapper.setLoadOnStartup(1);

        context.addServletMappingDecoded(
                "/",
                "dispatcherServlet"
        );

        // 9. Start Tomcat
        tomcat.start();
        System.out.println("Spring MVC Application Started!");
        // 10. Keep Server Running
        tomcat.getServer().await();
    }
}