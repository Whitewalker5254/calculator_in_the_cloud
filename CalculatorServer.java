//import java.io.*;
//import java.net.*;
//import java.util.*;
//public class CalculatorServer {
//    public static String calc(String exp) {
//        StringTokenizer st = new StringTokenizer(exp, " ");
//        if (st.countTokens() != 3)
//            return "error";
//        String res = "";
//        int op1 = Integer.parseInt(st.nextToken());
//        String opcode = st.nextToken();
//        int op2 = Integer.parseInt(st.nextToken());
//        switch (opcode) {
//            case "+":
//                res = Integer.toString(op1 + op2);
//                break;
//            case "-":
//                res = Integer.toString(op1 - op2);
//                break;
//            case "*":
//                res = Integer.toString(op1 * op2);
//                break;
//            default:
//                res = "error";
//        }
//        return res;
//    }
//    public static void main(String[] args) {
//        BufferedReader in = null;
//        BufferedWriter out = null;
//        ServerSocket listener = null;
//        Socket socket = null;
//        try {
//            listener = new ServerSocket(9999); // 서버 소켓 생성
//            System.out.println("연결을 기다리고 있습니다.....");
//            socket = listener.accept(); // 클라이언트로부터 연결 요청 대기System.out.println("연결되었습니다.");
//            in = new BufferedReader(
//                    new InputStreamReader(socket.getInputStream()));
//            out = new BufferedWriter(
//                    new OutputStreamWriter(socket.getOutputStream()));
//            while (true) {
//                String inputMessage = in.readLine();
//                if (inputMessage.equalsIgnoreCase("bye")) {
//                    System.out.println("클라이언트에서 연결을 종료하였음");
//                    break; // "bye"를 받으면 연결 종료
//                }
//                System.out.println(inputMessage); // 받은 메시지를 화면에출력
//                String res = calc(inputMessage); // 계산. 계산 결과는res
//                out.write(res + "\n"); // 계산 결과 문자열 전송
//                out.flush();
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (socket != null)
//                    socket.close(); // 통신용 소켓 닫기
//                if (listener != null)
//                    listener.close(); // 서버 소켓 닫기
//            } catch (IOException e) {
//                System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
//            }
//        }
//    }
//}
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CalculatorServer {

    // Method to perform calculation, updated to handle division by zero and invalid arguments
    public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");
        if (st.countTokens() != 3)
            return "ERROR: too many or too few arguments"; // Error message for incorrect number of arguments

        String res;
        int op1 = Integer.parseInt(st.nextToken());
        String opcode = st.nextToken();
        int op2 = Integer.parseInt(st.nextToken());

        try {
            switch (opcode) {
                case "+":
                    res = Integer.toString(op1 + op2);
                    break;
                case "-":
                    res = Integer.toString(op1 - op2);
                    break;
                case "*":
                    res = Integer.toString(op1 * op2);
                    break;
                case "/":
                    res = Integer.toString(op1 / op2); // Division by zero will be caught by the catch block
                    break;
                default:
                    res = "ERROR: unknown operation"; // Error message for unknown operation
            }
        } catch (ArithmeticException e) {
            res = "ERROR: division by zero"; // Error message for division by zero
        }

        return res; // Return either the result or an error message
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // ThreadPool for handling multiple clients
        try (ServerSocket listener = new ServerSocket(1234)) { // Default port is 1234
            System.out.println("Calculator Server is running...");
            while (true) {
                Socket socket = listener.accept();
                executor.execute(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                        String inputMessage;
                        while ((inputMessage = in.readLine()) != null) {
                            if ("bye".equalsIgnoreCase(inputMessage)) {
                                break; // Exit if "bye" is received
                            }
                            String res = calc(inputMessage); // Perform calculation
                            out.write(res + "\n"); // Send back the result
                            out.flush();
                        }
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            System.out.println("Error closing the client socket.");
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
