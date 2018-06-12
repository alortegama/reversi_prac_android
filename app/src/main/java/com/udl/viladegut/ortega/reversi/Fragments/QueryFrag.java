package com.udl.viladegut.ortega.reversi.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.udl.viladegut.ortega.reversi.BDSQLite.DetailRegActivity;
import com.udl.viladegut.ortega.reversi.BDSQLite.LogSQLiteHelper;
import com.udl.viladegut.ortega.reversi.BDSQLite.SQLAdapter;
import com.udl.viladegut.ortega.reversi.Log;
import com.udl.viladegut.ortega.reversi.MainActivity;
import com.udl.viladegut.ortega.reversi.R;

import java.util.ArrayList;
import java.util.List;

public class QueryFrag extends Fragment {


    LogSQLiteHelper LogsHelper;
    SQLiteDatabase db;
    Button buttonBack;
    Button buttonDelete;
    ListView listView;
    SQLAdapter adapter;
    QueryListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (QueryListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must be implement QueryListener");
        }
    }

    private List<Log> logs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_query, container, false);
        listView = (ListView) view.findViewById(R.id.listViewQuery);
        buttonBack = (Button) view.findViewById(R.id.buttonGoBack);
        buttonDelete = (Button) view.findViewById(R.id.buttonDelete);
        logs = new ArrayList<>();

        LogsHelper = new LogSQLiteHelper(getContext(), "DBReversi", null, 2);
        db = LogsHelper.getWritableDatabase();

        adapter = new SQLAdapter(getActivity(), logs, R.layout.itemdb);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle b = new Bundle();
                Cursor cursorReg = db.rawQuery("SELECT * FROM Logs WHERE _id = ?", new String[]{Integer.toString(position + 1)});
                //Cursor cursorReg = db.rawQuery("SELECT * FROM Logs", null);
                if (cursorReg.moveToFirst()) {
                    Log log = new Log(cursorReg.getString(cursorReg.getColumnIndex("alias")),
                            cursorReg.getString(cursorReg.getColumnIndex("resultado")),
                            cursorReg.getString(cursorReg.getColumnIndex("data_hora")),
                            cursorReg.getInt(cursorReg.getColumnIndex("tabla")),
                            cursorReg.getInt(cursorReg.getColumnIndex("num_negras")),
                            cursorReg.getInt(cursorReg.getColumnIndex("num_blancas")),
                            cursorReg.getString(cursorReg.getColumnIndex("tiempo_total")));
                    b.putParcelable("LogId", log);

                    listener.queryFragment(b,position+1);
                    /*

                    }*/
                } else
                    Toast.makeText(getActivity(), "Error en agafar les dades", Toast.LENGTH_LONG).show();
                cursorReg.close();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAll();
                update();
            }
        });

        update();

        return view;
    }

    private List<Log> getAllLogs() {
        Cursor cursor = db.rawQuery("SELECT * FROM Logs", null);
        List<Log> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String alias = cursor.getString(cursor.getColumnIndex("alias"));
                String date = cursor.getString(cursor.getColumnIndex("data_hora"));
                String result = cursor.getString(cursor.getColumnIndex("resultado"));

                list.add(new Log(alias, result, date));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }

    private void removeAll() {
        String name_of_the_table = "Logs";
        db.execSQL("delete from Logs");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + name_of_the_table + "'");
    }

    private void update() {
        logs.clear();
        logs.addAll(getAllLogs());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public interface QueryListener{
        public void queryFragment(Bundle b, int i);
    }

    public void setQueryListener(QueryListener queryListener) {
        listener = queryListener;
    }
}
