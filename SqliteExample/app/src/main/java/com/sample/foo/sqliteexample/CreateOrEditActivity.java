package com.sample.foo.sqliteexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class CreateOrEditActivity extends ActionBarActivity implements View.OnClickListener {


    private ExampleDBHelper dbHelper ;
    EditText nameEditText;
    EditText genderEditText;
    EditText ageEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    int personID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        personID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        setContentView(R.layout.activity_edit);
        nameEditText = (EditText) findViewById(R.id.editTextName);
        genderEditText = (EditText) findViewById(R.id.editTextGender);
        ageEditText = (EditText) findViewById(R.id.editTextAge);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new ExampleDBHelper(this);

        if(personID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getPerson(personID);
            rs.moveToFirst();
            String personName = rs.getString(rs.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_NAME));
            String personGender = rs.getString(rs.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_GENDER));
            int personAge = rs.getInt(rs.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_AGE));
            if (!rs.isClosed()) {
                rs.close();
            }

            nameEditText.setText(personName);
            nameEditText.setFocusable(false);
            nameEditText.setClickable(false);

            genderEditText.setText((CharSequence) personGender);
            genderEditText.setFocusable(false);
            genderEditText.setClickable(false);

            ageEditText.setText((CharSequence) (personAge + ""));
            ageEditText.setFocusable(false);
            ageEditText.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistPerson();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                nameEditText.setEnabled(true);
                nameEditText.setFocusableInTouchMode(true);
                nameEditText.setClickable(true);

                genderEditText.setEnabled(true);
                genderEditText.setFocusableInTouchMode(true);
                genderEditText.setClickable(true);

                ageEditText.setEnabled(true);
                ageEditText.setFocusableInTouchMode(true);
                ageEditText.setClickable(true);
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deletePerson)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deletePerson(personID);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Person?");
                d.show();
                return;
        }
    }

    public void persistPerson() {
        if(personID > 0) {
            if(dbHelper.updatePerson(personID, nameEditText.getText().toString(),
                    genderEditText.getText().toString(),
                    Integer.parseInt(ageEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Person Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Person Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.insertPerson(nameEditText.getText().toString(),
                    genderEditText.getText().toString(),
                    Integer.parseInt(ageEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Person Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not Insert person", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
