package com.online.Lyfe.Online.Adapter.MessangerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import com.online.Lyfe.Online.Activities.Profile;
import com.online.Lyfe.Online.Model.messanger.message_frontend;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class messanger_adapter extends RecyclerView.Adapter<messanger_adapter.friend_holder> {

    private Context mContext;
    private ArrayList<message_frontend> Messages;

    private FirebaseUser firebaseUser;
    private RequestQueue mRequestQue;

    public messanger_adapter(ArrayList<message_frontend> Messages, Context context) {
        this.Messages = Messages;
        this.mContext = context;
    }

    @NonNull
    @Override
    public friend_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_frontend, parent, false);
        return new friend_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final friend_holder holder, final int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final message_frontend Message = Messages.get(position);

        holder.name.setText(Message.getName());
        Picasso.get().load(Message.getPic()).into(holder.profile);
        if (Message.isSeeing()) {
            holder.seen.setVisibility(View.VISIBLE);
        }
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Profile.class);
                intent.putExtra("user_id", Message.getId());
                mContext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return Messages.size();
    }

    public static class friend_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View seen;
        CircleImageView profile;
        TextView name, lastMessage;
        LinearLayout container;

        friend_holder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            seen = itemView.findViewById(R.id.seen);
            profile = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
