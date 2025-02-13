package com.vojsace.projectalpha;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class myViewHolder extends RecyclerView.ViewHolder {

    TextView textViewUser, textViewMsg, likeText;
    LinearLayout usrLayout;
    ImageButton likeBtn;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewUser = itemView.findViewById(R.id.username_msgID);
        textViewMsg = itemView.findViewById(R.id.message_msgID);
        usrLayout = itemView.findViewById(R.id.current_userLayoutID);
        likeBtn = itemView.findViewById(R.id.likeButton);
        likeText = itemView.findViewById(R.id.likeTextID);

    }


}
