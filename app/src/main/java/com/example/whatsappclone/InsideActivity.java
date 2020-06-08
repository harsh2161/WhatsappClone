package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class InsideActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private String followedUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        setTitle("Whatsapp");
        listView = findViewById(R.id.ListView);
        arrayList=new ArrayList<>();
        arrayAdapter = new ArrayAdapter(InsideActivity.this,android.R.layout.simple_list_item_1,arrayList);
        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swipeContainer);
        listView.setOnItemClickListener(this);
        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            for (ParseUser user : objects) {
                                arrayList.add(user.getUsername());
                            }
                            listView.setAdapter(arrayAdapter);
                            listView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        /*This is refreshing coding*/
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",arrayList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(objects.size()>0){
                                if(e==null){
                                    for(ParseUser user:objects){
                                        arrayList.add(user.getUsername());
                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    if(mySwipeRefreshLayout.isRefreshing()){
                                        mySwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }else{
                                if(mySwipeRefreshLayout.isRefreshing()){
                                    mySwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try{
            if(item.getItemId()==R.id.logout_item){
                ParseUser.getCurrentUser().logOut();
                finish();
                Intent intent = new Intent(InsideActivity.this,LoginWhatsapp.class);
                startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(InsideActivity.this,WhatsappChatActivity.class);
        intent.putExtra("selectedUser",arrayList.get(position));
        startActivity(intent);
    }
}