package net.rafaeltoledo.keeptesting.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.rafaeltoledo.keeptesting.R;
import net.rafaeltoledo.keeptesting.data.model.User;
import net.rafaeltoledo.keeptesting.ui.listener.OnUserClickListener;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private ImageView userImage;
    private TextView userLocation;

    public UserViewHolder(View itemView) {
        super(itemView);
        userName = (TextView) itemView.findViewById(R.id.userName);
        userImage = (ImageView) itemView.findViewById(R.id.userImage);
        userLocation = (TextView) itemView.findViewById(R.id.userLocation);
    }

    public void bind(final User user, final OnUserClickListener listener) {
        userName.setText(user.getDisplayName());
        Glide.with(itemView.getContext()).load(user.getProfileImage()).into(userImage);
        userLocation.setText(user.getLocation());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserClick(user);
            }
        });
    }
}
