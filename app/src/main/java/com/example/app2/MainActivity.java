package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String AUTHORITY = "content://com.example.app1.NVProvider/nhanvien";
    static final Uri CONTENT_URI = Uri.parse(AUTHORITY);


    EditText ed_ID, ed_pass, ed_role;
    Button btnSave, btnSelect, btnXoa, btnSua;
    GridView gridView;
    ArrayAdapter<NhanVien> adapters;
    ArrayList<NhanVien> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_ID = findViewById(R.id.ed_ID);
        ed_pass = findViewById(R.id.ed_pass);
        ed_role = findViewById(R.id.ed_role);

        btnSave = findViewById(R.id.btnSave);
        btnSelect = findViewById(R.id.btnSelect);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);

        gridView = findViewById(R.id.gridview);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayGirdView();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NhanVien nv = new NhanVien(ed_ID.getText().toString(), ed_pass.getText().toString(), ed_role.getText().toString());
                insert(nv);
                displayGirdView();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NhanVien nv = new NhanVien(ed_ID.getText().toString(), ed_pass.getText().toString(), ed_role.getText().toString());
                updateNV(nv);
                displayGirdView();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNV(ed_ID.getText().toString());
                displayGirdView();
            }
        });
    }
    public ArrayList<NhanVien> selectAll() {
        ArrayList<NhanVien> ds = new ArrayList<>();
        Cursor cur = getContentResolver().query(CONTENT_URI, null, "Select * from nhanvien", null, null);
        if (cur != null)
            cur.moveToFirst();

        while (cur.isAfterLast() == false) {
            ds.add(new NhanVien(cur.getString(0), cur.getString(1), cur.getString(2)));
            cur.moveToNext();
        }

        cur.close();
        return ds;
    }
    public void displayGirdView(){
        // setAdapters();
        ArrayList<String> arrayList = new ArrayList<>();
        list = selectAll();
        for(NhanVien a : list){
            arrayList.add(a.getId() + "");
            arrayList.add(a.getName());
            arrayList.add(a.getAddress());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        gridView.setAdapter(adapter);
    }
    public void insert(NhanVien nv){

        if (list.contains(nv))
            Toast.makeText(MainActivity.this, "Trùng mã", Toast.LENGTH_SHORT).show();
        else {


            ContentValues values = new ContentValues();

            values.put("id", nv.getId());
            values.put("name", nv.getName());
            values.put("address", nv.getAddress());

            getContentResolver().insert(CONTENT_URI, values);

            list.add(nv);
            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteNV(String id) {
        if (id.equals(""))
            Toast.makeText(this, "ID không được rỗng", Toast.LENGTH_SHORT).show();
        else {

            int xoa = getContentResolver().delete(CONTENT_URI, id, null);
            Log.d("vị trí: ", "" + xoa);
            if (xoa > 0) {
                Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                NhanVien nv = new NhanVien(id);
                list.remove(nv);

                displayGirdView();
            }
            else
                Toast.makeText(this, "Không có account này", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateNV(NhanVien nv) {


        ContentValues contentValues = new ContentValues();

        contentValues.put("name", nv.getName());
        contentValues.put("address", nv.getAddress());

        int kt = getContentResolver().update(CONTENT_URI, contentValues, nv.getId(), null);
        if (kt > 0) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

            int vt = list.indexOf(nv);
            list.get(vt).setName(nv.getName());
            list.get(vt).setAddress(nv.getAddress());

            displayGirdView();
        }
        else
            Toast.makeText(this, "Không có nv này", Toast.LENGTH_SHORT).show();
    }


}
