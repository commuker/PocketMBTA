package com.commuker.pocketmbta.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commuker.pocketmbta.R;
import com.commuker.pocketmbta.adapter.RouteAdapter;
import com.commuker.pocketmbta.datamodel.Route;
import com.commuker.pocketmbta.datamodel.RouteState;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment class for each nav menu item
 */
public class MenuFragment extends Fragment {
    private List<Route> routeList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RouteAdapter mAdapter;

    public static Fragment newInstance(int itemId) {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new RouteAdapter(routeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        prepareRouteData();

        return view;
    }

    private void prepareRouteData() {
        Route route = new Route("Green-A", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-C", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-C", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-D", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-F", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-A2", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-P", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-S", RouteState.DELAY);
        routeList.add(route);

        route = new Route("Green-B2", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B3", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B4", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);

        route = new Route("Green-B", RouteState.ON_TIME);
        routeList.add(route);
    }

}
