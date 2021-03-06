package v3.estruturart.com.br.estruturaart.service;

import android.content.Context;
import android.os.StrictMode;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.codec.binary.Base32;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import v3.estruturart.com.br.estruturaart.R;
import v3.estruturart.com.br.estruturaart.model.OAuth;
import v3.estruturart.com.br.estruturaart.model.Orcamento;
import v3.estruturart.com.br.estruturaart.model.TbUsuario;
import v3.estruturart.com.br.estruturaart.utility.GsonDeserializeExclusion;

public class Client
{
    private String url;
    private boolean hasError = false;
    private String message = "";
	private TbUsuario usuario;
    private String json;
    private Context ctx;
    private Map<String, String> parameter = new HashMap<>();
    private String ip = "";
    public static final MediaType FORMURLENCODED = MediaType.parse(
            "application/x-www-form-urlencoded; charset=utf-8"
    );

    public Client(Context ctx, String ip)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.ctx = ctx;
        this.url = String.format(ctx.getString(R.string.ws_host), ip);
        this.ip = ip;
    }

    public Object fromGet(String action, Type type) {
        try {
            OkHttpClient client2 = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(this.url + action)
                    .addHeader("Authentication", getAuth())
                    .build();

            Response responseO = client2.newCall(request).execute();
            this.json = responseO.body().string();

            switch (responseO.code()) {
                case 200:
                    break;
                default:
                    throw new IOException(
                        "Erro ao chamar o web service. Codigo: "
                        + responseO.code()
                        + responseO.message()
                    );
            }
        } catch (MalformedURLException ex) {
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        } catch (ProtocolException ex) {
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        } catch (ConnectException ex) {
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        } catch (Exception ex) {
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        }

        Gson gson = new Gson();
        try {
            if (!this.json.equals("")) {
                return gson.fromJson(this.json, type);
            }
        } catch (NullPointerException ex) {
            this.hasError = true;
            this.message = ex.getMessage();

        } catch (Exception ex) {
            this.hasError = true;
            this.message = ex.getMessage();
        }

        try {
            String tentativa2 = type.toString();
            System.out.println("DYNAMIC INSTANCE: " + tentativa2.replace("class ", ""));

            this.message = "Sistema temporariamente indisponível. Tente novamente mais tarde!";
            if (tentativa2.matches("(.*)ArrayList(.*)")) {
                return new ArrayList<Object>();
            }

            Class cls = Class.forName(tentativa2.replace("class ", "").trim());
            return cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return new Object();
    }

    public Object fromPost(String action, Type type) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("MMM d, yyyy").create();
        try {
            OkHttpClient client2 = new OkHttpClient();
            RequestBody body = RequestBody.create(FORMURLENCODED, getParametersToString());
            Request request = new Request.Builder()
                    .url(this.url + action)
                    .addHeader("Authentication", getAuth())
                    .post(body)
                    .build();

            Response responseO = client2.newCall(request).execute();
            this.json = responseO.body().string();

            switch (responseO.code()) {
                case 200:
                    break;
                default:
                    throw new IOException(
                            "Erro ao chamar o web service. Codigo: "
                                    + responseO.code()
                                    + responseO.message()
                    );
            }
        } catch(MalformedURLException ex){
            System.out.println("EXCEPTION1 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        } catch(ProtocolException ex){
            System.out.println("EXCEPTION2 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        } catch(ConnectException ex){
            System.out.println("EXCEPTION3 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        } catch(Exception ex){
            System.out.println("EXCEPTION4 => " + ex.getMessage());
            this.hasError = true;
            this.message = ex.getMessage();
        }
        try {
            if (!this.json.equals("")) {
                return gson.fromJson(this.json, type);
            }
        } catch (JsonSyntaxException ex) {
            System.out.println("ddd\n\n\n\n\ndsds455");
            ex.printStackTrace();
            this.hasError = true;
            this.message = ex.getMessage();
            System.out.println("\n\n\n\n\n\nds");

        } catch (Exception ex) {
            System.out.println("\n\n\n\n\n\ndsds1");
            ex.printStackTrace();
            this.hasError = true;
            this.message = ex.getMessage();
            System.out.println("\n\n\n\n\n\ndsds");
        }

        try {
            String tentativa2 = type.toString();
            System.out.println("DYNAMIC INSTANCE: " + tentativa2.replace("class ", ""));

            this.message = "Sistema temporariamente indisponível. Tente novamente mais tarde!";
            if (tentativa2.matches("(.*)ArrayList(.*)")) {
                return new ArrayList<Object>();
            }

            Class cls = Class.forName(tentativa2.replace("class ", "").trim());
            return cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new Object();
    }

    private String toString(HttpURLConnection client) throws IOException
    {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        String response = null;

        for (String line; (line = br.readLine()) != null; ) {
            sb.append(line + "\n");
        }

        try {
            response = sb.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    public String getJson()
    {
        return this.json;
    }

    public String getParametersToString()
    {
        String paramString = "";
        for (Map.Entry<String, String> param : getParameter().entrySet()) {
            paramString += param.getKey() + "=" + param.getValue() + "&";
        }

        if (paramString.length() > 0) {
            return paramString.substring(0, paramString.length() - 1);
        }

        return paramString;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public boolean hasError() {
        return this.hasError;
    }

    public String getMessage() {
        return this.message;
    }
	
	public void setAuth(TbUsuario usuario) {
		this.usuario = usuario;
	}

	public String getAuth() {
		if (usuario != null) {
			OAuth oAuth = new OAuth();
			oAuth.setEmail(usuario.getEmail());
			oAuth.setSenha(usuario.getSenha());
			Gson gson = new Gson();
            Base32 b2 = new Base32();
			return b2.encodeToString(gson.toJson(oAuth).getBytes());
		} else {
			return "";
		}
	}
}
