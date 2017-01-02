package malawi_music.com.entrytest.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import malawi_music.com.entrytest.R;
import malawi_music.com.entrytest.app.MyApplication;
import malawi_music.com.entrytest.helper.Movie;
import malawi_music.com.entrytest.helper.SwipeListAdapter;

// A class which inherits its properties and methods from the ActionBarActivity and uses methods of the
// SwipeRefreshLayout.OnRefreshListener class
public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener
{

    // declared String constant which gets the Class' name.
    private String TAG = MainActivity.class.getSimpleName();

    // declared FirebaseAnalytics variable which can only be accessed within this class.
    private FirebaseAnalytics mFirebaseAnalytics;

    // declared String constant which is assigned to a link URL.
    private String URL_TOP_250 = "http://api.androidhive.info/json/imdb_top_250.php?offset=";

    // declared SwipeRefreshLayout variable which can only be accessed within this Class.
    private SwipeRefreshLayout swipeRefreshLayout;

    // declared ListView variable which can only be accessed within this Class
    private ListView listView;

    // declared SwipeListAdapter  variable.
    private SwipeListAdapter adapter;

    // declared generic List variable of type Movie.
    private List<Movie> movieList;

    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Type Casting the id found in the Resource folder to a ListView type and assigning it to
        // the listView variable declared in this class.
        listView = (ListView) findViewById(R.id.listView);

        // Type Casting the id found in the Resource folder to a ListView type and assigning it to
        // the listView variable declared in this class.
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // Creating a new Array List object and assigning it to the movieList variable.
        movieList = new ArrayList<>();

        // Creating a new SwipeListAdpater Object and assigning it to the adapter variable.
        adapter = new SwipeListAdapter(this, movieList);

        // Populating the listView.
        listView.setAdapter(adapter);

        // Used to keep track of Click Events found on the listView.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            // Used to keep track of individual List Item click events.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // finds TextView with the title id and assigns that text to the TextView variable.
                TextView text = (TextView) view.findViewById(R.id.title);

                // gets the text found in the TextView attribute and assigns it a String variable.
                String name = text.getText().toString();

                // Creating a new bundle Object and assigning it Bundle variable.
                Bundle bundle = new Bundle();

                // adding the text found in the list view to a bundle.
                bundle.putString("movie_title", name);

                // logs the bundle to firebase analysis.
                mFirebaseAnalytics.logEvent("movie_title", bundle);

            }
        });

        // sets a Refresh listner on this class.
        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
                {
                    // Starts the refresher.
                    swipeRefreshLayout.setRefreshing(true);

                    // retrieves the movie list.
                    fetchMovies();
                }
        }

        );

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh()
    {
        fetchMovies();
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchMovies()
    {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        // appending offset to url
        String url = URL_TOP_250 + offSet;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        // Logcat output which shows the response as a String
                        Log.d(TAG, response.toString());

                        if (response.length() > 0)
                        {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++)
                            {
                                try
                                {

                                    // geting a Movie Object from the json array, and assigning it
                                    // to JSONObject variable.
                                    JSONObject movieObj = response.getJSONObject(i);

                                    // getting the rank of the Movie Object and assigning that value
                                    // to an int variable.
                                    int rank = movieObj.getInt("rank");

                                    // getting the title of a Movie object and assigning that value
                                    // to a Striing variable.
                                    String title = movieObj.getString("title");

                                    // Creating a new movie Object and assigning it to a Movie variable
                                    Movie m = new Movie(rank, title);

                                    // adding Movie to the list, at the first position.
                                    movieList.add(0, m);

                                    // updating offset value to highest value
                                    if (rank >= offSet)
                                        offSet = rank;

                                } catch (JSONException e)
                                {
                                    // Throw exception when i less that 0.
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Logcat error handling.
                Log.e(TAG, "Server Error: " + error.getMessage());

                // Displays an error message on the screen.
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

}
