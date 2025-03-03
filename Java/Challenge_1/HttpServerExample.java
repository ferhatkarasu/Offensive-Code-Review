import com.sun.net.httpserver.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.Normalizer;

public class HttpServerExample {
    private static Connection connection;

    public static void main(String[] args) throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:comments.db");
        HttpServer srv = HttpServer.create(new InetSocketAddress(9000), 0);
        srv.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                String ret = "<!DOCTYPE html><html><head><title>Comments</title></head><body><table>";
                try {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM comments");
                    while (rs.next()) {
                        String comment = rs.getString("comment").replace("<", "&lt;").replace(">", "&gt;");
                        ret += "<tr><td>" + Normalizer.normalize(comment, Normalizer.Form.NFKC) + "</td></tr>\n";
                    }
                    response(he, 200, ret + "</table></body></html>");
                } catch (Exception exp) {
                    exp.printStackTrace();
                    response(he, 500, "Internal Server Error");
                }
            }
        });

  
        srv.createContext("/comment", new HttpHandler() {
            @Override
            public void handle(HttpExchange he) throws IOException {
                try {
                    JSONObject jsonObject = (JSONObject)(new JSONParser()).parse(new InputStreamReader(he.getRequestBody(), "UTF-8"));
                    PreparedStatement stmt = connection.prepareStatement("INSERT INTO comments VALUES(?)");
                    stmt.setString(1, (String)jsonObject.get("comment"));
                    stmt.executeUpdate();
                    response(he, 200, "Ok");
                } catch (Exception exp) {
                    exp.printStackTrace();
                    response(he, 500, "Internal Server Error");
                }
            }
        });

        srv.start();
        System.out.println("Server started on port 9000");
    }

    public static void response(HttpExchange he, int statusCode, String responseText) throws IOException {
        he.sendResponseHeaders(statusCode, responseText.length());
        OutputStream os = he.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }
}
