package client;

import database.SessionDAO;
import database.UserDAO;
import database.RoleDAO;
import server.PrinterServer;
import server.Role;

import java.rmi.Naming;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private static UserDAO userDAO = new UserDAO();
    private static RoleDAO roleDAO = new RoleDAO();
    private static SessionDAO sessionDAO = new SessionDAO();

    private static final int EXPIRATION_MINUTES = 60 * 12;

    private static Map<String, List<String>> roleOperations = new HashMap<>();


    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to the Printer software, WaKuWaKu!");
            PrinterServer printServer = (PrinterServer) Naming.lookup("rmi://localhost:8080/WaKuWaKu");

            String token;
            String roleName = "invalid";
            while (true) {
                System.out.println("Input your Username to login:");
                String userName = scanner.next();
                System.out.println("Input your password:");
                String password = scanner.next();
                User user = new User(userName, password, roleName);

                // authenticateUser
//                boolean isUser = userDAO.authenticateUser(user.getName(), user.getPassword());
                boolean isUser = true;
                token = sessionDAO.createSession(user.getName(), EXPIRATION_MINUTES);
                if (isUser) {
                    System.out.println("Successfully Logged In");
                    roleName = roleDAO.getUserRole(user.getName());
                    List<String> operations = Role.getOperationsForRole(roleName);
                    System.out.println("========================================================");
                    System.out.println("your current role is : " + roleName);
                    roleOperations.put(roleName, operations);
                    break;
                } else {
                    System.out.println("The account or password is incorrect");
                }
            }


            // while token is valid
            while (sessionDAO.validateSession(token)) {
                System.out.println("========================================================");
                System.out.println("Select a service you would like to use : ");

                Map<Integer, String> operationMap = getOperationsMapForRole(roleName);
                operationMap.forEach((key, value) -> System.out.println(key + " - " + value));

                String selectService = scanner.next().trim();
                try {
                    int serviceNumber = Integer.parseInt(selectService);
                    if (!operationMap.containsKey(serviceNumber)) {
                        System.out.println("Invalid selection. Please try again.");
                        continue;
                    }

                    String operation = operationMap.get(serviceNumber);
                    switch (operation) {
                        case "Print":
                            // Handle Print operation
                            System.out.println("write in the file print: ");
                            String fileName = scanner.next();
                            System.out.println("write in the printer to print from: ");
                            String printer = scanner.next();
                            String printContent = printServer.print(fileName, printer);
                            System.out.println(printContent);
                            break;
                        case "Queue":
                            // Handle Queue operation
                            System.out.println("write in the printer to select the queue from: ");
                            printer = scanner.next();
                            printServer.queue(printer);
                            break;
                        case "TopQueue":
                            System.out.println("Please enter the printer name: ");
                            printer = scanner.next();
                            System.out.println("Please enter the name of the file to be moved to the top of the queue: ");
                            int job = Integer.parseInt(scanner.next());
                            String msg = printServer.topQueue(printer, job);
                            System.out.println(msg);
                            break;
                        case "start":
                            System.out.println("Please enter the printer to be started");
                            printer = scanner.next();
                            printServer.start();
                            System.out.println(printer + "Successful start-up");
                            break;
                        case "stop":
                            System.out.println("Please enter the printer to be shut down:");
                            printer = scanner.next();
                            printServer.stop();
                            System.out.println(printer + "Close successfully");
                            break;
                        case "restart":
                            System.out.println("Please enter the printer to be restarted: ");
                            printer = scanner.next();
                            printServer.restart();
                            break;
                        case "status":
                            System.out.println("Please enter the printer whose status you want to view:");
                            printer = scanner.next();
                            printServer.status(printer);
//                            System.out.println(printer + " status：" + status);
                            break;
                        case "readConfig":
                            System.out.println("Please enter the printer whose parameter you want to view:");
                            printer = scanner.next();
                            String string = printServer.readConfig(printer);
                            System.out.println("The configuration information is as follows");
                            System.out.println("Parameter Name：" + string);
                            break;
                        case "setConfig":
                            System.out.println("Please enter the parameter name:");
                            String parameter = scanner.next();
                            System.out.println("Please enter the parameter value:");
                            String value = scanner.next();
                            printServer.setConfig(parameter, value);
                            break;
                        case "updateRole":
                            System.out.println("Please enter the user to be updated:");
                            parameter = scanner.next();
                            System.out.println("Please enter the new role of the user:");
                            value = scanner.next();
                            break;
                        case "addUser":
                            System.out.println("Please enter the new user name:");
                            parameter = scanner.next();
                            System.out.println("Please enter the new user role:");
                            value = scanner.next();
                            System.out.println("new user: " + parameter + " added successfully");
                            break;
                        case "Log Out":
                            return;
                        default:
                            System.out.println("Operation not supported.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }

            }
        } catch (Exception e) {
            System.out.println("Connect Server fail " + e.getMessage());
        }
    }

    private static Map<Integer, String> getOperationsMapForRole(String roleName) {
        Map<Integer, String> operationsMap = new HashMap<>();
        List<String> operations = roleOperations.get(roleName);
        for (int i = 0; i < operations.size(); i++) {
            operationsMap.put(i + 1, operations.get(i));
        }
        operationsMap.put(operations.size() + 1, "Log Out");
        return operationsMap;
    }

}
