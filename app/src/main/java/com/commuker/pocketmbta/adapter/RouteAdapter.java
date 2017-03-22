package com.commuker.pocketmbta.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commuker.pocketmbta.R;
import com.commuker.pocketmbta.datamodel.Route;

import java.util.List;

/**
 * Created by lyu on 2017/3/19.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder> {
    private List<Route> routeList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.name.setText(route.getName());
        holder.state.setText(route.getState().toString());
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, state;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.route_name);
            state = (TextView) view.findViewById(R.id.route_state);
        }
    }


    public RouteAdapter(List<Route> routeList) {
        this.routeList = routeList;
    }


}
