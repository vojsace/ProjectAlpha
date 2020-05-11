package com.vojsace.projectalpha;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class myViewHolder extends RecyclerView.ViewHolder {

    TextView textViewUser, textViewMsg;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewUser = itemView.findViewById(R.id.username_msgID);
        textViewMsg = itemView.findViewById(R.id.message_msgID);
    }
}
