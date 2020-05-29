package com.online.Lyfe.Online.Adapter.MessangerAdapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.online.Lyfe.Online.Activities.InsideChat;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class SMA extends RecyclerView.Adapter<SMA.friend_holder> {

    private ArrayList<user_list> mUsers;
    private Context mContext;

    public SMA(ArrayList<user_list> friend_list) {
        this.mUsers = friend_list;

    }

    @NonNull
    @Override
    public friend_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sma_item, parent, false);
        return new friend_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final friend_holder holder, final int position) {

        Picasso.get().load(mUsers.get(position).getProfile()).into(holder.profile);
        holder.name.setText(mUsers.get(position).getFullnaame());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ICA = new Intent(mContext, InsideChat.class);
                ICA.putExtra("USER_ID", mUsers.get(position).getId());
                ICA.putExtra("PROFILE_URL", mUsers.get(position).getProfile());
                ICA.putExtra("NAME", mUsers.get(position).getFullnaame());
                mContext.startActivity(ICA);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class friend_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profile;
        TextView name;

        friend_holder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {

        }

    }
}
