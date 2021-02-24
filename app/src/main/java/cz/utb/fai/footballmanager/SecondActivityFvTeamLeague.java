package cz.utb.fai.footballmanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecondActivityFvTeamLeague extends AppCompatActivity {

    ListView listView;


    ArrayList<String> listSource3 = new ArrayList<String>();

    private RequestQueue mQueue;
    Map<String, String> soutez = new HashMap<String, String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_fv_team_league);

        Toolbar toolbar = findViewById(R.id.toolbarLeague);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Soutěž");

        listSource3.add("La Liga");
        listSource3.add("BundesLiga");
        listSource3.add("Premier League");
        listSource3.add("Ligue 1");
        listSource3.add("Eredivise");
        listSource3.add("Primeira Liga");

        soutez.put("La Liga","2014");
        soutez.put("BundesLiga","2002");
        soutez.put("Premier League","2021");
        soutez.put("Serie A","2019");
        soutez.put("Ligue 1","2015");
        soutez.put("Eredivise","2003");
        soutez.put("Primeira Liga","2017");
        soutez.put("Serie A (Brazil)","2013");



        mQueue = Volley.newRequestQueue(getApplicationContext());



        listView = (ListView) findViewById(R.id.listViewMyLeague);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listSource3);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedLeague = listView.getItemAtPosition(position).toString();

                if (soutez.containsKey(selectedLeague))
                {
                    String code = soutez.get(selectedLeague);
                    jsonParse(code);
                }



            }
        });


    }








    private void jsonParse(String code)
    {

        String url = "https://api.football-data.org/v2/competitions/"+code+"/teams";

        Intent intent = new Intent(SecondActivityFvTeamLeague.this, SecondActivityAddFvTeam.class);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray2 = response.getJSONArray("teams");

                            Bundle b = new Bundle();

                            b.putString("arraySend",jsonArray2.toString());
                            intent.putExtras(b);

                            //teamsOfLeague.add(jsonArray);
                            /*
                            for (int j = 0; j < jsonArray2.length(); j++)
                            {

                                JSONObject teams = jsonArray2.getJSONObject(j);

                                b.putString("arraySend",teams.toString());
                                intent.putExtras(b);


                                //intent.putExtra("arraySend",teams.toString());

                                //String teamName = teams.getString("name");
                                //String teamID = teams.getString("id");

                                //teamsOfLeague.add(teams);
                                //teamsOfLeagueID.add(teamID);

                            }
                            */

                            startActivity(intent);
                            //Bundle bundle = new Bundle();
                            //bundle.putStringArrayList("teamsName",teamsOfLeague);
                            //bundle.putStringArrayList("teamsID",teamsOfLeagueID);

                            //intent.putExtra("teamsID",teamsOfLeagueID);

                        } catch (JSONException e)
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}