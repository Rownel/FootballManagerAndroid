package cz.utb.fai.footballmanager;

import android.app.VoiceInteractor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.utb.fai.footballmanager.ui.main.SectionsPagerAdapter;

public class Fragment1 extends Fragment{

    private RequestQueue mQueue;

    private TableLayout tableStandings;
    Map<String, String> soutez = new HashMap<String, String>();
    private boolean isCreated = false;

    private String text17;


    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog(this.getActivity());
        exampleDialog.show(getFragmentManager(), "example dialog");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_layout, container, false);

        tableStandings = (TableLayout) view.findViewById(R.id.TableStandings);

       // Map<String, String> soutez = new HashMap<String, String>();
        soutez.put("La Liga","2014");
        soutez.put("BundesLiga","2002");
        soutez.put("Premier League","2021");
        soutez.put("Serie A","2019");
        soutez.put("Ligue 1","2015");
        soutez.put("Eredivise","2003");
        soutez.put("Primeira Liga","2017");
        soutez.put("Serie A (Brazil)","2013");



        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());


        Button button2 = (Button) view.findViewById(R.id.btnGetApi);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Spinner mySpinner = (Spinner) view.findViewById(R.id.aSpinner);
                String liga = mySpinner.getSelectedItem().toString();

                if (soutez.containsKey(liga))
                {
                    String code = soutez.get(liga);

                    if (isCreated)
                    {
                        tableStandings.removeAllViews();
                        isCreated = false;
                    }
                    isCreated = true;
                    jsonParse(code);


                }

            }
        });





        return view;


        //   return inflater.inflate(R.layout.fragment1_layout,container,false);
    }


    private void jsonParse(String code)
    {

        String url = "https://api.football-data.org/v2/competitions/"+code+"/standings";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("standings");

                            JSONObject standings = jsonArray.getJSONObject(0);

                            JSONArray table = standings.getJSONArray("table");

                            for (int i = 0; i < table.length(); i++)
                            {


                                JSONObject teams = table.getJSONObject(i);

                                JSONObject club = teams.getJSONObject("team");
                                String clubName = club.getString("name");
                                //String clubLogo = club.getString("crestUrl");

                                String position = teams.getString("position");
                                String won = teams.getString("won");
                                String lost = teams.getString("lost");
                                String draw = teams.getString("draw");
                                String points = teams.getString("points");

                                String goalsFor = teams.getString("goalsFor");
                                String goalsAgainst = teams.getString("goalsAgainst");

                                String score = goalsFor + ":" + goalsAgainst;


                                TableRow newRow = new TableRow(Objects.requireNonNull(getActivity()).getApplicationContext());

                                TextView standing1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                standing1.setText(position);
                                standing1.setTextColor(Color.BLACK);
                                standing1.setGravity(Gravity.CENTER);
                                standing1.setBackgroundColor(Color.GREEN);
                                standing1.setMaxLines(2);
                                standing1.setPadding(5,5,5,5);
                                newRow.addView(standing1);


                                TextView teamName1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                //teamName1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                teamName1.setMaxWidth(220);
                                teamName1.setText(clubName);
                                teamName1.setTextColor(Color.BLACK);
                                teamName1.setBackgroundColor(Color.YELLOW);
                                teamName1.setGravity(Gravity.START);
                                teamName1.setMaxLines(40);
                                teamName1.setPadding(5,5,2,5);
                                teamName1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                newRow.addView(teamName1);


                                TextView teamWins1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                //teamName1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                teamWins1.setText(won);
                                teamWins1.setTextColor(Color.BLACK);
                                teamWins1.setBackgroundColor(Color.BLUE);
                                teamWins1.setGravity(Gravity.CENTER);
                                teamWins1.setMaxLines(2);
                                teamWins1.setPadding(5,5,5,5);
                                teamWins1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                newRow.addView(teamWins1);


                                TextView teamLoses1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                teamLoses1.setText(lost);
                                teamLoses1.setTextColor(Color.BLACK);
                                teamLoses1.setBackgroundColor(Color.CYAN);
                                teamLoses1.setGravity(Gravity.CENTER);
                                teamLoses1.setMaxLines(2);
                                teamLoses1.setPadding(5,5,5,5);
                                teamLoses1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                newRow.addView(teamLoses1);


                                TextView teamTies1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                teamTies1.setText(draw);
                                teamTies1.setTextColor(Color.BLACK);
                                teamTies1.setBackgroundColor(Color.LTGRAY);
                                teamTies1.setGravity(Gravity.CENTER);
                                teamTies1.setMaxLines(2);
                                teamTies1.setPadding(5,5,5,5);
                                teamTies1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                newRow.addView(teamTies1);


                                TextView teamPoints1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                teamPoints1.setText(points);
                                teamPoints1.setTextColor(Color.BLACK);
                                teamPoints1.setGravity(Gravity.CENTER);
                                teamPoints1.setBackgroundColor(Color.RED);
                                teamPoints1.setMaxLines(2);
                                teamPoints1.setPadding(10,5,10,5);
                                teamPoints1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                newRow.addView(teamPoints1);


                                TextView teamScore1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                teamScore1.setText(score);
                                teamScore1.setTextColor(Color.BLACK);
                                teamScore1.setBackgroundColor(Color.MAGENTA);
                                teamScore1.setGravity(Gravity.CENTER);
                                teamScore1.setMaxLines(2);
                                teamScore1.setPadding(10,5,10,5);
                                teamScore1.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                newRow.addView(teamScore1);


                                tableStandings.addView(newRow);

                            }



                            /*
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject employee = jsonArray.getJSONObject(i);

                                String firstName = employee.getString("firstName");
                                String age = employee.getString("age");
                                String mail = employee.getString("mail");

                            }*/

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


    }


    public void setViewLayoutParams(View view, int width, int height, Gravity gravity, String text, Color textColor, Layout.Alignment align, int minLines)
    {

        view.setMinimumWidth(width);
        view.setMinimumHeight(height);


    }

    public void setParentViewLayoutParams(View view, int width, int height)
    {

        view.setMinimumWidth(width);
        view.setMinimumHeight(height);


    }




}
