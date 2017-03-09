package smart.app.githubsearch;


import info.androidhive.customlistviewvolley.app.AppController;
import info.androidhive.customlistviewvolley.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// Movies json url
	private static final String url = "https://api.github.com/search/users?q=location:lagos";
	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<Movie>();
	private ListView listView;
	private CustomListAdapter adapter;
	private Toolbar toolbar;
	AlertDialog dialog1;
	private Button share;
	int height1 = 1000;
	TextView profileurl,username;
	JSONArray ja_data;
	String browseurl,browseusername;
	JSONObject obj,obj1;
	NetworkImageView image;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		
		//setting up listview adapter
		listView = (ListView) findViewById(R.id.listView);
		adapter = new CustomListAdapter(this, movieList);
		listView.setAdapter(adapter);
		
		//instantiate appcontroller
		if (imageLoader == null)
		imageLoader = AppController.getInstance().getImageLoader();
		
		
		
       //instantiate progress dialog
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		//set action bar as tool bar
		toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_launcher);

        //instantiate action bar
        ActionBar ab = getSupportActionBar();
        
        		

		// Creating volley request for JSONObject
		JsonObjectRequest movieReq = new JsonObjectRequest(url,new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						
						//getting json array from json object
						
						try {
							ja_data = response.getJSONArray("items");
							Toast.makeText(getApplicationContext(),"Items recieved: "+ja_data.length()+" items", Toast.LENGTH_LONG).show();
						

						// Parsing json object from json array
						for (int i = 0; i < ja_data.length(); i++) {
							try {
								
							
                            //setting movie parameter from recieved json object items
								obj = ja_data.getJSONObject(i);
								Movie movie = new Movie();
								movie.setImage(obj.getString("avatar_url"));
								movie.setUsername(obj.getString("login"));
								movie.setProfileurl(obj.getString("html_url"));

								// adding movie to movies array
								movieList.add(movie);

							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
							}

						}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(), "Failed to get message, Refresh and try again!", Toast.LENGTH_LONG).show();
						hidePDialog();

					}
				});
		
		
		
		//setting listview click listener
		listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
            	
            	
            	//setting the Alertdialog
            	
            	AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this)
							 .setTitle("User's Profile")
							 .setCancelable(true);
					 
					 final AlertDialog alertdialog = builder1.create();
					 
					 LayoutInflater inflater = getLayoutInflater();
				        View dialoglayout = inflater.inflate(R.layout.profileview, null);
				        dialoglayout.setMinimumHeight(height1);
					 builder1.setView(dialoglayout);
					dialog1 = builder1.create();
					 
					 share = (Button)dialoglayout.findViewById(R.id.share);
					 image = (NetworkImageView)dialoglayout.findViewById(R.id.thumbnail);
					 username = (TextView)dialoglayout.findViewById(R.id.username);
					 profileurl = (TextView)dialoglayout.findViewById(R.id.profileurl);
					 
					 //getting JSONObject
					 
					 try {
						obj1 = ja_data.getJSONObject(position);
						browseurl = obj1.getString("html_url");
						browseusername = obj1.getString("login");

						 image.setImageUrl(obj1.getString("avatar_url"), imageLoader);
						 username.setText("Username: "+browseusername);
						 profileurl.setText("Github profile URL: "+browseurl);
						 Toast.makeText(getApplicationContext(), ""+obj1.getString("login")+"", Toast.LENGTH_SHORT).show();
						 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					 //setting onclick listener for profile url
					 profileurl.setOnClickListener(new View.OnClickListener() {
				    	 
				    	 public void onClick(View view) {
				    		 
				    		 Intent browserintent = new Intent(Intent.ACTION_VIEW, Uri.parse(browseurl));
								startActivity(browserintent);
				               
				            }
    
				}); 
					 
					 
					 //setting share button for share intent
				     
					 share.setOnClickListener(new View.OnClickListener() {
				    	 
				    	 public void onClick(View view) {
				    		 
				    		 Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				    		 sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Check out this awesome developer @ " +browseusername+","+browseurl+"");
				    		 startActivity(Intent.createChooser(sharingIntent,"Share using"));
				               
				            }	     
				});
					 
					 dialog1.show();
            }
        });

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}
	
//hide dialog class
	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

}