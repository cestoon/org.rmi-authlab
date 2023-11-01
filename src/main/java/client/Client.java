package client;

import database.SessionDAO;
import database.UserDAO;
import server.PrinterServer;

import java.rmi.Naming;
import java.util.Scanner;

public class Client {

    private static UserDAO userDAO = new UserDAO();
    private static SessionDAO sessionDAO = new SessionDAO();

    private static final int EXPIRATION_MINUTES = 60 * 12;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to the Printer software, WaKuWaKu!");
            PrinterServer printServer = (PrinterServer) Naming.lookup("rmi://localhost:8080/WaKuWaKu");
            String token;
            while (true) {
                System.out.println("Input your Username to login:");
                String userName = scanner.next();
                System.out.println("Input your password:");
                String password = scanner.next();
                User user = new User(userName, password);

                // authenticateUser
                boolean isUser = userDAO.authenticateUser(user.getName(), user.getPassword());
                token = sessionDAO.createSession(user.getName(), EXPIRATION_MINUTES);
                if (isUser) {
                    System.out.println("Successfully Logged In");
                    break;
                } else {
                    System.out.println("The account or password is incorrect");
                }
            }
            // while token is valid
            while (sessionDAO.validateSession(token)) {
                System.out.println("========================================================");
                System.out.println("Select a service you would like to use : ");
                serverList();
                String selectService = scanner.next().trim();
                if ("1".equals(selectService) || "Print".equals(selectService)) {
                    System.out.println("write in the file print: ");
                    String fileName = scanner.next();
                    System.out.println("write in the printer to print from: ");
                    String printer = scanner.next();
                    String printContent = printServer.print(fileName, printer);
                    System.out.println(printContent);
                    continue;
                } else if ("2".equals(selectService) || "Queue".equals(selectService)) {
                    System.out.println("write in the printer to select the queue from: ");
                    String printer = scanner.next();
                    printServer.queue(printer);
                    continue;
                } else if ("3".equals(selectService) || "set file to top of the Queue".equals(selectService)) {
                    System.out.println("Please enter the printer name: ");
                    String printer = scanner.next();
                    System.out.println("Please enter the name of the file to be moved to the top of the queue: ");
                    int job = Integer.parseInt(scanner.next());
                    String msg = printServer.topQueue(printer, job);
                    System.out.println(msg);
                    continue;
                } else if ("4".equals(selectService) || "start Server".equals(selectService)) {
                    System.out.println("Please enter the printer to be started");
                    String printer = scanner.next();
                    printServer.start();
                    System.out.println(printer + "Successful start-up");
                    continue;
                } else if ("5".equals(selectService) || "Stop Server".equals(selectService)) {
                    System.out.println("Please enter the printer to be shut down:");
                    String printer = scanner.next();
                    printServer.stop();
                    System.out.println(printer + "Close successfully");
                    continue;
                } else if ("6".equals(selectService) || "Restart Server".equals(selectService)) {
                    System.out.println("Please enter the printer to be restarted: ");
                    String printer = scanner.next();
                    printServer.restart();
                    continue;
                } else if ("7".equals(selectService) || "Get status".equals(selectService)) {
                    System.out.println("Please enter the printer whose status you want to view:");
                    String printer = scanner.next();
                    printServer.status(printer);
//                    System.out.println(printer + " status：" + status);
                    continue;
                } else if ("8".equals(selectService) || "Read Configuration".equals(selectService)) {
                    System.out.println("Please enter the printer whose parameter you want to view:");
                    String printer = scanner.next();
                    String string = printServer.readConfig(printer);
                    System.out.println("The configuration information is as follows");
                    System.out.println("Parameter Name：" + string);
                    continue;
                } else if ("9".equals(selectService) || "Set Configuration".equals(selectService)) {
                    System.out.println("Please enter the parameter name:");
                    String parameter = scanner.next();
                    System.out.println("Please enter the parameter value:");
                    String value = scanner.next();
                    printServer.setConfig(parameter, value);
//                    System.out.println("Setting parameters：" + msg);
                    continue;
                } else if ("10".equals(selectService) || "Log Out".equals(selectService)) {
                    return;
                } else {
                    System.out.println("please input again:");
                }
            }
        } catch (Exception e) {
            System.out.println("Connect Server fail " + e.getMessage());
        }
    }

    public static void serverList() {
        String format = "(%d) - %s";
        String[] services = {
                "Print",
                "Queue",
                "TopQueue : moves job to the top of the queue",
                "Start Server",
                "Stop Server",
                "Restart Server",
                "Get status",
                "Read Configuration",
                "Set Configuration",
                "Log Out",
        };
        for (int i = 0; i < services.length; i++) {
            System.out.println(String.format(format, i + 1, services[i]));
        }
    }

}
