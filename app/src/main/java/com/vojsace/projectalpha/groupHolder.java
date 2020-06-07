package com.vojsace.projectalpha;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class groupHolder extends RecyclerView.ViewHolder {

    TextView questionText;

    public groupHolder(@NonNull View itemView) {
        super(itemView);
        questionText = itemView.findViewById(R.id.group_msgID);
    }
}
