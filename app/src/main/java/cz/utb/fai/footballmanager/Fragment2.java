package cz.utb.fai.footballmanager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Fragment2 extends Fragment {


    private RequestQueue mQueue;

    private TableLayout tableMatches;
    Map<String, String> soutez = new HashMap<String, String>();
    private boolean isCreated = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2_layout,
                container, false);


        tableMatches = (TableLayout) view.findViewById(R.id.TableMatches);

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


        Button button2 = (Button) view.findViewById(R.id.button1Match);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Spinner mySpinner = (Spinner) view.findViewById(R.id.aSpinner2);
                String liga = mySpinner.getSelectedItem().toString();

                if (soutez.containsKey(liga))
                {
                    String code = soutez.get(liga);

                    if (isCreated)
                    {
                        tableMatches.removeAllViews();
                        isCreated = false;
                    }
                    isCreated = true;
                    jsonParse(code);


                }

            }
        });


        return view;
    }



    private void jsonParse(String code)
    {

        String url = "https://api.football-data.org/v2/competitions/"+code+"/matches";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("ResourceAsColor")
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("matches");
                            int complet = 0;
                            int complet2 = 0;

                            for (int k = 0; k < jsonArray.length(); k++)
                            {
                                JSONObject matches = jsonArray.getJSONObject(k);
                                String status = matches.getString("status");
                                int matchDay =  matches.getInt("matchday");
                                JSONObject current =  matches.getJSONObject("season");
                                int currentMatchDay = current.getInt("currentMatchday");

                                if (status.equals("SCHEDULED") && matchDay >= currentMatchDay)
                                {
                                    complet =  matches.getInt("matchday");
                                    break;
                                }

                            }

                            int startDay = complet - 3;
                            int maxDay = complet + 1;   //jsonArray.length()-1 ;


                            for (int j = 0; j < jsonArray.length(); j++)
                            {
                                JSONObject matches = jsonArray.getJSONObject(j);

                                int fMatch =  matches.getInt("matchday");

                                if (fMatch == startDay)
                                {
                                    complet2 = j;
                                    break;
                                }

                            }

                            int lastDayMatch = 0;


                            for (int i = complet2; i < jsonArray.length(); i++)
                            {
                                JSONObject matches = jsonArray.getJSONObject(i);

                                String status = matches.getString("status");

                                String matchDay = matches.getString("matchday");

                                if (matchDay.equals(String.valueOf(maxDay+1)))
                                {
                                    break;
                                }
                                else
                                {

                                    String matchDayText = "MatchDay:";

                                    JSONObject home = matches.getJSONObject("homeTeam");
                                    JSONObject away = matches.getJSONObject("awayTeam");
                                    String homeTeam = home.getString("name");
                                    String awayTeam = away.getString("name");

                                    JSONObject score = matches.getJSONObject("score");
                                    JSONObject fullTime = score.getJSONObject("fullTime");
                                    String homeGoal = fullTime.getString("homeTeam");
                                    String awayGoal = fullTime.getString("awayTeam");

                                    String sep2 = "VS";

                                    String finalScore1 = homeGoal + " : " + awayGoal;

                                    if(!matchDay.equals(String.valueOf(lastDayMatch)))
                                    {
                                        lastDayMatch = Integer.parseInt(matchDay);

                                        TableRow headerRow = new TableRow(Objects.requireNonNull(getActivity()).getApplicationContext());
                                        headerRow.setBackgroundColor(R.color.tabActive);

                                        TextView matchday1 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                        matchday1.setText(matchDayText);
                                        matchday1.setTextColor(Color.BLACK);
                                        matchday1.setGravity(Gravity.CENTER);
                                        //matchday1.setBackgroundColor(Color.GREEN);
                                        matchday1.setMaxLines(2);
                                        matchday1.setPadding(5,9,5,9);
                                        headerRow.addView(matchday1);

                                        TextView matchday2 = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                        matchday2.setText(matchDay);
                                        matchday2.setTextColor(Color.BLACK);
                                        matchday2.setGravity(Gravity.CENTER);
                                        //matchday2.setBackgroundColor(Color.RED);
                                        matchday2.setMaxLines(2);
                                        matchday2.setPadding(5,9,5,9);
                                        headerRow.addView(matchday2);

                                        tableMatches.addView(headerRow);

                                    }



                                    TableRow contentRow = new TableRow(Objects.requireNonNull(getActivity()).getApplicationContext());

                                    TextView homeTeamName = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                    homeTeamName.setText(homeTeam);
                                    homeTeamName.setTextColor(Color.BLACK);
                                    homeTeamName.setGravity(Gravity.CENTER);
                                    //homeTeamName.setBackgroundColor(Color.YELLOW);
                                    homeTeamName.setMaxWidth(200);
                                    homeTeamName.setMaxLines(20);
                                    homeTeamName.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                    homeTeamName.setPadding(2,5,2,5);
                                    contentRow.addView(homeTeamName);



                                    if (status.equals("FINISHED"))
                                    {

                                        TextView finalScore = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                        finalScore.setText(finalScore1);
                                        finalScore.setTextColor(Color.BLACK);
                                        finalScore.setGravity(Gravity.CENTER);
                                        //finalScore.setBackgroundColor(Color.MAGENTA);
                                        finalScore.setMaxLines(2);
                                        finalScore.setPadding(5,5,5,5);
                                        contentRow.addView(finalScore);

                                    }
                                    else
                                    {

                                        TextView scoreBreaker = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                        scoreBreaker.setText(sep2);
                                        scoreBreaker.setTextColor(Color.BLACK);
                                        scoreBreaker.setGravity(Gravity.CENTER);
                                        //scoreBreaker.setBackgroundColor(Color.MAGENTA);
                                        scoreBreaker.setMaxLines(2);
                                        scoreBreaker.setPadding(5,5,5,5);
                                        contentRow.addView(scoreBreaker);


                                    }


                                    TextView awayTeamName = new TextView(Objects.requireNonNull(getActivity()).getApplicationContext());
                                    awayTeamName.setText(awayTeam);
                                    awayTeamName.setTextColor(Color.BLACK);
                                    awayTeamName.setGravity(Gravity.CENTER);
                                    //awayTeamName.setBackgroundColor(Color.YELLOW);
                                    awayTeamName.setMaxWidth(200);
                                    awayTeamName.setMaxLines(20);
                                    awayTeamName.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                    awayTeamName.setMaxLines(2);
                                    awayTeamName.setPadding(2,5,2,5);
                                    contentRow.addView(awayTeamName);


                                    tableMatches.addView(contentRow);


                                }


                            }

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









}
