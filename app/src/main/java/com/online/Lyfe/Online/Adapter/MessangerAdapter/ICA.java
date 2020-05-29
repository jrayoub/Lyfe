package com.online.Lyfe.Online.Adapter.MessangerAdapter;

import android.annotation.SuppressLint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.online.Lyfe.Online.Model.messanger.MSI;
import com.online.Lyfe.R;

import java.util.ArrayList;

public class ICA extends RecyclerView.Adapter {
    private ArrayList<MSI> Message;
    private FirebaseUser MY_USER = FirebaseAuth.getInstance().getCurrentUser();

    public ICA(ArrayList<MSI> Message) {
        this.Message = Message;
    }

    @Override
    public int getItemViewType(int position) {
        if (Message.get(position).getUser_id().equals(MY_USER.getUid())) {
            if (Message.get(position).getImage().equals("") || Message.get(position).getImage() == null)
                return 0;
            return 1;
        } else {
            if (Message.get(position).getImage().equals("") || Message.get(position).getImage() == null)
                return 2;
            return 3;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mychat, parent, false);
            return new MY_message(view);
        }
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mychat_image, parent, false);
            return new my_message_image(view);
        }
        if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.otherchat, parent, false);
            return new other_message(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.otherchat_image, parent, false);
        return new other_message_image(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        MSI item = Message.get(position);
        if (Message.get(position).getUser_id().equals(MY_USER.getUid())) {
            if (Message.get(position).getImage().equals("") || Message.get(position).getImage() == null) {
                MY_message HOLDER = (MY_message) holder;
                chat(item, HOLDER);
            } else {
                my_message_image Holder = (my_message_image) holder;
                chat_image(item, Holder);
            }
        } else {
            if (Message.get(position).getImage().equals("") || Message.get(position).getImage() == null) {
                other_message Holder = (other_message) holder;
                other_chat(item, Holder);
            } else {
                other_message_image Holder = (other_message_image) holder;
                other_chat_image(item, Holder);
            }
        }
    }

    private void other_chat_image(MSI item, other_message_image holder) {
        holder.message.setText(item.getMessage());

    }

    private void other_chat(MSI item, other_message holder) {
        holder.message.setText(item.getMessage());

    }

    private void chat_image(MSI item, my_message_image holder) {
        holder.message.setText(item.getMessage());

    }

    private void chat(MSI item, MY_message holder) {
        holder.message.setText(item.getMessage());
    }


    @Override
    public int getItemCount() {
        return Message.size();
    }

    public static class MY_message extends RecyclerView.ViewHolder {
        TextView message, time;
        View seen;

        MY_message(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            seen = itemView.findViewById(R.id.seen);


        }
    }

    public static class other_message extends RecyclerView.ViewHolder {
        TextView message, time;
        View seen;

        other_message(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            seen = itemView.findViewById(R.id.seen);
        }


    }

    public static class other_message_image extends RecyclerView.ViewHolder {
        TextView message, time;
        View seen;
        ImageView image;

        other_message_image(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            seen = itemView.findViewById(R.id.seen);
            image = itemView.findViewById(R.id.image);
        }


    }

    public static class my_message_image extends RecyclerView.ViewHolder {
        TextView message, time;
        View seen;
        ImageView image;

        my_message_image(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            seen = itemView.findViewById(R.id.seen);
            image = itemView.findViewById(R.id.image);

        }


    }


}
