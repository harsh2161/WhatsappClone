package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class WhatsappChatActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_chat);
        selectedUser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(this,"Chat With "+selectedUser+" Now!!!", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
        findViewById(R.id.btnSend).setOnClickListener(this);
        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatsList);
        chatListView.setAdapter(adapter);
        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("Receiver", selectedUser);

            secondUserChatQuery.whereEqualTo("Sender", selectedUser);
            secondUserChatQuery.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");
            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String sender = chatObject.get("Message") + "";
                            if (chatObject.get("Sender").equals(ParseUser.getCurrentUser().getUsername())) {
                                sender = ParseUser.getCurrentUser().getUsername() + ": " + sender;
                            }
                            if (chatObject.get("Sender").equals(selectedUser)) {
                                sender = selectedUser + ": " + sender;
                            }
                            chatsList.add(sender);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        final EditText edtMessage = findViewById(R.id.edtSend);
        ParseObject chat = new ParseObject("Chat");
        chat.put("Sender",ParseUser.getCurrentUser().getUsername());
        chat.put("Receiver",selectedUser);
        chat.put("Message",edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
            }
        });
    }
}