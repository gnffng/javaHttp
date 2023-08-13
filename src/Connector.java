import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connector {
    static final String USER_AGENT = "Mozilla/5.0";
    URL url;
    HttpURLConnection connection;
    
    public Connector(String strUrl) throws IOException {
        url = new URL(strUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
    }
    
    public Response response(Http http) throws IOException {
        switch (http){
            case Get:
                connection.setRequestMethod("GET");
                break;
            case Post:
                connection.setRequestMethod("POST");
                break;
            case Put:
                connection.setRequestMethod("PUT");
                break;
            case Delete:
                connection.setRequestMethod("DELETE");
                break;
        }

        Response response = new Response();
        response.setResponseCode(connection.getResponseCode());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null)  {
            stringBuffer.append(inputLine);
        }
        bufferedReader.close();

        response.setData(stringBuffer.toString());

        return response;
    }
    
}
