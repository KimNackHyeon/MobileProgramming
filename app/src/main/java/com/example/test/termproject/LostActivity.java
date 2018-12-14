package com.example.test.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LostActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton LostRegister, campus;
    public RecyclerView recyclerView;
    public List<ImageDTO> imageDTOS = new ArrayList<>();
    public FirebaseDatabase database;
    String date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        campus = (ImageButton) findViewById(R.id.campus);
        LostRegister = (ImageButton) findViewById(R.id.LostRegister);
        LostRegister.setOnClickListener(this);
        campus.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        date = new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecycleViewAdapter boardRecycleViewAdapter = new BoardRecycleViewAdapter();
        recyclerView.setAdapter(boardRecycleViewAdapter);

        database.getReference().child("Lost_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageDTOS.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                    imageDTOS.add(imageDTO);
                }
                boardRecycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class BoardRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

            return new BoardRecycleViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder) holder).textView.setText(imageDTOS.get(position).title);
            ((CustomViewHolder) holder).textView2.setText("작성자 : " + imageDTOS.get(position).name);
            ((CustomViewHolder) holder).textView3.setText(date);
            ((CustomViewHolder) holder).textView4.setText(imageDTOS.get(position).tel);
            ((CustomViewHolder) holder).textView5.setText(imageDTOS.get(position).location);
            ((CustomViewHolder) holder).textView6.setText(imageDTOS.get(position).detail);
            ((CustomViewHolder) holder).textView7.setText(imageDTOS.get(position).imageUrl);
            Glide.with(holder.itemView.getContext()).load(imageDTOS.get(position).imageUrl).into((((CustomViewHolder) holder).imageView));
        }

        @Override
        public int getItemCount() {
            return imageDTOS.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView textView2;
            TextView textView3;
            TextView textView4;
            TextView textView5;
            TextView textView6;
            TextView textView7;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imgview);
                textView = (TextView) view.findViewById(R.id.title);
                textView2 = (TextView) view.findViewById(R.id.name);
                textView3 = (TextView) view.findViewById(R.id.date);
                textView4 = (TextView) view.findViewById(R.id.tel);
                textView5 = (TextView) view.findViewById(R.id.location);
                textView6 = (TextView) view.findViewById(R.id.explain);
                textView7 = (TextView) view.findViewById(R.id.url);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), DetailActivity.class);
                        intent.putExtra("title", textView.getText().toString());
                        intent.putExtra("name", textView2.getText().toString());
                        intent.putExtra("tel", textView4.getText().toString());
                        intent.putExtra("location", textView5.getText().toString());
                        intent.putExtra("explain", textView6.getText().toString());
                        intent.putExtra("url", textView7.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (view == LostRegister) {
            Intent intent = new Intent(LostActivity.this, LostRegisterActivity.class);
            startActivity(intent);
        }
        if (view == campus) {
            Intent intent = new Intent(LostActivity.this, campus.class);
            startActivity(intent);
        }
    }
}
