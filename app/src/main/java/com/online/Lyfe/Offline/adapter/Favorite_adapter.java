package com.online.Lyfe.Offline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.Lyfe.R;
import java.util.ArrayList;

public class Favorite_adapter extends RecyclerView.Adapter<Favorite_adapter.Holder> {
    private ArrayList list;
    private Holder.itemclick click;

    public Favorite_adapter(ArrayList list, Holder.itemclick click) {
        this.list = list;
        this.click = click;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoritexml, parent, false);
        return new Holder(view, click);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.post.setText(list.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView post;
        Button delet, cpy, shar;
        itemclick itemclick;
        public Holder(@NonNull View itemView, itemclick itemclick) {
            super(itemView);
            this.itemclick = itemclick;
            post = itemView.findViewById(R.id.text);
            delet = itemView.findViewById(R.id.delet);
            cpy = itemView.findViewById(R.id.copy);
            shar = itemView.findViewById(R.id.share);
            delet.setOnClickListener(this);
            cpy.setOnClickListener(this);
            shar.setOnClickListener(this);
        }

        public interface itemclick {
            public void share(int position);

            public void delet(int position);

            public void copy(int position);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == shar.getId()) {
                itemclick.share(getAdapterPosition());
            }
            if (v.getId() == cpy.getId()) {
                itemclick.copy(getAdapterPosition());
            }
            if (v.getId() == delet.getId()) {
                itemclick.delet(getAdapterPosition());
            }
        }
    }
}
