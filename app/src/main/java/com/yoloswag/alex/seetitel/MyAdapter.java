package com.yoloswag.alex.seetitel;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.WhistleViewHolder> {

    private static List<Whistle> whistles;

    public MyAdapter(List<Whistle> whistles) {
        this.whistles = whistles;
    }

    @Override
    public int getItemCount() {
        return whistles.size();
    }

    @Override
    public void onBindViewHolder(WhistleViewHolder whistle, int i) {
        Log.i("MyAdapter", "id " +  whistles.get(i).id +"populate whistles " + "data type: " + whistles.get(i).title);
        Whistle w = whistles.get(i);

        whistle.icon.setImageResource(w.icon);
        whistle.title.setText(w.title);
        whistle.description.setText(w.description);
    }

    @Override
    public WhistleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_layout,
                viewGroup, false);

        return new WhistleViewHolder(itemView);
    }

    public static class WhistleViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected ImageView icon;
        protected TextView title;
        protected TextView description;

        public WhistleViewHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.icon_card);
            title = (TextView) v.findViewById(R.id.tit);
            description = (TextView) v.findViewById(R.id.desc);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = null;
            if ((whistles.get(getPosition())).dataType.equals("TEXT")){
                i = new Intent(v.getContext(), TextActivity.class);
                i.putExtra("ID", (whistles.get(getPosition())).id);
                i.putExtra("FULL_TEXT", (whistles.get(getPosition())).description);
            }
            else if ((whistles.get(getPosition())).dataType.equals("IMAGE")) {
                i = new Intent(v.getContext(), ImageActivity.class);
                i.putExtra("ID", (whistles.get(getPosition())).id);
            } else if ((whistles.get(getPosition())).dataType.equals("AUDIO")) {
                i = new Intent(v.getContext(), AudioActivity.class);
                i.putExtra("ID", (whistles.get(getPosition())).id);
            }
            if (i != null) {
                v.getContext().startActivity(i);
            }
            //Toast.makeText(v.getContext(), ((TextView) v.findViewById(R.id.tit)).getText().toString(),Toast.LENGTH_LONG).show();
        }
    }


}
