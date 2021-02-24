package cz.utb.fai.footballmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Headers;

public class SaveFvTeamActivity extends AppCompatActivity {

    private static final String FILE_NAME = "FavoriteTeam.txt";
    private String jsonArrayStr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_fv_team);


        Toolbar toolbar = findViewById(R.id.toolbarChangeFvTeam);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }




        Bundle b = getIntent().getExtras();

        jsonArrayStr = b.getString("arraySend3");

        ImageView imageView = findViewById(R.id.teamLogo3);
        String url = b.getString("teamLogo3");

        //Picasso.get().load(url).into(imageView);

        GlideUrl myUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("X-Auth-Token", "628254539678490994a50ca761f4109b")
                .build());

        Glide.with(SaveFvTeamActivity.this)
                .load(myUrl)
                .into(imageView);


        try {

            JSONObject team = new JSONObject(jsonArrayStr);

            //String logoUrl = team.getString("crestUrl");
            String crestUrl = team.getString("crestUrl");
            String name = team.getString("name");
            String founded = team.getString("founded");
            String colors = team.getString("clubColors");
            String venue = team.getString("venue");

            //URL url = new URL(logo);
            //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            //imageView.setImageBitmap(bmp);

            //Picasso.get().load(crestUrl).into(imageView);



            TextView teamName = (TextView) findViewById(R.id.TeamName3);
            teamName.setText(name);
            TextView teamFounded = (TextView) findViewById(R.id.Founded3);
            teamFounded.setText(founded);
            TextView teamColors = (TextView) findViewById(R.id.ClubColors3);
            teamColors.setText(colors);
            TextView teamVenue = (TextView) findViewById(R.id.Venue3);
            teamVenue.setText(venue);


        } catch (JSONException e) {
            e.printStackTrace();
        }

/*

        Glide.with(SaveFvTeamActivity.this)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageView);
        */

    }


    public void cancelAndSaveFvTeam(View view) {

        writeToFile(jsonArrayStr);


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);

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


}