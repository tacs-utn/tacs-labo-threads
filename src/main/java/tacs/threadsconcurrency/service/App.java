package tacs.threadsconcurrency.service;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import tacs.threadsconcurrency.controller.RuntimeExceptionMapper;
import tacs.threadsconcurrency.controller.TransferenciaController;

import java.lang.management.ManagementFactory;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void main(String[] args) {
        configureLog();

        boolean serialize = extractSerializeFromArguments(args);

        ResourceConfig config = bindResources(serialize);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        QueuedThreadPool threadPool = configureThreadPool();
        Server server = new Server(threadPool);

        configurePort(server);
        configureJmx(server);

        try {
            startServer(servlet, server);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            server.destroy();
        }
    }

    private static void configureJmx(Server server) {
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addEventListener(mbContainer);
        server.addBean(mbContainer);
        server.addBean(Log.getLog());
    }

    private static void startServer(ServletHolder servlet, Server server) throws Exception {
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        server.start();
        server.join();
    }

    private static void configurePort(Server server) {
        ServerConnector http = new ServerConnector(server,
                new HttpConnectionFactory());
        http.setPort(8080);
        http.setIdleTimeout(30000);
        server.addConnector(http);
    }

    private static QueuedThreadPool configureThreadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(500);
        threadPool.setName("Jetty");
        return threadPool;
    }

    private static ResourceConfig bindResources(boolean serialize) {
        ResourceConfig config = new ResourceConfig();
        config.register(new AppBinder(serialize));
        config.register(TransferenciaController.class);
        config.register(RuntimeExceptionMapper.class);
        return config;
    }

    private static boolean extractSerializeFromArguments(String[] args) {
        boolean serialize = false;
        if (args.length > 0) {
            String s = "";
            for (String arg : args) {
                s += " " + arg;
            }
            System.out.println("args:" + s);
            String serializeArg = args[0];
            serialize = Boolean.valueOf(serializeArg);
        }
        return serialize;
    }

    private static void configureLog() {
        Logger.getGlobal().addHandler(new ConsoleHandler());
        Logger.getGlobal().setLevel(Level.ALL);
    }

}
