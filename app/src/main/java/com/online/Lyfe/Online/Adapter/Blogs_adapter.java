package com.online.Lyfe.Online.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.online.Lyfe.Online.Activities.Comment;
import com.online.Lyfe.Online.Activities.ImageScale;
import com.online.Lyfe.Online.Activities.Profile;
import com.online.Lyfe.Online.Model.Notification_model;
import com.online.Lyfe.Online.Model.blog_model;
import com.online.Lyfe.Online.Model.user_list;
import com.online.Lyfe.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Blogs_adapter extends RecyclerView.Adapter {
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
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
        mRequestQue = Volley.newRequestQueue(mcontext);
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

                BindDataTOui(holder, position);
            } else {
                share_BindData(holder, position);
            }
        } else {
            if (blog_list.get(position).getOwner_id() == null) {
                Bind_holder_image(holder, position);
            } else {
                share_holder_bind(holder, position);
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void Bind_holder_image(final RecyclerView.ViewHolder holder, final int position) {
        final bloges_holder holder1 = (bloges_holder) holder;
        holder1.name.setText(blog_list.get(position).getName());
        holder1.date.setText(blog_list.get(position).getDate());
        holder1.dec.setText(blog_list.get(position).getDec());
        Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
        Picasso.get().load(blog_list.get(position).getImage()).fit().centerInside().into(holder1.image);

        getlikes(blog_list.get(position).getKey(), new MyCallback() {
            @Override
            public void onCallback(long value, boolean check) {
                if (value > 0) {
                    holder1.text.setText(value + " likes");
                }
                if (check) {
                    holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                    holder1.favorite.setImageResource(R.drawable.wite_favorite);

                }
            }
        });
        // onclick
        holder1.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile(blog_list.get(position));
            }
        });
        holder1.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale(blog_list.get(position));

            }
        });
        holder1.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = blog_list.get(position).getKey();
                getlikes(key, new MyCallback() {
                    @Override
                    public void onCallback(long value, boolean check) {
                        holder1.text.setText(value + " likes");
                        if (check) {
                            Remove(key);
                            holder1.favorite.setBackgroundResource(R.drawable.button_background_rounded);
                            holder1.text.setText(value - 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.empty);
                        }
                        if (!check) {
                            like(key, blog_list.get(position));
                            holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                            holder1.text.setText(value + 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.wite_favorite);

                        }
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


    @SuppressLint("SetTextI18n")
    private void share_holder_bind(RecyclerView.ViewHolder holder, final int position) {
        final shared_holder holder1 = (shared_holder) holder;

        holder1.name.setText(blog_list.get(position).getName());
        holder1.owner_name.setText(blog_list.get(position).getOwner_nmae());
        holder1.date.setText(blog_list.get(position).getOwnerDate());
        holder1.share_date.setText(blog_list.get(position).getDate());
        holder1.dec.setText(blog_list.get(position).getDec());

        Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
        Picasso.get().load(blog_list.get(position).getOwner_pic()).into(holder1.owner_pic);
        Picasso.get().load(blog_list.get(position).getImage()).fit().centerInside().into(holder1.image);

        getlikes(blog_list.get(position).getKey(), new MyCallback() {
            @Override
            public void onCallback(long value, boolean check) {
                if (value > 0) {
                    holder1.text.setText(value + " likes");

                }
                if (check) {
                    holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                    holder1.favorite.setImageResource(R.drawable.wite_favorite);

                }
            }
        });
        // onclick
        holder1.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile(blog_list.get(position));
            }
        });
        holder1.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale(blog_list.get(position));

            }
        });

        holder1.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = blog_list.get(position).getKey();

                //like(key);
                getlikes(key, new MyCallback() {
                    @Override
                    public void onCallback(long value, boolean check) {
                        holder1.text.setText(value + " likes");
                        if (check) {
                            Remove(key);
                            holder1.favorite.setBackgroundResource(R.drawable.button_background_rounded);
                            holder1.text.setText(value - 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.empty);

                        }
                        if (!check) {
                            like(key, blog_list.get(position));
                            holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                            holder1.text.setText(value + 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.wite_favorite);

                        }
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

    @SuppressLint("SetTextI18n")
    private void share_BindData(final RecyclerView.ViewHolder holder, final int position) {
        final shared_holder_noimage holder1 = (shared_holder_noimage) holder;
        holder1.name.setText(blog_list.get(position).getName());
        holder1.owner_name.setText(blog_list.get(position).getOwner_nmae());
        holder1.date.setText(blog_list.get(position).getDate());
        holder1.owner_date.setText(blog_list.get(position).getDate());
        holder1.dec.setText(blog_list.get(position).getDec());
        Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
        Picasso.get().load(blog_list.get(position).getOwner_pic()).into(holder1.owner_pic);
        getlikes(blog_list.get(position).getKey(), new MyCallback() {
            @Override
            public void onCallback(long value, boolean check) {
                if (value > 0) {
                    holder1.text.setText(value + " likes");
                }
                if (check) {
                    holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                    holder1.favorite.setImageResource(R.drawable.wite_favorite);

                }
            }
        });
        // onclick
        holder1.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile(blog_list.get(position));
            }
        });
        holder1.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale(blog_list.get(position));

            }
        });

        holder1.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = blog_list.get(position).getKey();

                //like(key);
                getlikes(key, new MyCallback() {
                    @Override
                    public void onCallback(long value, boolean check) {
                        holder1.text.setText(value + " likes");
                        if (check) {
                            Remove(key);
                            holder1.favorite.setBackgroundResource(R.drawable.button_background_rounded);
                            holder1.text.setText(value - 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.empty);

                        }
                        if (!check) {
                            like(key, blog_list.get(position));
                            holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                            holder1.text.setText(value + 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.wite_favorite);

                        }
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

    @SuppressLint("SetTextI18n")
    private void BindDataTOui(RecyclerView.ViewHolder holder, final int position) {
        final bloges_holder_noimage holder1 = (bloges_holder_noimage) holder;
        holder1.name.setText(blog_list.get(position).getName());
        holder1.date.setText("Uploaded at " + blog_list.get(position).getDate());
        holder1.dec.setText(blog_list.get(position).getDec());
        Picasso.get().load(blog_list.get(position).getPic()).into(holder1.pic);
        getlikes(blog_list.get(position).getKey(), new MyCallback() {
            @Override
            public void onCallback(long value, boolean check) {
                if (value > 0) {
                    holder1.text.setText(value + " likes");
                }
                if (check) {
                    holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                    holder1.favorite.setImageResource(R.drawable.wite_favorite);


                }
            }
        });
        // onclick
        holder1.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile(blog_list.get(position));
            }
        });
        holder1.dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scale(blog_list.get(position));

            }
        });


        holder1.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = blog_list.get(position).getKey();

                //   like(key);
                getlikes(key, new MyCallback() {
                    @Override
                    public void onCallback(long value, boolean check) {
                        holder1.text.setText(value + " likes");
                        if (check) {
                            Remove(key);
                            holder1.favorite.setBackgroundResource(R.drawable.button_background_rounded);
                            holder1.text.setText(value - 1 + " likes");
                            holder1.favorite.setImageResource(R.drawable.empty);

                        }
                        if (!check) {
                            like(key, blog_list.get(position));
                            holder1.favorite.setBackgroundResource(R.drawable.clicked_favorite);
                            holder1.favorite.setImageResource(R.drawable.wite_favorite);
                            holder1.text.setText(value + 1 + " likes");

                        }
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

    private void Profile(blog_model model) {
        Intent profile = new Intent(mcontext, Profile.class);
        profile.putExtra("user_id", model.getId());
        mcontext.startActivity(profile);

    }

    private void scale(blog_model model) {
        Intent scale = new Intent(mcontext, ImageScale.class);
        scale.putExtra("url", model.getImage());
        scale.putExtra("key", model.getKey());
        scale.putExtra("name", model.getName());
        scale.putExtra("text", model.getDec());
        scale.putExtra("pic", model.getPic());
        scale.putExtra("date", model.getDate());
        mcontext.startActivity(scale);
    }

    public void sendNotification(user_list user, String Action, String category, blog_model item) {

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + user.getId());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "Lyfe");
            notificationObj.put("body", user.getFullnaame() + Action);

            JSONObject extraData = new JSONObject();
            extraData.put("category", category);
            extraData.put("key", item.getKey());
            extraData.put("url", item.getImage());
            extraData.put("pic", item.getPic());
            extraData.put("name", item.getName());
            extraData.put("text", item.getDec());
            extraData.put("date", item.getDate());


            json.put("notification", notificationObj);
            json.put("data", extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: " + error.networkResponse);
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAABNgTKmw:APA91bF5FSGfZmn5KVK5EpxIrXIwMcZXWTeF6h22WAOdCGdWv2lQbdr5NO5CgW14Xd3qJS_w_JOItJsrS0_XhsvHkBuyRxR0TqcFl0RNaK3OjQKmBm1oRwNMQMnzim8rwxsYMWVRNRrF");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Remove(String key) {
        database.child(key).child("likes").child(user.getUid()).removeValue();
    }

    private void getlikes(String key, final MyCallback myCallback) {
        if (key != null) {
            database.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    boolean check = dataSnapshot.child(user.getUid()).exists();
                    myCallback.onCallback(count, check);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    public interface MyCallback {
        void onCallback(long value, boolean check);
    }

    private void share(blog_model blog_model, user_list user_list) {
        blog_model.setId(user.getUid());
        blog_model.setOwner_nmae(blog_model.getName());
        blog_model.setOwner_pic(blog_model.getPic());
        blog_model.setOwner_id(blog_model.getId());
        blog_model.setOwnerDate(blog_model.getDate());
        blog_model.setName(user_list.getFullnaame());
        blog_model.setPic(user_list.getProfile());
        blog_model.setId(user_list.getId());
        blog_model.setDate(Calendar.getInstance().get(Calendar.HOUR) + ":" + Calendar.getInstance().get(Calendar.MINUTE));
        database.push().setValue(blog_model);
        Toast.makeText(mcontext, "post has been shared into your timeline", Toast.LENGTH_SHORT).show();

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

    private void addNOtification(final blog_model blog_model) {
        assert user != null;
        final DatabaseReference my_account = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        my_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list my_user = dataSnapshot.getValue(user_list.class);
                assert my_user != null;
                sentNotification(my_user, blog_model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void like(String key, blog_model model) {
        if (!model.getId().equals(user.getUid()) || true) {
            addNOtification(model);
        }
        DatabaseReference liked_data = database.child(key).child("likes").child(user.getUid());
        liked_data.setValue(user.getUid());
    }

    private void sentNotification(user_list theUser, blog_model model) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(model.getId());
        Notification_model notification = new Notification_model();
        notification.setUserid(user.getUid());
        notification.setText(" liked your post");
        notification.setIspost(true);
        notification.setPostid(model.getKey());
        notification.setUserid(theUser.getId());
        notification.setName(theUser.getFullnaame());
        reference.push().setValue(notification);
        sendNotification(theUser, " Liked your post", "post", model);
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

    public static class bloges_holder extends RecyclerView.ViewHolder {
        TextView name, date, dec, text;
        ImageView image;
        CircleImageView pic;
        ImageButton favorite, deslike, comment, share;

        bloges_holder(@NonNull View itemView) {
            super(itemView);
            //original
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            pic = itemView.findViewById(R.id.pic);
            image = itemView.findViewById(R.id.image);
            dec = itemView.findViewById(R.id.dec);

            //buttons
            favorite = itemView.findViewById(R.id.favorit);
          //  deslike = itemView.findViewById(R.id.deslike);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);

            //likes text
            text = itemView.findViewById(R.id.likes);
        }
    }

    public static class bloges_holder_noimage extends RecyclerView.ViewHolder {
        TextView name, date, dec, text;
        //ImageView image;
        CircleImageView pic;
        ImageButton favorite, deslike, comment, share;


        bloges_holder_noimage(@NonNull View itemView) {
            super(itemView);
            //original
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            pic = itemView.findViewById(R.id.pic);
            //image = itemView.findViewById(R.id.image);
            dec = itemView.findViewById(R.id.dec);

            //buttons
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
           // deslike = itemView.findViewById(R.id.deslike);

            //likes text
            text = itemView.findViewById(R.id.likes);
        }


    }

    public static class shared_holder extends RecyclerView.ViewHolder {
        TextView name, date, share_date, dec, owner_name, text;
        ImageView image;
        CircleImageView pic, owner_pic;
        ImageButton favorite, comment, share;

        shared_holder(@NonNull View itemView) {
            super(itemView);
            //share info
            name = itemView.findViewById(R.id.share_name);
            pic = itemView.findViewById(R.id.share_pic);
            share_date = itemView.findViewById(R.id.share_date);

            //original
            date = itemView.findViewById(R.id.date);
            owner_name = itemView.findViewById(R.id.name);
            owner_pic = itemView.findViewById(R.id.pic);
            image = itemView.findViewById(R.id.image);
            dec = itemView.findViewById(R.id.dec);

            //buttons
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);

            text = itemView.findViewById(R.id.likes);
        }


    }

    public static class shared_holder_noimage extends RecyclerView.ViewHolder {
        TextView name, date, owner_date, dec, owner_name, text;
        //ImageView image;
        CircleImageView pic, owner_pic;
        ImageButton favorite, deslike, comment, share;

        shared_holder_noimage(@NonNull View itemView) {
            super(itemView);
            //share info
            name = itemView.findViewById(R.id.share_name);
            pic = itemView.findViewById(R.id.share_pic);
            owner_date = itemView.findViewById(R.id.share_date);

            //original
            date = itemView.findViewById(R.id.date);
            owner_name = itemView.findViewById(R.id.name);
            owner_pic = itemView.findViewById(R.id.pic);
            dec = itemView.findViewById(R.id.dec);

            //buttons
            favorite = itemView.findViewById(R.id.favorit);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            //deslike = itemView.findViewById(R.id.deslike);

            //likes text
            text = itemView.findViewById(R.id.likes);
        }


    }

}
