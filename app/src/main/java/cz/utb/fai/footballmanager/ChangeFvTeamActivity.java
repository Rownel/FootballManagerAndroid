package cz.utb.fai.footballmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeFvTeamActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> teamsOfLeague = new ArrayList<String>();
    Map<String, String> teamsInfo = new HashMap<String, String>();
    private String finalContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_fv_team);


        Toolbar toolbar = findViewById(R.id.toolbarFvTeam);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Oblíbený Tým");
        }



        try {

            if (isFilePresent("FavoriteTeamsList.txt"))
            {
                String fileContent = readFile();
                finalContent = "["+ fileContent +"]";

                JSONArray jsonarray = new JSONArray(finalContent);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject team = jsonarray.getJSONObject(i);

                    String teamName = team.getString("name");

                    teamsOfLeague.add(teamName);

                    teamsInfo.put(team.getString("name"),team.getString("id"));

                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = (ListView) findViewById(R.id.listViewMyFvTeam);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teamsOfLeague);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ChangeFvTeamActivity.this, SaveFvTeamActivity.class);

                String selectedTeam = listView.getItemAtPosition(position).toString();

                if (teamsInfo.containsKey(selectedTeam))
                {
                    String code = teamsInfo.get(selectedTeam);

                    try {

                        JSONArray jsonarray = new JSONArray(finalContent);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject team = jsonarray.getJSONObject(i);

                            String teamID = team.getString("id");

                            if (teamID.equals(code))
                            {
                                Bundle b = new Bundle();

                                b.putString("arraySend3",team.toString());
                                b.putString("teamLogo3",team.getString("crestUrl"));
                                intent.putExtras(b);
                                break;
                            }

                            //String name = jsonobject.getString("name");
                            //String url = jsonobject.getString("url");
                        }

                        startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
        });


    }


    private String readFile() {


        String ret = "";

        try {
            InputStream inputStream = getBaseContext().openFileInput("FavoriteTeamsList.txt");

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

    public boolean isFilePresent(String fileName) {
        String path = getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);

        return file.exists() && file.length() != 0;

        //return file.exists();
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