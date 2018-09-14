package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.wolfsoft.propertyui.R;

import java.util.ArrayList;

import adapter.RecycleAdapter_propertylist;
import model.CampData;


/**
 * Created by Wolf Soft on 10/10/2017.
 */

public class NearByFragment extends Fragment {

    View view;

    private String name[] = {"여의도 캠핑장", "뚝섬 캠핑장", "서울 성동숲 캠핑장", "구로 천왕공원 캠핑장", "난지 캠핑장", "노을 캠핑장", "서울대공원 캠핑장", "중랑캠핑숲 가족캠핑장", "강동그린웨이 가족캠핑장"};
    private int image[] = {R.drawable.buildingone, R.drawable.buidingtwo, R.drawable.buildingthree, R.drawable.outdoorgames, R.drawable.buildingone, R.drawable.buidingtwo, R.drawable.buildingthree, R.drawable.outdoorgames, R.drawable.buildingone};


    private ArrayList<CampData> CampdataArrayList;
    private RecyclerView recyclerView;
    private RecycleAdapter_propertylist mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_neabyproperty, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_property);
        CampdataArrayList = new ArrayList<>();


        for (int i = 0; i < name.length; i++) {
            CampData beanClassForRecyclerView_contacts = new CampData(name[i], image[i]);
            CampdataArrayList.add(beanClassForRecyclerView_contacts);
        }


        mAdapter = new RecycleAdapter_propertylist(getActivity(), CampdataArrayList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


}
