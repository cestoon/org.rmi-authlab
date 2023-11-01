package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterServer extends Remote {

    /**
     * Prints file filename on the specified printer.
     *  @param filename The name of the file to be printed.
     * @param printer  The name of the printer.
     * @return
     */
    String print(String filename, String printer) throws RemoteException;

    /**
     * Lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>.
     *
     * @param printer The name of the printer.
     */
    void queue(String printer) throws RemoteException;

    /**
     * Moves job to the top of the queue.
     *  @param printer The name of the printer.
     * @param job     The job number.
     * @return
     */
    String topQueue(String printer, int job) throws RemoteException;

    /**
     * Starts the print server.
     */
    void start() throws RemoteException;

    /**
     * Stops the print server.
     */
    void stop() throws RemoteException;

    /**
     * Stops the print server, clears the print queue, and starts the print server again.
     */
    void restart() throws RemoteException;

    /**
     * Prints the status of the printer on the user's display.
     *
     * @param printer The name of the printer.
     */
    void status(String printer) throws RemoteException;

    /**
     * Prints the value of the parameter on the print server to the user's display.
     *
     * @param parameter The parameter to be read.
     * @return
     */
    String readConfig(String parameter) throws RemoteException;

    /**
     * Sets the parameter on the print server to value.
     *
     * @param parameter The parameter to be set.
     * @param value     The value to set the parameter to.
     */
    void setConfig(String parameter, String value) throws RemoteException;
}