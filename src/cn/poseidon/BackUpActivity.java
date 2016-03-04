package cn.poseidon;

import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;

import cn.poseidon.domain.ContactInfo;
import cn.poseidon.engine.UpdateInfoParser;

import com.example.poseidon.R;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class BackUpActivity extends Activity{
	private static String path="http://192.168.191.4:8080/test/test.xml";
	private ContactInfo contactInfo;
	private TextView test_tx;
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.service_test);
		
		
		test_tx= (TextView)findViewById(R.id.textView);
		

		getPDAServerData(path);
		
		test_tx.setText(contactInfo.getName().toString());
		
		}
	private void getPDAServerData(String urlString)
	{
		//HttpClient client = new DefaultHttpClient();
		//HttpPost request;
        try {
            URL url = new URL(urlString);
		    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
		    httpURLConnection.setConnectTimeout(5000);
		    httpURLConnection.setRequestMethod("GET");
		    InputStream is = httpURLConnection.getInputStream();
		    if (httpURLConnection.getResponseCode()==200) {
		    	contactInfo.setName(UpdateInfoParser.getUpdateInfo(is).getName());
			    System.out.println("GET SUCCESSEDDDDDD");
			}
		    
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

			

	}

}
