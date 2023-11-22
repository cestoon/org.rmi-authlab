package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.*;


public class Servant extends UnicastRemoteObject implements PrinterServer {
    private static final Logger logger = Logger.getLogger(PrinterServer.class.getName());

    static {
        // Create a console handler
        ConsoleHandler handler = new ConsoleHandler();

        // Create a formatter that includes a timestamp and other details
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%1$tF %1$tT %2$s %3$s %n",
                        record.getMillis(),
                        record.getLevel().getLocalizedName(),
                        formatMessage(record));
            }
        });

        // Add the handler to the logger
        logger.addHandler(handler);

        // Set the log level to INFO
        logger.setLevel(Level.INFO);
    }

    public Servant() throws RemoteException {
        super();
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        System.out.println("Print file: " + filename + " on printer: " + printer);
        logger.log(Level.INFO, "User printed file: " + filename + " on printer: " + printer);
        return filename;
    }

    @Override
    public void queue(String printer) throws RemoteException {
        System.out.println("queue success");
    }

    @Override
    public String topQueue(String printer, int job) throws RemoteException {
        return "topQueue success";
    }

    @Override
    public void start() throws RemoteException {
        System.out.println("start success");
    }

    @Override
    public void stop() throws RemoteException {
        System.out.println("stop success");
    }

    @Override
    public void restart() throws RemoteException {
        System.out.println("restart success");
    }

    @Override
    public void status(String printer) throws RemoteException {
        System.out.println("status success");
    }

    @Override
    public String readConfig(String parameter) throws RemoteException {
        return "config value";
    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException {
        System.out.println("setConfig success");
    }

}
