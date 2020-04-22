package com.online.Lyfe.Offline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.online.Lyfe.R;


import java.util.ArrayList;

public class Browser_adapter extends RecyclerView.Adapter<Browser_adapter.holder> {
    ArrayList list;
    holder.itemclick click;
    Context mcontext;

    public Browser_adapter(ArrayList list, Context mcontext, holder.itemclick click) {
        this.list = list;
        this.click = click;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public Browser_adapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new holder(view, click);
    }

    @Override
    public void onBindViewHolder(@NonNull Browser_adapter.holder holder, int position) {
        holder.post.setText(list.get(position).toString());
        holder.container.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.item_load));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView post;
        Button fv, cpy, shar;
        itemclick itemclick;
        boolean i = true;
        CardView container;

        public holder(@NonNull View itemView, itemclick itemclick) {
            super(itemView);
            this.itemclick = itemclick;
            post = itemView.findViewById(R.id.text);
            fv = itemView.findViewById(R.id.favorit);
            cpy = itemView.findViewById(R.id.copy);
            shar = itemView.findViewById(R.id.share);
            container = itemView.findViewById(R.id.container);
            fv.setOnClickListener(this);
            cpy.setOnClickListener(this);
            shar.setOnClickListener(this);
        }

        public interface itemclick {
            public void share(int position);

            public void favorite(int position);

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
            if (v.getId() == fv.getId()) {
                if (i) {
                    itemclick.favorite(getAdapterPosition());
                    fv.setBackgroundResource(R.drawable.empty);
                    i = false;
                } else {
                    fv.setBackgroundResource(R.drawable.favorite);
                    i = true;
                }
            }
        }
    }
}
