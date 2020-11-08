package za.co.familytracker.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WSCallsUtils extends AsyncTask<String, Void, String>
{
    private WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller;
    private String dialogText;

    private int reqCode;
    //private static HashMap<String, String> urlMap;

    private WSCallsUtils(WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller, int reqCode)
    {
        this.wsCallsUtilsTaskCaller = wsCallsUtilsTaskCaller;
        this.reqCode = reqCode;
    }

    public static void get(WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller, String url, int reqCode)
    {
        try
        {
            //url = getUrl(url);

            WSCallsUtils wsCallsUtils = new WSCallsUtils(wsCallsUtilsTaskCaller, reqCode);
            wsCallsUtils.execute(ConstantUtils.REQUEST_METHOD_GET, url);
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - get"
                    + "\nURL: " + url
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    public static void post(WSCallsUtilsTaskCaller wsCallsUtilsTaskCaller, String url, String body, int reqCode)
    {
        try
        {
            //url = getUrl(url);

            WSCallsUtils wsCallsUtils = new WSCallsUtils(wsCallsUtilsTaskCaller, reqCode);
            wsCallsUtils.execute(ConstantUtils.REQUEST_METHOD_POST, url, body);
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - post"
                    + "\nURL: " + url
                    + "\nBody: " + body
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {

        //JSONObject toReturn = null;
        String toReturn = null;

        String requestMethod = strings[0];
        String url = strings[1];
        try
        {
            if(requestMethod.equals(ConstantUtils.REQUEST_METHOD_GET))
            {
                //GET
                toReturn = genericGET(url);
                //toReturn = new JSONObject();
                //String response = genericGET(url);

                /*toReturn.put("url", url);
                toReturn.put("response", response);*/
            }else if(requestMethod.equals(ConstantUtils.REQUEST_METHOD_POST))
            {
                //POST
                String body = strings[2];

                toReturn = genericPOST(url, body);
                /*toReturn = new JSONObject();
                String response = genericPOST(url, body);

                toReturn.put("url", url);
                toReturn.put("body", body);
                toReturn.put("response", response);*/

            }
        }catch(Exception e)
        {
            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - doInBackground"
                    + "\nURL: " + url
                    + "\nBody: " + strings[1]
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally
        {

        }

        /*if(toReturn != null)
        {
            return toReturn.toString();
        }*/

        return toReturn;
    }


    @Override
    protected void onPostExecute(String body)
    {

        this.wsCallsUtilsTaskCaller.taskCompleted(body, this.reqCode);
        /*try
        {
            SQLiteUtils sqLiteUtils = new SQLiteUtils(this.wsCallsUtilsTaskCaller.getActivity());

            String url = null;
            String response = null;

            JSONObject jsonObject = new JSONObject(body);

            if(jsonObject != null)
            {
                if(jsonObject.has("response"))
                {
                    //cache the response
                    url = jsonObject.getString("url");
                    response = jsonObject.getString("response");
                    sqLiteUtils.cacheResponse(url, response, GeneralUtils.getCurrentDateTime());

                }else
                {
                    //get from cache
                    url = jsonObject.getString("url");
                    response = sqLiteUtils.getResponse(url);
                }
            }

            this.wsCallsUtilsTaskCaller.taskCompleted(response, this.reqCode);

        }catch(Exception e)
        {
            Log.e(ConstantUtils.TMSX, "\nError: " + e.getMessage()
                    + "\nMethod: WSCallsUtils - doInBackground"
                    + "\nbody: " + body
                    + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
        }*/

    }

    private String genericGET(String url)
    {
        String toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ConstantUtils.TIMEOUT_CONNECTION);
            connection.setReadTimeout(ConstantUtils.TIMEOUT_READ);
            connection.setDoInput(true);

            connection.connect();

            if(connection != null)
            {
                if(connection.getResponseCode() >= 400)
                {
                    //Cache Data

                    Log.e(ConstantUtils.TAG, "\nError: " + connection.getResponseMessage()
                            + "\nCode: " + connection.getResponseCode()
                            + "\nURL: " + url
                            + "\nMethod: genericGET"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }else
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String output = "";
                    StringBuilder sb = new StringBuilder();

                    while((output = bufferedReader.readLine()) != null)
                    {
                        sb.append(output);
                    }
                    toReturn = sb.toString();
                }
            }

        }catch(Exception e)
        {
            //Cache Data

            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nMethod: genericGET"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally
        {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }

    private String genericPOST(String url, String body)
    {
        String toReturn = null;

        HttpURLConnection connection = null;

        try
        {
            URL target = new URL(url);
            connection = (HttpURLConnection) target.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ConstantUtils.TIMEOUT_CONNECTION);
            connection.setReadTimeout(ConstantUtils.TIMEOUT_READ);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            outputStreamWriter.close();

            connection.connect();

            if(connection != null)
            {
                if(connection.getResponseCode() >= 400)
                {
                    //Cache Data

                    Log.e(ConstantUtils.TAG, "\nError: " + connection.getResponseMessage()
                            + "\nCode: " + connection.getResponseCode()
                            + "\nURL: " + url
                            + "\nMethod: genericPOST"
                            + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
                }else
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String output = "";

                    while((output = bufferedReader.readLine()) != null) {
                        sb.append(output);
                    }

                    toReturn = sb.toString();
                }
            }

        }catch(Exception e)
        {
            //Cache Data

            Log.e(ConstantUtils.TAG, "\nError: " + e.getMessage()
                    + "\nURL: " + url
                    + "\nMethod: genericPOST"
                    + "\nCreatedTime: " + DTUtils.getCurrentDateTime());
        }finally
        {
            if(connection != null)
            {
                connection.disconnect();
                connection = null;
            }
        }

        return toReturn;
    }

    /*private static String getUrl(String key)
    {
        String toReturn = key;

        urlMap = new HashMap<>();
        //urlMap.put(StringUtils.SERVER_URL + "/rest/truckDriver/validDriverLogin", "http://www.mocky.io/v2/5eb3d24a3200007c477b8b51");
        //urlMap.put(StringUtils.SERVER_URL + "/rest/mobile/currentJobv2/184", "http://www.mocky.io/v2/5ed4c5343300005f00f7a3da");
        //urlMap.put(StringUtils.SERVER_URL + "/rest/validationService/getJobValidations/918550", "http://www.mocky.io/v2/5eb975d72f00005f523c31c9");

        if(urlMap.containsKey(key))
        {
            toReturn = urlMap.get(key);
        }

        return toReturn;
    }*/


}
