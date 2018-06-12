package com.udl.viladegut.ortega.reversi.BDSQLite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udl.viladegut.ortega.reversi.Log;
import com.udl.viladegut.ortega.reversi.OnItemActionClickListener;
import com.udl.viladegut.ortega.reversi.R;
import com.udl.viladegut.ortega.reversi.ResultActivity;

import java.util.List;

public class SQLAdapter extends BaseAdapter{

    public static final String ID_BD = "_id";
    public static final String ALIAS_BD = "alias";
    public static final String DATE_TIME_BD = "data_hora";
    public static final String TABLE_BD = "tabla";
    public static final String NUM_NEGRAS_BD = "num_negras";
    public static final String NUM_BLANCAS_BD = "num_blancas";
    public static final String TOTAL_TIME_BD = "tiempo_total";
    public static final String RESULTAT_BD = "resultado";

    private Context context;
    private List<Log> list;
    private int layout;
    private OnItemActionClickListener listener;

    public SQLAdapter(Context context, List<Log> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Log getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.alias = (TextView) convertView.findViewById(R.id.textViewAlias);
            vh.data = (TextView) convertView.findViewById(R.id.textViewData);
            vh.result = (TextView) convertView.findViewById(R.id.textViewResultat);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Log currentLog = list.get(position);

        vh.alias.setText(currentLog.getPlayer() + "");
        vh.data.setText(currentLog.getDate_time());
        vh.result.setText(currentLog.getResult());

        return convertView;
    }

    public class ViewHolder {
        TextView alias;
        TextView data;
        TextView result;
    }
}
