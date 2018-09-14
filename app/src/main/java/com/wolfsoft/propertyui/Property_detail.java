package com.wolfsoft.propertyui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import apiParseing.Parser_Spring;
import apiParseing.Parser_Spring_response;
import listview.CommentItem;
import listview.CommentItemView;
import model.CampData;
import weather.WeatherInfo;
import weather.WeatherInfo2;

/**
 * Created by wolfsoft4 on 22/1/18.
 */
/* made in 2018-09-14 by Kim Dong Young */

public class Property_detail extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private TextView name, address, value1, value2, value3;
    private Button reservation, calling;
    private String strreserv, strsite, telNum;
    private double x, y;

    /* 새로 추가된 부분 ( listView 관련 변수들 )*/
    ListView listView;
    EditText comment;
    Button writeButton;
    CommentAdapter adapter = null;
    ArrayList<CommentItem> dataList = new ArrayList<>();
    Button showAllButton;
    EditText editComment;
    RatingBar ratingBar;
    TextView txtTime;
    /* 끝 */
    CampData index;
    ArrayList<CommentItem> list = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_propertydetail);

        /* intent 받은 정보를 가지고 오기 위한*/
        Intent i = getIntent();
        index = (CampData) i.getSerializableExtra("index");//parsing test

        name = (TextView) findViewById(R.id.showname);
        address = (TextView) findViewById(R.id.showaddress);
        value1 = (TextView) findViewById(R.id.value1);
        value2 = (TextView) findViewById(R.id.value2);
        value3 = (TextView) findViewById(R.id.value3);
        reservation = (Button) findViewById(R.id.reservation);
        calling = (Button) findViewById(R.id.calling);
        editComment = (EditText) findViewById(R.id.editComment);

        showAllButton = findViewById(R.id.btnShowAll);
        writeButton = findViewById(R.id.btnWriteComment);
        comment = findViewById(R.id.editComment);
        ratingBar = findViewById(R.id.ratingBar);
        txtTime = findViewById(R.id.txtTime);
        adapter = new CommentAdapter();
        listView = findViewById(R.id.listView);


        /* 전달받은 intent정보 사용*/
        name.setText(index.getName());
        address.setText(index.getAdd());
        value1.setText(index.getValue1());
        value2.setText(index.getValue2());
        value3.setText(index.getValue3());
        strsite = index.getHomepageurl();
        strreserv = index.getResurl();
        telNum = index.getTel();
        x = index.getX();
        y = index.getY();

        ViewPager pager = (ViewPager) findViewById(R.id.weatherPager);
        pager.setOffscreenPageLimit(3); //프래그먼트를 미리 담아두기

        CampingPagerAdapter fragment = new CampingPagerAdapter(getSupportFragmentManager());

        /* Fragment 추가 */
        WeatherInfo fragment1 = new WeatherInfo();
        fragment.addItem(fragment1);

        WeatherInfo2 fragment2 = new WeatherInfo2();
        fragment.addItem(fragment2);
        pager.setAdapter(fragment);

        /* Arraylist변수에 파싱을한 리턴값을 저장 execute()안에 들어가는 변수는 doInBackground()의 매개변수로 전달 */
        try {
            list = new Parser_Spring_response().execute(index.getId()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataList.clear(); // ArrayList내용 비우기
        adapter.setInit(); // adapter 내부 클래스의 setInit()메소드 호출

        for (int j = 0; j < list.size(); j++) {// db연동 전 listview 확인을 위한 for문
            Log.i("regdate : " , list.get(j).getRegdate());
            dataList.add(list.get(j));
            adapter.addItem(dataList.get(j));
        }// 리스트 추가
        listView.setAdapter(adapter);
        showComment();

        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllComment();
            }
        }); // 더보기 클릭시 이벤트


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // 토스트 메세지 ( 리스트 뷰 클릭시 )

                Toast.makeText(Property_detail.this, dataList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() { // 작성하기 버튼 클릭시 이벤트
            @Override
            public void onClick(View view) {
                String id;
                String text;
                float star;
                String type;
                if (editComment.getText().toString().length() == 0) {
                    Toast.makeText(Property_detail.this, "내용을 입력하지 않았습니다.", Toast.LENGTH_SHORT);
                } else {
                    WifiManager mwifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = mwifi.getConnectionInfo();
                    id = info.getMacAddress(); // 핸드폰 맥주소 가져오기
                    text = editComment.getText().toString();
                    star = ratingBar.getRating();
                    type = index.getId();
                    try {
                        CommentItem result = new CommentItem(id, text, star, type);
                        new Parser_Spring().execute(result).get();
                        editComment.setText("");
                        Toast.makeText(Property_detail.this, "작성한 내용이 전달되었습니다.", Toast.LENGTH_SHORT);
                        try {
                            list = new Parser_Spring_response().execute(index.getId()).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapter.setInit();
                        dataList.clear();
                        for (int j = 0; j < list.size(); j++) {       // db연동 전 listview 확인을 위한 for문
                            if (list.get(j).getType().equals(index.getId())) {
                                dataList.add(list.get(j));
                                adapter.addItem(dataList.get(j));
                            }
                        }// 리스트 추가
                        listView.setAdapter(adapter);
                        showComment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        /*추가 부분 끝 */


        GoogleApiClient gclient = new GoogleApiClient
                .Builder(this)
                .addApi(AppIndex.API)
                .build();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Property_detail.this);

        reservation.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strreserv)));
            }
        });
        calling.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNum));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class CampingPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public CampingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int i) {
            return items.get(i);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(y, x)).zoom(15).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //show current location
        googleMap.setMyLocationEnabled(true); // false to disable
        // Zooming Buttons
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //Zooming Functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        //Compass Functionality
        googleMap.getUiSettings().setCompassEnabled(true);
        //My Location Button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Map Rotate Gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);


    }


    /* inner 클래스 */
    class CommentAdapter extends BaseAdapter {

        ArrayList<CommentItem> items = new ArrayList();
        private int size = 0;

        public void initSize() {
            size = 0;
        }

        @Override
        public int getCount() {
            return size;
        }
        //리스트 개수 2개로 제한

        public void addItem(CommentItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setInit() {
            items.clear();
        }

        public void plusSize() {
            if (showAllButton.getText() == "더보기") {
                if (size + 2 <= items.size()) {
                    size += 2;
                } else if (size + 1 == items.size()) {
                    size++;
                } else {

                }
                if (size == items.size()) {
                    showAllButton.setText("가리기");

                }
            } else {
                showAllButton.setText("더보기");
                showComment();
            }

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommentItemView view = new CommentItemView(getApplicationContext());

            CommentItem item = items.get(position);

            view.setID(item.getId());
            view.setText(item.getText());
            view.setStar(item.getStar());
            view.setRegdate(item.getRegdate());
            return view;
        }

    }

    public void showComment() { // 초기화
        adapter.initSize();
        showAllComment();
    }


    public void showAllComment() { // 더보기
        adapter.plusSize();
        getTotalHeightofListView(listView);
        adapter.notifyDataSetChanged();
    }


    public void getTotalHeightofListView(ListView listView) // 개수 제한 및 높이 수정
    {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += listItem.getMeasuredHeight();
            Log.w("Height" + i, String.valueOf(totalHeight));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
