package com.example.help_m5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.help_m5.messages.MessagesAdapter;
import com.example.help_m5.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String name;
    private String email;
    private RecyclerView messagesRecyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cpen321help-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        name = "Peter Na";
        email = "lufei8351@gmail.com";

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilePicUrl = snapshot.child("users").child("profile_pic").getValue(String.class);

                if (!profilePicUrl.isEmpty()) {
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();

                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {

                    final String getEmail = dataSnapshot.child("email").getValue(String.class);

                    if (!getEmail.equals(email)) {
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);

                        MessagesList messagesList = new MessagesList(getName, getEmail, "", getProfilePic, 0);
                        messagesLists.add(messagesList);
                    }
                }

                messagesRecyclerView.setAdapter(new MessagesAdapter(messagesLists, ChatActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}