import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;


public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_BASE_URL = "https://v6.exchangeratesapi.io/latest";

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Enter the base currency: ");
            String baseCurrency = reader.readLine();

            System.out.print("Enter the target currency: ");
            String targetCurrency = reader.readLine();

            System.out.print("Enter the amount: ");
            double amount = Double.parseDouble(reader.readLine());

            double convertedAmount = convertCurrency(baseCurrency, targetCurrency, amount);
            System.out.println(amount + " " + baseCurrency + " = " + convertedAmount + " " + targetCurrency);
        } catch (IOException e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
    }

    private static double convertCurrency(String baseCurrency, String targetCurrency, double amount) {
        try {
            String apiUrl = API_BASE_URL + "?access_key=" + API_KEY + "&base=" + baseCurrency + "&symbols=" + targetCurrency;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();

                JSONObject json = new JSONObject(response);
                JSONObject rates = json.getJSONObject("rates");
                double exchangeRate = rates.getDouble(targetCurrency);

                return amount * exchangeRate;
            } else {
                throw new IOException("Error: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while fetching exchange rates: " + e.getMessage());
            return 0.0;
        }
    }
}