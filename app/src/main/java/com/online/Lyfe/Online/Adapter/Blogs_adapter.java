package com.online.Lyfe.Online.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Activities.Comment;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Blogs_adapter extends RecyclerView.Adapter {
    private ArrayList<blog_model> blog_list;
    private Context mcontext;


    private FirebaseUser user = FirebaseAuth
            .getInstance().getCurrentUser();

    private DatabaseReference database
            = FirebaseDatabase.getInstance().getReference().child("Timeline");

    public Blogs_adapter(ArrayList<blog_model> bloglist) {
        this.blog_list = bloglist;
    }

    @Override
    public int getItemViewType(int position) {
        if (blog_list.get(position).getImage() == null || blog_list.get(position).getImage().equals("")) {
            if (blog_list.get(position).getOwner_id() == null)
                return 0;
            return 2;
        } else {
            if (blog_list.get(position).getOwner_id() == null)
                return 1;
            return 3;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.blog2, parent, false);
            return new bloges_holder_noimage(view);
        }
        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.blog, parent, false);
            return new bloges_holder(view);
        }
        if (viewType == 2) {
            view = layoutInflater.inflate(R.layout.shared_post_noimage, parent, false);
            return new shared_holder_noimage(view);
        }
        view = layoutInflater.inflate(R.layout.shared_post, parent, false);
        return new shared_holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (blog_list.get(position).getImage() == null || blog_list.get(position).getImage().equals("")) {
            if (blog_list.get(position).getOwner_id() == null) {
                final bloges_holder_noimage holder1 = (bloges_holder_noimage) holder;
                holder1.name.setText(blog_list.get(position).getName());
                holder1.date.setText("Uploaded at " + blog_list.get(position).getDate());
                holder1.dec.setText(blog_list.get(position).getDec());
                Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
                getlikes(blog_list.get(position).getKey(), new MyCallback() {
                    @Override
                    public void onCallback(long value) {
                        if (value > 0) {
                            holder1.text.setVisibility(View.VISIBLE);
                            holder1.favorite.setVisibility(View.GONE);
                            holder1.text.setText("" + value);
                        }
                    }
                });
                // onclick
                holder1.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());
                    }
                });
                holder1.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());
                        getlikes(blog_list.get(position).getKey(), new MyCallback() {
                            @Override
                            public void onCallback(long value) {
                                holder1.text.setVisibility(View.VISIBLE);
                                holder1.favorite.setVisibility(View.GONE);
                                holder1.text.setText("" + value);
                            }
                        });

                    }
                });
                holder1.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comment(blog_list.get(position).getKey());
                    }
                });
                holder1.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_user_data(blog_list.get(position));
                    }
                });
            } else {
                final shared_holder_noimage holder1 = (shared_holder_noimage) holder;
                holder1.name.setText(blog_list.get(position).getName());
                holder1.owner_name.setText(blog_list.get(position).getOwner_nmae());
                holder1.date.setText("Shared at " + blog_list.get(position).getDate());
                holder1.dec.setText(blog_list.get(position).getDec());
                Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
                Picasso.get().load(blog_list.get(position).getOwner_pic()).into(holder1.owner_pic);
                getlikes(blog_list.get(position).getKey(), new MyCallback() {
                    @Override
                    public void onCallback(long value) {
                        if (value > 0) {
                            holder1.text.setVisibility(View.VISIBLE);
                            holder1.favorite.setVisibility(View.GONE);
                            holder1.text.setText("" + value);
                        }
                    }
                });
                // onclick
                holder1.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());

                    }
                });
                holder1.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());
                        getlikes(blog_list.get(position).getKey(), new MyCallback() {
                            @Override
                            public void onCallback(long value) {
                                holder1.text.setVisibility(View.VISIBLE);
                                holder1.favorite.setVisibility(View.GONE);

                                holder1.text.setText("" + value);
                            }
                        });
                    }
                });
                holder1.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comment(blog_list.get(position).getKey());
                    }
                });
                holder1.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_user_data(blog_list.get(position));
                    }
                });
            }
        } else {
            if (blog_list.get(position).getOwner_id() == null) {
                final bloges_holder holder1 = (bloges_holder) holder;
                holder1.name.setText(blog_list.get(position).getName());
                holder1.date.setText("Uploaded at " + blog_list.get(position).getDate());
                holder1.dec.setText(blog_list.get(position).getDec());
                Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
                Picasso.get().load(blog_list.get(position).getImage()).fit().centerInside().into(holder1.image);

                getlikes(blog_list.get(position).getKey(), new MyCallback() {
                    @Override
                    public void onCallback(long value) {
                        if (value > 0) {
                            holder1.text.setVisibility(View.VISIBLE);
                            holder1.favorite.setVisibility(View.GONE);
                            holder1.text.setText("" + value);
                        }
                    }
                });
                // onclick
                holder1.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());

                    }
                });
                holder1.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getlikes(blog_list.get(position).getKey(), new MyCallback() {
                            @Override
                            public void onCallback(long value) {
                                holder1.text.setVisibility(View.VISIBLE);
                                holder1.favorite.setVisibility(View.GONE);

                                holder1.text.setText("" + value);
                            }
                        });
                        like(blog_list.get(position).getKey());

                    }
                });
                holder1.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comment(blog_list.get(position).getKey());
                    }
                });
                holder1.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_user_data(blog_list.get(position));
                    }
                });
            } else {
                final shared_holder holder1 = (shared_holder) holder;
                holder1.name.setText(blog_list.get(position).getName());
                holder1.owner_name.setText(blog_list.get(position).getOwner_nmae());
                holder1.date.setText("Shared at " + blog_list.get(position).getDate());
                holder1.dec.setText(blog_list.get(position).getDec());
                Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
                Picasso.get().load(blog_list.get(position).getOwner_pic()).into(holder1.owner_pic);
                Picasso.get().load(blog_list.get(position).getImage()).fit().centerInside().into(holder1.image);
                getlikes(blog_list.get(position).getKey(), new MyCallback() {
                    @Override
                    public void onCallback(long value) {
                        if (value > 0) {
                            holder1.text.setVisibility(View.VISIBLE);
                            holder1.favorite.setVisibility(View.GONE);
                            holder1.text.setText("" + value);
                        }
                    }
                });
                // onclick
                holder1.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());

                    }
                });
                holder1.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like(blog_list.get(position).getKey());
                        getlikes(blog_list.get(position).getKey(), new MyCallback() {
                            @Override
                            public void onCallback(long value) {
                                holder1.text.setVisibility(View.VISIBLE);
                                holder1.favorite.setVisibility(View.GONE);
                                holder1.text.setText("" + value);
                            }
                        });

                    }
                });
                holder1.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Comment(blog_list.get(position).getKey());
                    }
                });
                holder1.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        get_user_data(blog_list.get(position));
                    }
                });
            }

        }
    }

    private void Remove(String key) {
        database.child(key).child("likes").child(user.getUid()).removeValue();
    }

    private void getlikes(String key, final MyCallback myCallback) {
        if (key != null) {
            database.child(key).child("likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    myCallback.onCallback(count);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public interface MyCallback {
        void onCallback(long value);
    }

    private void share(blog_model blog_model, user_list user_list) {
        blog_model.setId(user.getUid());
        blog_model.setOwner_nmae(blog_model.getName());
        blog_model.setOwner_pic(blog_model.getPic());
        blog_model.setOwner_id(blog_model.getId());
        blog_model.setName(user_list.getFullnaame());
        blog_model.setPic(user_list.getProfile());
        blog_model.setId(user_list.getId());
        database.push().setValue(blog_model);
        //blog_list.clear();
        //notifyDataSetChanged();
        //  Toast.makeText(mcontext, "" + blog_model.getId() + "name is " + user_list.getFullnaame(), Toast.LENGTH_SHORT).show();
    }

    private void get_user_data(final blog_model blog_model) {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                share(blog_model, my_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void like(String key) {
        DatabaseReference liked_data = database.child(key).child("likes").child(user.getUid());
        liked_data.setValue(user.getUid());
    }

    private void Comment(String key) {
        Intent comment = new Intent(mcontext, Comment.class);
        comment.putExtra("key", key);
        mcontext.startActivity(comment);
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

//    public String getlastItemID() {
//        return blog_list.get(blog_list.size() - 1).getKey();
//    }

    public static class bloges_holder extends RecyclerView.ViewHolder {
        TextView name, date, dec;
        ImageView image;
        CircleImageView pic;
        ImageButton favorite, comment, share;
        Button text;

        bloges_holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            dec = itemView.findViewById(R.id.dec);
            image = itemView.findViewById(R.id.image);
            pic = itemView.findViewById(R.id.pic);
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);

        }
    }

    public static class bloges_holder_noimage extends RecyclerView.ViewHolder {
        TextView name, date, dec;
        //ImageView image;
        CircleImageView pic;
        ImageButton favorite, comment, share;
        Button text;

        bloges_holder_noimage(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            dec = itemView.findViewById(R.id.dec);
            // image = itemView.findViewById(R.id.image);
            pic = itemView.findViewById(R.id.pic);
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            text = itemView.findViewById(R.id.text);
        }


    }

    public static class shared_holder extends RecyclerView.ViewHolder {
        TextView name, date, dec, owner_name;
        ImageView image;
        CircleImageView pic, owner_pic;
        ImageButton favorite, comment, share;
        Button text;

        shared_holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);

            name = itemView.findViewById(R.id.name);
            owner_name = itemView.findViewById(R.id.owner_name);
            date = itemView.findViewById(R.id.date);
            dec = itemView.findViewById(R.id.dec);
            image = itemView.findViewById(R.id.image);
            pic = itemView.findViewById(R.id.pic);
            owner_pic = itemView.findViewById(R.id.owner_pic);
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
        }


    }

    public static class shared_holder_noimage extends RecyclerView.ViewHolder {
        TextView name, date, dec, owner_name;
        //ImageView image;
        CircleImageView pic, owner_pic;
        ImageButton favorite, comment, share;
        Button text;

        shared_holder_noimage(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);

            name = itemView.findViewById(R.id.name);
            owner_name = itemView.findViewById(R.id.owner_name);
            date = itemView.findViewById(R.id.date);
            dec = itemView.findViewById(R.id.dec);
            // image = itemView.findViewById(R.id.image);
            pic = itemView.findViewById(R.id.pic);
            owner_pic = itemView.findViewById(R.id.owner_pic);
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
        }


    }

}
