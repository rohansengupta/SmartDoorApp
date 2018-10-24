package com.example.hp.smartdoor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ImageView mimageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    //private String url = "http://www.mocky.io/v2/5bce15802f00002c00c856f8";
    //private String url = "http://192.168.56.1:3000/upload";
    private String url = "http://10.42.0.171:3000/upload";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mimageView = (ImageView) findViewById(R.id.imageView);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void takePicture(View view) {
        Intent imageTakenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakenIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageTakenIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mimageView.setImageBitmap(imageBitmap);
            //sendRequest(imageBitmap);
            try {
                sendPhoto(imageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /////////////////////
    private void sendPhoto(Bitmap bitmap) throws Exception {
        new UploadTask().execute(bitmap);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
        String str="";
        protected Void doInBackground(Bitmap... bitmaps) {
            if (bitmaps[0] == null)
                return null;
            setProgress(0);

            Bitmap bitmap = bitmaps[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
            InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                HttpPost httppost = new HttpPost(
                        "http://192.168.43.69:3000/upload"); // server

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("file",
                        System.currentTimeMillis() + ".jpg", in);
                httppost.setEntity(reqEntity);

               // Log.i(TAG, "request " + httppost.getRequestLine());
                HttpResponse response = null;
                AlertDialog.Builder builder=null;

                try {
                    response = httpclient.execute(httppost);
                    Log.i(TAG, "response :--->" +response);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    if (response != null){
                     //   Log.i(TAG, "response " + response.getStatusLine().toString());
                    // printing the response
                        Log.i(TAG,"NOt NULL  Response1");
                        builder = new AlertDialog.Builder(MainActivity.this);
                        Log.i(TAG,"NOt NULL  Response2");
                    builder.setCancelable(true);
                        Log.i(TAG,"NOt NULL  Response3");
                    builder.setTitle("Title");
                        Log.i(TAG,"NOt NULL  Response4");

                        HttpEntity entity = response.getEntity();
                         str = EntityUtils.toString(entity);

                        Log.i(TAG, "response :==>" +str);
                        Log.i(TAG,"NOt NULL  Response5");
                        //  Toast.makeText(MainActivity.this, str  , Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                 //   builder.setMessage(str);
                  //  builder.show();

                }
            } finally {

            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(str.indexOf("True")!= -1 )
            Toast.makeText(MainActivity.this, "Verified,Welcome!" , Toast.LENGTH_LONG).show();
            else
                Toast.makeText(MainActivity.this, "Not-Authorized" , Toast.LENGTH_LONG).show();

        }
    }
    ////////////////////

    /*
    private void sendRequest(final Bitmap imageBitmap ) {
        mRequestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response :" + response.toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Title");
                builder.setMessage(response.toString());
                builder.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error : " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                String images = getStringImage(imageBitmap);
                Log.i("Mynewsam", "" + images);
                param.put("image", images);
                return param;
            }
        };
        mRequestQueue.add(stringRequest);
    }
*/
    /*public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }*/
}
