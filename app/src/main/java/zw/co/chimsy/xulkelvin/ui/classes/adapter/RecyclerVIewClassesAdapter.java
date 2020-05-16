package zw.co.chimsy.xulkelvin.ui.classes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zw.co.chimsy.xulkelvin.R;
import zw.co.chimsy.xulkelvin.ui.classes.activity.DetailViewActivity;
import zw.co.chimsy.xulkelvin.ui.classes.model.List_Data_Classes;

public class RecyclerVIewClassesAdapter extends RecyclerView.Adapter<RecyclerVIewClassesAdapter.ViewHolder> {
        private List<List_Data_Classes> list_datum_classes;
        private Context context;

        public RecyclerVIewClassesAdapter(List<List_Data_Classes> list_datum_classes, Context context) {
            this.list_datum_classes = list_datum_classes;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_classes,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final List_Data_Classes listData= list_datum_classes.get(position);

            Picasso.with(context)
                    .load(listData
                            .getImage_url())
                    .into(holder.img);

            holder.txtname.setText(listData.getName());

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, DetailViewActivity.class);
                    intent.putExtra("name",listData.getName());
                    intent.putExtra("imagurl",listData.getImage_url());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list_datum_classes.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView img;
            private TextView txtname;
            ViewHolder(View itemView) {
                super(itemView);
                img= itemView.findViewById(R.id.image_view);
                txtname= itemView.findViewById(R.id.text_name);
            }
        }
    }