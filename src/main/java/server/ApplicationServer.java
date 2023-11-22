package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;


import static database.Database.*;
import static database.SessionDAO.createTable;

public class ApplicationServer {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(8080);
        registry.rebind("WaKuWaKu", new Servant());

        createDatabase();
        createTable();

        // server start to load role info
        LoadUserRole.loadRolesNew();

    }
}
