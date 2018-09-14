package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolfsoft.propertyui.Property_detail;
import com.wolfsoft.propertyui.R;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import apiParseing.JsonParser;
import model.CampData;



/* 해당부분은 opensource이용하여 cardview 생성 */
public class RecycleAdapter_propertylist extends RecyclerView.Adapter<RecycleAdapter_propertylist.MyViewHolder> { // 카드뷰

    Context context;
    List<CampData> CampdataList;
    Map<String, CampData> tmp;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView name;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
        }

    }

    public RecycleAdapter_propertylist(Context mainActivityContacts, List<CampData> moviesList) {
        this.CampdataList = moviesList;
        this.context = mainActivityContacts;
        try {
            tmp = new JsonParser().execute().get();
            Log.d("Size", "" + tmp.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_property, parent, false);

        return new MyViewHolder(itemView);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CampData movie = CampdataList.get(position);

        holder.name.setText(movie.getName());
        holder.image.setImageResource(movie.getImage());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Property_detail.class);
                switch (movie.getName()) {
                    case "여의도 캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_1"));//parsing test
                        break;
                    case "뚝섬 캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_2"));//parsing test
                        break;
                    case "서울 성동숲 캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_3"));//parsing test
                        break;
                    case "구로 천왕공원 캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_4"));//parsing test
                        break;
                    case "난지 캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_5"));//parsing test
                        break;
                    case "노을캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_6"));//parsing test
                        break;
                    case "서울대공원 캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_7"));//parsing test
                        break;
                    case "중랑캠핑숲 가족캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_8"));//parsing test
                        break;
                    case "강동그린웨이 가족캠핑장":
                        i.putExtra("index", (CampData) tmp.get("camp2018_9"));//parsing test
                        break;
                }

                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return CampdataList.size();
    }


}


