package com.xtel.VietLotAPI.services;



import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.xtel.VietLotAPI.dao.VietlotDao;
import com.xtel.VietLotAPI.log4j.Logfactory;

public class App {
    public static void main(String[] args) {
    	Logger logger = Logfactory.getLogger(VietlotDao.class);
        ResourceConfig config = new ResourceConfig();
        config.packages("com.xtel.VietLotAPI.services");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(1213);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            System.out.println(server.toString());
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
           logger.warn("Can not Start server : "+ server.toString());
        }
        server.destroy();
    }

    

}
