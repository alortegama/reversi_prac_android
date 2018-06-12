package com.udl.viladegut.ortega.reversi.BDSQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.udl.viladegut.ortega.reversi.Fragments.QueryFrag;
import com.udl.viladegut.ortega.reversi.Fragments.RegFrag;
import com.udl.viladegut.ortega.reversi.Log;
import com.udl.viladegut.ortega.reversi.R;

import java.util.ArrayList;
import java.util.List;

public class AccesBDActivity extends FragmentActivity implements QueryFrag.QueryListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acces_bd);

        QueryFrag queryFrag = (QueryFrag) getSupportFragmentManager().findFragmentById(R.id.queryFragment);
        queryFrag.setQueryListener(this);
    }

    @Override
    public void queryFragment(Bundle b, int position) {
        RegFrag regFrag = (RegFrag) getSupportFragmentManager().findFragmentById(R.id.regFragment);
        if (regFrag != null && regFrag.isInLayout()) {
            regFrag.updateRegFragment(regFrag.getView(), b, position);
        } else {
            Intent i = new Intent(this, DetailRegActivity.class);
            b.putInt(RegFrag.KEY_ID, position);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
    }
}
