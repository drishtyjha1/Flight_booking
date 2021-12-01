package com.example.flight_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class BookAFlight extends AppCompatActivity {
    EditText dates,datea;
    DatePickerDialog datePickerDialog1,datePickerDialog2;
    Spinner spinner1,spinner2;
    String dep,dest;
    NavigationView navi;
    FirebaseAuth auth;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_aflight);


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();

        spinner1 = (Spinner) findViewById(R.id.spinnerDep);
        spinner2 = (Spinner) findViewById(R.id.spinnerDest);

        // initiate the date picker and a button

        dates = (EditText) findViewById(R.id.dateStart);

        // perform click event on edit text
        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // date picker dialog
                datePickerDialog1 = new DatePickerDialog(BookAFlight.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dates.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog1.show();
            }
        });

        datea = (EditText) findViewById(R.id.dateAnd);
        // perform click event on edit text
        datea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);
                // date picker dialog
                datePickerDialog2 = new DatePickerDialog(BookAFlight.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                datea.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);


                            }
                        }, year1, month1,day1);
                datePickerDialog2.show();
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                startActivity(new Intent(BookAFlight.this,UserProfile.class));
                Toast.makeText(getApplicationContext(), "profile panel is open", Toast.LENGTH_LONG).show();


                break;
            case R.id.menu_edit:
                Toast.makeText(getApplicationContext(), "edit panel is open", Toast.LENGTH_LONG).show();

                break;

            case R.id.settings:
                Toast.makeText(BookAFlight.this, "Setting Click", Toast.LENGTH_LONG).show();
                break;

            case R.id.logout:

                auth.signOut();
                Toast.makeText(BookAFlight.this, "you have successfully logout", Toast.LENGTH_LONG).show();
                startActivity(new Intent(BookAFlight.this, MainActivity.class));
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    public void Book_it(View v)
    {


        dep=String.valueOf(spinner1.getSelectedItem());
        dest=String.valueOf(spinner2.getSelectedItem());

        //DAtePicker


        String DateDest=dates.getText().toString();
        String DateDep=datea.getText().toString();

        //test
        if(dep.length()==0 || dest.length()==0 || DateDep.length()==0 || DateDest.length()==0)
        {
            //AlertDialog
            AlertDialog.Builder builder1 = new AlertDialog.Builder(BookAFlight.this);
            builder1.setMessage("Fill all the informations !");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder1.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Flight Booked !",Toast.LENGTH_LONG).show();
        }

            }

}