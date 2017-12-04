package com.example.esoukhanov.group_project_organizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Automobile extends Activity {
protected ListView list_view;
protected EditText e_date;
protected EditText e_litars;
protected EditText e_price;
protected EditText e_km;
protected Button add;
protected ArrayList<ListItim> array;
protected ListItim itim;
    protected Cursor cursor;
 protected DatePickerDialog datePickerDialog;
    protected static final String ACTIVITY_NAME = "Automobile";
    protected AutomobileDatabaseHelper mydbase;
    protected SQLiteDatabase dbase;
    protected ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        Context context = getApplicationContext();
        list_view =(ListView) findViewById(R.id.list_view);
        e_date = (EditText)findViewById(R.id.date);
        e_litars =(EditText) findViewById(R.id.litars);
        e_price =(EditText) findViewById(R.id.price);
        e_km =(EditText) findViewById(R.id.km);
        add =(Button) findViewById(R.id.add);
        array = new ArrayList<ListItim>();

        final ListItimAdapter messageAdapter = new ListItimAdapter(this);
        list_view.setAdapter(messageAdapter);

        mydbase = new AutomobileDatabaseHelper(context) ;
        dbase = mydbase.getReadableDatabase();
        cv= new ContentValues();

        cursor = dbase.rawQuery("select * from " + AutomobileDatabaseHelper.TABLE_NAME, null);
        cursor .moveToFirst();
        while(!cursor.isAfterLast() ){
            String date= cursor.getString((cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_DATE)));
            String litars= cursor.getString((cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_LITARS)));
            String price= cursor.getString((cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_PRICE)));
            String km= cursor.getString((cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_KM)));
            itim = new ListItim (cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            array.add(itim);
            Log.i("Automobile", "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_DATE)));
            cursor.moveToNext();
        }

        Log.i("Automobile", "Cursor’s  column count =" + cursor.getColumnCount() );
        for (int i = 0; i < cursor.getColumnCount(); i++ ){
            Log.i("Automobile", cursor.getColumnName(i));
        }

        e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Automobile.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                e_date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = e_date.getText().toString();
                String litars = e_litars.getText().toString();
                String price = e_price.getText().toString();
                String km = e_km.getText().toString();

                if (!date.isEmpty() && !litars.isEmpty() && !price.isEmpty() && !km.isEmpty() ) {
                    array.add(itim);


                    ContentValues values = new ContentValues();
                    values.put(AutomobileDatabaseHelper.KEY_DATE, date);
                    values.put(AutomobileDatabaseHelper.KEY_LITARS, litars);
                    values.put(AutomobileDatabaseHelper.KEY_PRICE, price);
                    values.put(AutomobileDatabaseHelper.KEY_KM, km);

                    dbase.insert(AutomobileDatabaseHelper.TABLE_NAME, null, values);
                    e_date.setText("");
                    e_litars.setText("");
                    e_price.setText("");
                    e_km.setText("");
                    Toast.makeText(Automobile.this, "One row added", Toast.LENGTH_SHORT).show();
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Automobile.this);
                    builder.setMessage(R.string.dialog_message);
                    builder.setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                           // User cancelled the dialog
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            /**Intent resultIntent = new Intent(  );
                            resultIntent.putExtra("Response", "Here is my response");
                            setResult(Activity.RESULT_OK, resultIntent);*/
                            Automobile.this.finish();
                        }
                    })
                            .show();
                }
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
            }
        });
    }


    private class ListItimAdapter extends ArrayAdapter<ListItim> {
        public ListItimAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return array.size();
        }

        public ListItim getItem(int position) {
            return array.get(position);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Automobile.this.getLayoutInflater();
            View result = null;

                result = inflater.inflate(R.layout.automobile_row, null);


            ListItim li = getItem(position);
            String date= li.getDate();
            String litres = li.getLitars();
            String price = li.getPrice();
            String km = li.getLitars();

            TextView d = result.findViewById(R.id.t_d);
            d.setText(String.valueOf(date) ); // get the string at position
            TextView l = result.findViewById(R.id.t_l);
            l.setText(String.valueOf(litres)); // get the string at position

            TextView p = result.findViewById(R.id.t_p);
            p.setText(String.valueOf(price)); // get the string at position
            TextView k = result.findViewById(R.id.t_km);
            k.setText(String.valueOf(km)); // get the string at position
            Log.i("Automobile", "Create view");
            return result;

        }

    }
    protected void onDestroy() {
        super.onDestroy();
        //dbase.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
