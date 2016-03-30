package com.example.neeraj.spotifystreamer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String baseUrl = "http://api.themoviedb.org/3/movie/";
    String preference = "top_rated";
    String apiKey = "d4a6c95a8a7b0576f22425e1a518e28b";
    String baseURLImage="http://image.tmdb.org/t/p/w185";
    JSONObject jsonObject;
    List<MyObject> posterPathList = new ArrayList<MyObject>();

    GridviewAdapter gridviewAdapter;
    boolean flag=true;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");


        if (savedInstanceState != null) {
            posterPathList = savedInstanceState.getParcelableArrayList("posterlist");
            if(posterPathList.size()==0)
            { Log.v("size",""+posterPathList.size());
             flag=false;
            }
            if(flag) {

                Log.v("sizezzzzz",""+posterPathList.size());

                preference = savedInstanceState.getCharSequence("preference").toString();
                try {
                    jsonObject = new JSONObject(savedInstanceState.getCharSequence("jsonObject").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GridView gridView = (GridView) findViewById(R.id.grid_view);


                gridviewAdapter = new GridviewAdapter();
                gridView.setAdapter(gridviewAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            JSONObject movieDataObject = jsonArray.getJSONObject(position);

                            intent.putExtra("jsonString", movieDataObject.toString());
                            intent.putExtra("baseURLImage", baseURLImage);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }

        }
         if(posterPathList.size()==0) {

            progressDialog.show();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);

            GridView gridView = (GridView) findViewById(R.id.grid_view);


            gridviewAdapter = new GridviewAdapter();
            gridView.setAdapter(gridviewAdapter);

            FetchData fetchData = new FetchData();
            fetchData.execute();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        JSONObject movieDataObject = jsonArray.getJSONObject(position);

                        intent.putExtra("jsonString", movieDataObject.toString());
                        intent.putExtra("baseURLImage", baseURLImage);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        }


    }
    private class GridviewAdapter extends BaseAdapter
    {


        @Override
        public int getCount() {
            return posterPathList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView==null) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.gridview_items, parent, false);
                 imageView = (ImageView) v.findViewById(R.id.recyclerview_imageview);

            }
            else
            {
                 imageView= (ImageView) convertView;
            }

            Picasso.with(MainActivity.this).load(baseURLImage + posterPathList.get(position).posterPath).into(imageView, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {

                }
            });

            return imageView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(MainActivity.this);
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.popular_item&&preference.equals("top_rated"))
        {  progressDialog.show();
            preference="popular";
            posterPathList.clear();
            FetchData fetchData=new FetchData();
            fetchData.execute();
           gridviewAdapter.notifyDataSetChanged();

        }
        else if(item.getItemId()==R.id.top_rated_item&&preference.equals("popular"))
        { progressDialog.show();
            preference="top_rated";
            posterPathList.clear();
            FetchData fetchData=new FetchData();
            fetchData.execute();
           gridviewAdapter.notifyDataSetChanged();

        }
        else {
            Toast toast=Toast.makeText(MainActivity.this,"Already Sorted BY: "+preference,Toast.LENGTH_SHORT);
            toast.show();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v("posterlist size", ("" + posterPathList.size()));
        outState.putParcelableArrayList("posterlist", (ArrayList<? extends Parcelable>) posterPathList);
        if(posterPathList.size()!=0) {
            outState.putCharSequence("jsonObject", jsonObject.toString());
        }
        outState.putCharSequence("preference",preference);
        super.onSaveInstanceState(outState);
    }
    private class MyObject implements Parcelable
    {   String posterPath;
        MyObject(String path)
    {
        posterPath=path;
    }

        protected MyObject(Parcel in) {
            posterPath = in.readString();
        }

        public  final Creator<MyObject> CREATOR = new Creator<MyObject>() {
            @Override
            public MyObject createFromParcel(Parcel in) {
                return new MyObject(in);
            }

            @Override
            public MyObject[] newArray(int size) {
                return new MyObject[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(posterPath);
        }
    }

    private class FetchData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (!s.equals(null)) {
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        posterPathList.add(new MyObject(new MoviesData(object).getPosterPath()));
                    }


                    gridviewAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Toast toast=Toast.makeText(MainActivity.this,"Check Internet Connection",Toast.LENGTH_SHORT);
                toast.show();
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(baseUrl + preference + "?api_key=" + apiKey);
                 urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

            } catch (MalformedURLException e) {
                Log.v("malfomed","url");
                e.printStackTrace();
            }

            catch (IOException e) {
                Log.v("ioexcep","url");
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
            finally {

                urlConnection.disconnect();
            }
            return stringBuilder.toString();
        }
    }
}
