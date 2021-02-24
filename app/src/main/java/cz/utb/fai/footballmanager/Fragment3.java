package cz.utb.fai.footballmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Fragment3 extends Fragment {

    private static final String FILE_NAME = "FavoriteTeam.txt";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout,
                container, false);

        //Objects.requireNonNull(getActivity()).getActionBar().setDisplayHomeAsUpEnabled(true);

        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        //openChangeFvTeamActivity

        //GlideTo

        Button addButton = (Button) view.findViewById(R.id.addFvTeam);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAddFvTeamActivity();
            }
        });


        Button changeButton = (Button) view.findViewById(R.id.chgFvTeam);
        changeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openChangeFvTeamActivity();
            }
        });


        Button button = (Button) view.findViewById(R.id.openWebsiteBtn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openClubWebsite(v);
            }
        });





        try {

            if (isFilePresent(FILE_NAME))
            {
                String fileContent = readFile();

                JSONObject team = new JSONObject(fileContent);


                //String logo = team.getString("crestUrl");
                String name = team.getString("name");
                String founded = team.getString("founded");
                String colors = team.getString("clubColors");
                String venue = team.getString("venue");

                //URL url = new URL(logo);
                //Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //imageView.setImageBitmap(bmp);

                //ImageView teamCrest = (ImageView) view.findViewById(R.id.teamLogo2);




                //teamCrest.setImageBitmap(bmp);

                TextView teamName = (TextView) view.findViewById(R.id.ClubName);
                teamName.setText(name);
                TextView teamFounded = (TextView) view.findViewById(R.id.Founded);
                teamFounded.setText(founded);
                TextView teamColors = (TextView) view.findViewById(R.id.ClubColors);
                teamColors.setText(colors);
                TextView teamVenue = (TextView) view.findViewById(R.id.Venue);
                teamVenue.setText(venue);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }






        return view;

    }

    public void openClubWebsite(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.seznam.cz/"));
        startActivity(browserIntent);
    }

    public void openAddFvTeamActivity()
    {
        Intent intent = new Intent(getActivity(), SecondActivityFvTeamLeague.class);
        startActivity(intent);
    }

    public void openChangeFvTeamActivity()
    {
        Intent intent = new Intent(getActivity(), ChangeFvTeamActivity.class);
        startActivity(intent);
    }


    public boolean isFilePresent(String fileName) {
        String path = getActivity().getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);

        return file.exists() && file.length() != 0;

        //return file.exists();
    }


    private String readFile() {


        String ret = "";

        try {
            InputStream inputStream = getContext().openFileInput(Fragment3.FILE_NAME);

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


}
