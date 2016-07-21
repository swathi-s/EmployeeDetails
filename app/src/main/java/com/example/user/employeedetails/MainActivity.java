package com.example.user.employeedetails;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText empName,empEmail,empPhone,empDob,empAddress;
    ImageView empImg;
    List<Employee> Employees = new ArrayList<Employee>();
    ListView employeeListView;
    Uri imageUri = Uri.parse("android.resource://com.example.user.employeedetails/drawable/business_man_blue");
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empName = (EditText) findViewById(R.id.empName);
        empEmail = (EditText) findViewById(R.id.empEmail);
        empPhone = (EditText) findViewById(R.id.empPhone);
        empAddress = (EditText) findViewById(R.id.empAddress);
        empDob = (EditText) findViewById(R.id.empDob);
        empImg = (ImageView) findViewById(R.id.empImg);
        employeeListView = (ListView) findViewById(R.id.employeesList);
        dbHandler = new DatabaseHandler(getApplicationContext());
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Add Employee");

        tabSpec.setContent(R.id.tabEmployeeCreator);
        tabSpec.setIndicator("Add Employee");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("List");
        tabSpec.setContent(R.id.tabEmployeeList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        final Button btnSave = (Button) findViewById(R.id.btnSave);
        final Button btnReset = (Button) findViewById(R.id.btnReset);

        btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view)
           {
               Employee employee = new Employee(dbHandler.getEmployeesCount(),String.valueOf(empName.getText()),String.valueOf(empEmail.getText()),String.valueOf(empPhone.getText()),String.valueOf(empDob.getText()),String.valueOf(empAddress.getText()),imageUri);
               if(!employeeExists(employee)) {
                   dbHandler.createEmployee(employee);
                   Employees.add(employee);
                   //populateList();
                   //Employees.add(new Employee(1,empName.getText().toString(),empEmail.getText().toString(),empPhone.getText().toString(),empDob.getText().toString(),empAddress.getText().toString(),imageUri));
                   Toast.makeText(getApplicationContext(), String.valueOf(empName.getText()) + " has been added to system successfully", Toast.LENGTH_SHORT).show();
                   return;
               }
               Toast.makeText(getApplicationContext(),String.valueOf(empName.getText()) + " already exists. Pleae use different name",Toast.LENGTH_SHORT).show();
           }
        });

        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                empName.setText("");
                empDob.setText("");
                empPhone.setText("");
                empEmail.setText("");
                empAddress.setText("");
                empImg.setImageResource(R.drawable.business_man_blue);
                Toast.makeText(getApplicationContext(),"Data cleared",Toast.LENGTH_SHORT).show();
            }
        });
        empName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSave.setEnabled(!String.valueOf(empName.getText()).trim().isEmpty());
                btnReset.setEnabled(!String.valueOf(empName.getText()).trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        empImg.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
            }
        });

        if(dbHandler.getEmployeesCount() != 0)
            Employees.addAll(dbHandler.getAllEmployees());

        populateList();
    }

    private boolean employeeExists(Employee employee)
    {

        String name = employee.getName();
        Log.w("Employee exists","Entered into this"+name);
        int employeeCount = Employees.size();
        Log.w("Employee exists","size "+employeeCount);
        for(int i = 0; i< employeeCount; i++)
        {
            Log.w("Employee name",Employees.get(i).getName());
            if(name.compareToIgnoreCase(Employees.get(i).getName()) == 0)
                return true;
        }
        return false;
    }
    public void onActivityResult(int reqCode, int resCode, Intent data){
        if( resCode == RESULT_OK) {
            if(reqCode == 1) {
                imageUri = data.getData();
                empImg.setImageURI(data.getData());
            }
        }
    }

    private void populateList(){
        ArrayAdapter<Employee> adapter = new EmployeeListAdapter();
        employeeListView.setAdapter(adapter);
    }

    private class EmployeeListAdapter extends ArrayAdapter<Employee> {
        public EmployeeListAdapter() {
            super (MainActivity.this,R.layout.listviewitem,Employees);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.listviewitem,parent,false);
            }

            Employee currentEmployee = Employees.get(position);
            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(currentEmployee.getName());
            TextView email = (TextView) view.findViewById(R.id.email);
            email.setText(currentEmployee.getEmail());
            TextView phone = (TextView) view.findViewById(R.id.phone);
            phone.setText(currentEmployee.getPhone());
            TextView dob = (TextView) view.findViewById(R.id.dob);
            dob.setText(currentEmployee.getDob());
            TextView address = (TextView) view.findViewById(R.id.address);
            address.setText(currentEmployee.getAddress());

            ImageView listImg = (ImageView) view.findViewById(R.id.listImg);
            listImg.setImageURI(currentEmployee.getImageURI());
            return view;
        }
    }
}
