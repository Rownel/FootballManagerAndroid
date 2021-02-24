package cz.utb.fai.footballmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class SecondActivityFinalSaving extends AppCompatActivity {

    private static final String FILE_NAME = "FavoriteTeamsList.txt";
    private RequestQueue mQueue;
    String jsonArrayStr;
    ImageView imageView;
    String code;
    SecondActivityFinalSaving context = this;
    Picasso picasso;
    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_final_saving);


        Toolbar toolbar = findViewById(R.id.toolbarSaveFvTeam);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        imageView = findViewById(R.id.teamLogo2);

        Bundle b = getIntent().getExtras();

        jsonArrayStr = b.getString("arraySend2");
        URL = b.getString("arraySend2TeamLogo");

        //setupPicasso();

        String url = b.getString("arraySend2TeamLogo");

        GlideUrl glideUrl = new GlideUrl(url,
                new LazyHeaders.Builder()
                        .addHeader("X-Auth-Token", "628254539678490994a50ca761f4109b")
                        .addHeader("content-type", "application/json")
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(imageView);


        try {

            JSONObject team = new JSONObject(jsonArrayStr);

            //String logo = team.getString("crestUrl");
            code = team.getString("id");
            String name = team.getString("name");
            String founded = team.getString("founded");
            String colors = team.getString("clubColors");
            String venue = team.getString("venue");

            //URL url = new URL(logo);
            //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            //imageView.setImageBitmap(bmp);



            //ImageView teamCrest = (ImageView) findViewById(R.id.teamLogo2);
            //teamCrest.setImageBitmap(bmp);

            TextView teamName = (TextView) findViewById(R.id.TeamName2);
            teamName.setText(name);
            TextView teamFounded = (TextView) findViewById(R.id.Founded2);
            teamFounded.setText(founded);
            TextView teamColors = (TextView) findViewById(R.id.ClubColors2);
            teamColors.setText(colors);
            TextView teamVenue = (TextView) findViewById(R.id.Venue2);
            teamVenue.setText(venue);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        //jsonParse(code);

        //mQueue = Volley.newRequestQueue(getApplicationContext());

    }

    public void cancelEverything(View view) {
        /*
        try {
            FileOutputStream fOut = openFileOutput("file name here",MODE_WORLD_READABLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/



        if (isFilePresent(FILE_NAME))
        {

            String fileContent;
            String finalContent;

            fileContent = readFile();
            finalContent = fileContent + "," + jsonArrayStr;
            writeToFile(finalContent);
        }
        else
        {
            writeToFile(jsonArrayStr);
        }



        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }


    private void jsonParse(String code)
    {

        String url = "https://api.football-data.org/v2/teams/"+code;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String crestUrl = response.getString("crestUrl");

                            URL url = new URL(crestUrl);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            imageView.setImageBitmap(bmp);


                        } catch (JSONException | IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", "628254539678490994a50ca761f4109b");
                params.put("content-type", "application/json");

                return params;
            }
        };

        mQueue.add(request);

//        startActivity(intent);

    }




    public boolean isFilePresent(String fileName) {
        String path = getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);

        return file.exists() && file.length() != 0;

        //return file.exists();
    }


    private void writeToFile(String data) {

        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }

    private String readFile() {


        String ret = "";

        try {
            InputStream inputStream = getBaseContext().openFileInput(SecondActivityFinalSaving.FILE_NAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }

        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void setupPicasso()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request newRequest = chain.request().newBuilder()
                                .addHeader("X-Auth-Token", "628254539678490994a50ca761f4109b")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        //Picasso.get().load(url).into(imageView);

        picasso.load(URL).into(imageView);

    }



}