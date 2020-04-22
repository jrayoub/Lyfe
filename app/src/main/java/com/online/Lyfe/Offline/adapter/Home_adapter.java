package com.online.Lyfe.Offline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.online.Lyfe.R;

import java.util.ArrayList;

public class Home_adapter extends RecyclerView.Adapter<Home_adapter.myholder> {
    private ArrayList list;
    private myholder.Onclick onclick;

    public Home_adapter(ArrayList list, myholder.Onclick onclick) {
        this.list = list;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
        return new myholder(view, onclick);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.title.setText(list.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public enum holder {}

    public static class myholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        Onclick mOnclick;

        myholder(@NonNull View itemView, Onclick onclick) {
            super(itemView);
            mOnclick = onclick;
            title = itemView.findViewById(R.id.title);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnclick.selected(getAdapterPosition());
        }

        public interface Onclick {
            public void selected(int position);
        }
    }

}
