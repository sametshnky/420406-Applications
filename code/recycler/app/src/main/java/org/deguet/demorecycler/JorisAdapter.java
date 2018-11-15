package org.deguet.demorecycler;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class JorisAdapter  extends RecyclerView.Adapter<JorisAdapter.MyViewHolder> {

    private List<Truc> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2;

        public MyViewHolder(View view) {
            super(view);
            tv1 = view.findViewById(R.id.tv1);
        }
    }


    public JorisAdapter(List<Truc> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Truc truc = list.get(position);
        holder.tv1.setText(truc.a);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}