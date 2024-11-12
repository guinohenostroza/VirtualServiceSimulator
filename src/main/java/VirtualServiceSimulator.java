import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class VirtualServiceSimulator {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Simulación de un servicio de autenticación
        server.createContext("/auth", new AuthHandler());

        // Simulación de un servicio de pago
        server.createContext("/payment", new PaymentHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor virtual ejecutándose en http://localhost:8080/");
    }

    static class AuthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            if ("POST".equals(exchange.getRequestMethod())) {
                // Simula autenticación exitosa
                response = "{\"status\": \"success\", \"userId\": \"12345\"}";
            } else {
                // Simula error de autenticación
                response = "{\"status\": \"error\", \"message\": \"Method not allowed\"}";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class PaymentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            if ("POST".equals(exchange.getRequestMethod())) {
                // Simula aprobación o rechazo aleatorio de pago
                boolean paymentApproved = Math.random() > 0.5;
                response = paymentApproved ? "{\"status\": \"approved\"}" : "{\"status\": \"rejected\"}";
            } else {
                response = "{\"status\": \"error\", \"message\": \"Method not allowed\"}";
            }
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

