package me.pedrocaires.chapt.core;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        runTomcat();
    }

    private static void runTomcat() {
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        //The port we will run is 8080
        tomcat.setPort(8080);

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.enableNaming();
        tomcat.getConnector();

        try {
            tomcat.start();
        } catch (LifecycleException ignored) {
        }
        tomcat.getServer().await();
    }
}
