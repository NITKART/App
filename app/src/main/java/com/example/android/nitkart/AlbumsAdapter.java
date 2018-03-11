package com.example.android.nitkart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    public ImageView thumbnail;
    TextView priceView, nameView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public Album album;
        private CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            priceView = (TextView) view.findViewById(R.id.product_price);
            nameView = (TextView) view.findViewById(R.id.product_name);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Album album = albumList.get(position);
        String url = MainActivity.domain + album.getUrl();
        Log.v("This is the URL:", url);
        Picasso.with(mContext).load(url).into(thumbnail);
        nameView.setText(album.getName());
        priceView.setText(album.getPrice());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                System.out.println("Clicked");
                Intent intent = new Intent(v.getContext(), ProductDetails.class);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), album.getUrl(), Toast.LENGTH_LONG).show();
            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetails.class);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), album.getUrl(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
