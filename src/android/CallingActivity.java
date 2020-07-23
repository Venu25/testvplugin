package com.gae.scaffolder.plugin;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.heka.android.R;

import org.json.JSONObject;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import android.widget.ImageView;
import io.socket.emitter.Emitter;


public class CallingActivity extends AppCompatActivity {
    
	String userID;
    String docUserId;
	Timer timer;


    MediaPlayer mediaPlayer;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calling);

        Window oWindow = this.getWindow();
        oWindow.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		final String session = getIntent().getStringExtra("session");
        final String token = getIntent().getStringExtra("token");
        int docNameId = getResources().getIdentifier("textView5", "id", getPackageName());
        TextView docBtnText = (TextView)findViewById(docNameId);
        docBtnText.setText("Dr."+getIntent().getStringExtra("docName"));
        userID=getIntent().getStringExtra("userId");
        docUserId=getIntent().getStringExtra("docUserId");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
           mediaPlayer.start();
		   
		 startTimer();
		 addSocket();
			 
		SocketService.getSocket().on("beforereject", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                mediaPlayer.stop();
                CallingActivity.this.finish();
            }
        });
        //        //add Clickhandler for reject buttton
        int acceptButtonId = getResources().getIdentifier("accept", "id", getPackageName());
        ImageView acceptButton = (ImageView)findViewById(acceptButtonId);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				timer.cancel();
                mediaPlayer.stop();
                Intent intent = new Intent(getApplicationContext(),VideoCallActivity.class);
                intent.putExtra("session",session);
                intent.putExtra("token",token);
                intent.putExtra("docUserId",docUserId);
                startActivity(intent);
				  try {
                    SocketService.getSocket().emit("calllifted",new JSONObject("{\"userId\":"+docUserId+"}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CallingActivity.this.finish();
            }
        });

        //        //add Clickhandler for accept buttton
        int rejectButtonId = getResources().getIdentifier("reject", "id", getPackageName());
        ImageView rejectButton = (ImageView) findViewById(rejectButtonId);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				 timer.cancel();
                mediaPlayer.stop();
				try {
                    SocketService.getSocket().emit("missed",new JSONObject("{\"userId\":"+docUserId+"}"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CallingActivity.this.finish();
            }
        });
		
   }
   
    public void addSocket() {
        try {
            SSLContext mySSLContext = SSLContext.getInstance("TLS");
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }
            } };
            HostnameVerifier myHostnameVerifier = new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            X509TrustManager manager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            mySSLContext.init(null, trustAllCerts, null);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().hostnameVerifier(myHostnameVerifier)
                    .sslSocketFactory(mySSLContext.getSocketFactory(), manager).build();

            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
  			SocketService.setSocket( IO.socket("https://hekasolutions.in", opts));
            //SocketService.setSocket( IO.socket("https://140.82.10.102", opts));
            SocketService.getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    try {
                        Log.w("Userid", userID);
                        SocketService.getSocket().emit("login", new JSONObject("{\"userId\":" + userID + ",\"isGlucostatus\":false}"));
                         SocketService.getSocket().emit("ringing",new JSONObject("{\"userId\":"+docUserId+"}"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // socket.disconnect();
                    Log.w("Socket", "Socket connected");
                }

            });
            SocketService.getSocket().connect();
        } catch (Exception e) {
            Log.e("Socket connectionError", e.getMessage());
        }
    }
	public void startTimer(){
        timer=new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               mediaPlayer.stop();
                               try {
                                   SocketService.getSocket().emit("missed",new JSONObject("{\"userId\":"+docUserId+"}"));
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                               CallingActivity.this.finish();
                         }
               },30000);
    }
}
