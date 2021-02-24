 package cz.utb.fai.footballmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;

 public class SecondActivityAddFvTeam extends AppCompatActivity {

    private MaterialSearchView searchView;

    ListView listView;

    ArrayList<String> teamsOfLeague = new ArrayList<String>();
    Map<String, String> teamsInfo = new HashMap<String, String>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_add_fv_team);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vyhledat Týmy");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().getSelectedNavigationIndex();
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));


        //Intent intent = getIntent();
        Bundle b = getIntent().getExtras();

        String jsonArrayStr = b.getString("arraySend");
        //teamsOfLeague.add(jsonArray);

        try {


            JSONArray jsonarray = new JSONArray(jsonArrayStr);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject team = jsonarray.getJSONObject(i);

                teamsOfLeague.add(team.getString("name"));

                teamsInfo.put(team.getString("name"),team.getString("id"));

                //String name = jsonobject.getString("name");
                //String url = jsonobject.getString("url");
            }

            if (isFilePresent("FavoriteTeamsList.txt"))
            {
                String fileContent = readFile();
                String finalContent = "["+ fileContent +"]";

                JSONArray jsonarray2 = new JSONArray(finalContent);
                for (int i = 0; i < jsonarray2.length(); i++) {
                    JSONObject team = jsonarray2.getJSONObject(i);

                    String teamName = team.getString("name");

                    for (int j = 0; j < jsonarray.length(); j++) {
                        JSONObject team2 = jsonarray.getJSONObject(j);

                        String teamName2 = team2.getString("name");

                        if (teamName.equals(teamName2))
                        {
                            teamsOfLeague.remove(teamName2);
                        }

                    }

                }



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView = (ListView) findViewById(R.id.listViewMy);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,teamsOfLeague);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SecondActivityAddFvTeam.this, SecondActivityFinalSaving.class);

                String selectedLeague = listView.getItemAtPosition(position).toString();

                if (teamsInfo.containsKey(selectedLeague))
                {
                    String code = teamsInfo.get(selectedLeague);

                    try {

                        JSONArray jsonarray = new JSONArray(jsonArrayStr);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject team = jsonarray.getJSONObject(i);

                            String teamID = team.getString("id");

                            if (teamID.equals(code))
                            {
                                Bundle b = new Bundle();

                                b.putString("arraySend2",team.toString());
                                b.putString("arraySend2TeamLogo",team.getString("crestUrl"));
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




        View cancelButton = (View) findViewById(R.id.cancelView);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                // If closed Search View, listView will return default

                listView = (ListView) findViewById(R.id.listViewMy);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SecondActivityAddFvTeam.this, android.R.layout.simple_list_item_1,teamsOfLeague);
                listView.setAdapter(adapter);

            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText != null && !newText.isEmpty())
                {
                    List<String> lastFound = new ArrayList<String>();
                    for (String item: teamsOfLeague){
                        if (item.contains(newText)){
                            lastFound.add(item);
                        }
                    }

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SecondActivityAddFvTeam.this, android.R.layout.simple_list_item_1,lastFound);
                    listView.setAdapter(adapter1);
                }
                else
                {
                    //if search text is null

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SecondActivityAddFvTeam.this, android.R.layout.simple_list_item_1,teamsOfLeague);
                    listView.setAdapter(adapter1);
                }

                return true;
            }

        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        MenuItem menuItem2 = menu.findItem(R.id.cancelBtn);
        searchView.setMenuItem(menuItem);
        searchView.setMenuItem(menuItem2);

        return true;
        //return super.onCreateOptionsMenu(menu);


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

}