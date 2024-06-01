package net.radewon.skyblock;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet({ "/skybank" })
public class BankAPI extends HttpServlet {
    private static final String token = getToken();
    private static final String uuid = "1ad33e99e0484995ac1d62134a1ca67a";
    private static final String PATH;
    private BankDAO dao = new BankDAO();

    public BankAPI() {
    }

    private static String getToken() {
        String t = "";

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("net/radewon/skyblock/config.prop"));
            t = p.getProperty("token");
        } catch (Exception var2) {
            System.out.println(var2.getMessage());
        }

        return t;
    }

    public static void refreshTransactions() throws IOException {
        try {
            URL url = new URL(PATH);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = null;
            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            JSONObject obj = new JSONObject(br.readLine());
            JSONObject bank = obj.getJSONArray("profiles").getJSONObject(0).getJSONObject("banking");
            JSONArray tr = bank.getJSONArray("transactions");
            String name = "";
            Transaction t = null;
            int count = 0;

            for (int i = 0; i < tr.length(); ++i) {
                JSONObject trans = tr.getJSONObject(i);
                name = trans.get("initiator_name").toString();
                BankDAO dao = new BankDAO();
                if (name.charAt(0) == 167) {
                    name = name.substring(2);
                }

                t = new Transaction(
                        Transaction.getAmountWithAction(trans.getInt("amount"), trans.get("action").toString()),
                        (Long) trans.get("timestamp"), name);
                if (dao.save(t)) {
                    ++count;
                }
            }

            System.out.println("11");
            System.out.println("There are " + count + " new transaction(s)!");
        } catch (Exception var12) {
            System.out.println(var12.getMessage());
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Bank Information</h2>");
        out.println("<table border=\"1\">");
        out.println("<tr><th>User ID</th><th>Total Money</th></tr>");

        try {
            ResultSet resultSet = this.dao.getBank();

            while (resultSet.next()) {
                String player = resultSet.getString("player");
                int totalMoney = resultSet.getInt("amount");
                out.println("<tr><td>" + player + "</td><td>" + totalMoney + "</td></tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception var7) {
            out.println("Error: " + var7.getMessage());
        }

    }

    static {
        PATH = "https://api.hypixel.net/v2/skyblock/profiles?key=" + token + "&uuid=1ad33e99e0484995ac1d62134a1ca67a";
    }
}