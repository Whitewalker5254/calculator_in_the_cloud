//import java.io.*;
//import java.net.*;
//import java.util.*;
//public class CalculatorClient {
//    public static void main(String[] args) {
//        BufferedReader in = null;
//        BufferedWriter out = null;
//        Socket socket = null;
//        Scanner scanner = new Scanner(System.in);
//        try {
//            socket = new Socket("localhost", 9999);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            while (true) {
//                System.out.print("계산식(빈칸으로 띄어 입력,예:24 + 42)>>"); // 프롬프트
//                String outputMessage = scanner.nextLine(); // 키보드에서 수식 읽기
//                if (outputMessage.equalsIgnoreCase("bye")) {
//                    out.write(outputMessage + "\n"); // "bye" 문자열 전송
//                    out.flush();
//                    break; // 사용자가 "bye"를 입력한 경우 서버로 전송 후 연결 종료
//                }
//                out.write(outputMessage + "\n"); // 키보드에서 읽은 수식 문자열 전송
//                out.flush();
//                String inputMessage = in.readLine(); // 서버로부터 계산 결과 수신
//                System.out.println("계산 결과: " + inputMessage);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                scanner.close();
//                if (socket != null)
//                    socket.close(); // 클라이언트 소켓 닫기
//            } catch (IOException e) {
//                System.out.println("서버와 채팅 중 오류가 발생했습니다.");
//            }
//        }
//    }
//}
import java.io.*;
import java.net.*;
import java.util.*;

public class CalculatorClient {
    private static final String DEFAULT_SERVER_IP = "localhost";
    private static final int DEFAULT_SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter an expression (e.g., 24 + 42) or 'bye' to exit: ");
                String outputMessage = scanner.nextLine();
                if ("bye".equalsIgnoreCase(outputMessage)) {
                    break; // Exit if "bye" is entered
                }
                out.write(outputMessage + "\n"); // Send the expression to the server
                out.flush();
                String inputMessage = in.readLine(); // Read the result from the server
                System.out.println("Result: " + inputMessage);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
