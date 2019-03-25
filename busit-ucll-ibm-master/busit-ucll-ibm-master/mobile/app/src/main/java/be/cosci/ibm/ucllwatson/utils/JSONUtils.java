package be.cosci.ibm.ucllwatson.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.cosci.ibm.ucllwatson.utils.item.ReceiptItem;

/**
 * Created by Petr on 28-Mar-18.
 */
public final class JSONUtils {

    /**
     * @param json
     * @return
     */
    public static List<ReceiptItem> parseJSONtoReceiptItems(String json) {
        List<ReceiptItem> receiptItemList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("0");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                String imageURL = jsonObject1.getString("image_url");
                String sourceUrl = jsonObject1.getString("source_url");
                String title = jsonObject1.getString("title");
                receiptItemList.add(new ReceiptItem(title, imageURL, sourceUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return receiptItemList;
    }

    /**
     * @param jsonURL
     * @return
     * @throws IOException
     */
    public static String downloadJSON(String jsonURL, String[] parameters) throws IOException {
        URL url;
        BufferedReader br;
        HttpURLConnection conn = null;
        StringBuilder sb = null;
        try {
            url = new URL(jsonURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);

            JSONObject jsonParam = new JSONObject();
            JSONArray jsonArray = new JSONArray(Arrays.asList(parameters));
            jsonParam.put("ingredients", jsonArray);

            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
            printout.writeBytes(jsonParam.toString());
            printout.flush();
            printout.close();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return sb.toString();
    }
}