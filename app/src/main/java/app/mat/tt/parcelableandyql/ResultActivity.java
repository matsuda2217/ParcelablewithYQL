package app.mat.tt.parcelableandyql;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Result> resultArrayList;
    ListView listV;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        listV = (ListView) findViewById(R.id.list);
        String searchTerm = getIntent().getStringExtra("searchTerm");
        String postalCode = getIntent().getStringExtra("postalCode");
        DownloadTask downloadTask = new DownloadTask(this);
        lat = getIntent().getDoubleExtra("latitude",0);
        lng = getIntent().getDoubleExtra("longitude",0);
        Log.e("Tag", lat + "--"+ lng);
        downloadTask.execute(searchTerm,postalCode);


    }
    public void drawList(final ArrayList<Result> lists){
        MyAdapter adapter = new MyAdapter(ResultActivity.this,lists);
        listV.setAdapter(adapter);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ResultActivity.this,DetailsActivity.class);
                intent.putExtra("result", lists.get(i));
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", lng);
                startActivity(intent);
            }
        });
    }
}
class DownloadTask extends AsyncTask<String ,Void, ArrayList<Result>>{
    ResultActivity re;

    DownloadTask(ResultActivity rc){
        re = rc;
    }
    ArrayList<Result> resultArrayList = new ArrayList<>();
    @Override
    protected ArrayList<Result> doInBackground(String... strings) {
        try {

            URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20zip%3D%27"+strings[1]+"%27%20and%20query%3D%27"+strings[0]+"%27&format=json&callback=");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String result = reader.readLine();
            JSONObject jsonObject =  new JSONObject(result);
            JSONObject query = jsonObject.getJSONObject("query");
            JSONObject result1 = query.getJSONObject("results");
            JSONArray array = result1.getJSONArray("Result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject currObj = array.getJSONObject(i);
                String title = currObj.getString("Title");
                String address = currObj.getString("Address");
                String city = currObj.getString("City");
                String phone = currObj.getString("Phone");
                String bUrl = currObj.getString("BusinessUrl");
                String dist = currObj.getString("Distance");
                double lat = currObj.getDouble("Latitude");
                double lng = currObj.getDouble("Longitude");
                Result resultss = new Result(lng,title,address,city,phone,bUrl,dist,lat);
                resultArrayList.add(resultss);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Result> results) {
        super.onPostExecute(results);
        re.drawList(results);
    }
}
class MyAdapter extends BaseAdapter{
    Context c;
    ArrayList<Result> lists;
    MyAdapter(Context c, ArrayList<Result> l){
        this.c = c;
        this.lists = l;
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView title, address, phone,dist;
        View v = LayoutInflater.from(c).inflate(R.layout.row_layout,viewGroup,false);
        title = (TextView) v.findViewById(R.id.titleText);
        address = (TextView) v.findViewById(R.id.addressText);
        phone = (TextView) v.findViewById(R.id.phoneText);
        dist = (TextView) v.findViewById(R.id.distanceText);

        title.setText(lists.get(i).title);
        address.setText(lists.get(i).address);
        phone.setText(lists.get(i).phone);
        dist.setText(lists.get(i).distance);

        return v;
    }
}