package com.example.childvaccinereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class schedule extends AppCompatActivity {

    private ExpandableListViewAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    private HashMap<String, List<Boolean>> selectedItems;



    Button saveButton;
    SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        sharedPreference = getSharedPreferences("SelectedItems", MODE_PRIVATE);

        ExpandableListView expandableListView;
        listDataGroup = new ArrayList<String>();
        listDataChild = new HashMap<String,List<String>>();
        selectedItems = new HashMap<String,List<Boolean>>();
        expandableListView = findViewById(R.id.expandableVaccineView);
        saveButton = findViewById(R.id.saveButton);

        String childName = getIntent().getStringExtra("childName");
        TextView childNameTextView = findViewById(R.id.childNameTextView);
        childNameTextView.setText(childName);

        expandableListViewAdapter=new ExpandableListViewAdapter(this,listDataGroup,listDataChild,selectedItems);
        expandableListView.setAdapter(expandableListViewAdapter);
        initListData();
        loadSelectedItems();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedItems(childName);
                Toast.makeText(schedule.this, "Selected items saved.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(schedule.this,view_child.class);
                startActivity(intent);
            }
        });

    }
    private void initListData() {
        listDataGroup.add("At Birth");
        listDataGroup.add("6 Weeks");
        listDataGroup.add("10 Weeks");
        listDataGroup.add("14 Weeks");
        listDataGroup.add("6 Months");
        listDataGroup.add("9 Months");
        listDataGroup.add("9-12 Months");
        listDataGroup.add("12 Months");
        listDataGroup.add("15 Months");
        listDataGroup.add("16-18 Months");
        listDataGroup.add("18 Months");
        listDataGroup.add("2 Years");
        listDataGroup.add("4-6 Years");

        String[] array;

        array = getResources().getStringArray(R.array.vaccine_birth);
        List<String> vaccineBirth = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_week_6);
        List<String> vaccineWeek6 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_week_10);
        List<String> vaccineWeek10 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_week_14);
        List<String> vaccineWeek14 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_6);
        List<String> vaccineMonth6 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_9);
        List<String> vaccineMonth9 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_9_12);
        List<String> vaccineMonth912 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_12);
        List<String> vaccineMonth12 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_15);
        List<String> vaccineMonth15 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_16_18);
        List<String> vaccineMonth1618 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_month_18);
        List<String> vaccineMonth18 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_year_2);
        List<String> vaccineYear2 = new ArrayList<>(Arrays.asList(array));

        array = getResources().getStringArray(R.array.vaccine_year_4_6);
        List<String> vaccineYear46 = new ArrayList<>(Arrays.asList(array));

        listDataChild.put(listDataGroup.get(0), vaccineBirth);
        listDataChild.put(listDataGroup.get(1), vaccineWeek6);
        listDataChild.put(listDataGroup.get(2), vaccineWeek10);
        listDataChild.put(listDataGroup.get(3), vaccineWeek14);
        listDataChild.put(listDataGroup.get(4), vaccineMonth6);
        listDataChild.put(listDataGroup.get(5), vaccineMonth9);
        listDataChild.put(listDataGroup.get(6), vaccineMonth912);
        listDataChild.put(listDataGroup.get(7), vaccineMonth12);
        listDataChild.put(listDataGroup.get(8), vaccineMonth15);
        listDataChild.put(listDataGroup.get(9), vaccineMonth1618);
        listDataChild.put(listDataGroup.get(10), vaccineMonth18);
        listDataChild.put(listDataGroup.get(11), vaccineYear2);
        listDataChild.put(listDataGroup.get(12), vaccineYear46);

        for (String group : listDataGroup) {
            List<Boolean> childSelected = new ArrayList<>();
            int childCount = listDataChild.get(group).size();
            for (int i = 0; i < childCount; i++) {
                childSelected.add(false);
            }
            selectedItems.put(group, childSelected);
        }
        expandableListViewAdapter.notifyDataSetChanged();
    }
    private void saveSelectedItems(String childName) {
        SharedPreferences.Editor editor = sharedPreference.edit();

        for (Map.Entry<String, List<Boolean>> entry : selectedItems.entrySet()) {
            String group = entry.getKey();
            List<Boolean> childSelected = entry.getValue();

            StringBuilder selectedItemsBuilder = new StringBuilder();
            for (boolean isSelected : childSelected) {
                selectedItemsBuilder.append(isSelected).append(",");
            }

            if (selectedItemsBuilder.length() > 0) {
                selectedItemsBuilder.deleteCharAt(selectedItemsBuilder.length() - 1);
            }

            String key = childName + "_" + group;
            editor.putString(key, selectedItemsBuilder.toString());
        }

        editor.apply();
    }

    private void loadSelectedItems() {
        String childName = getIntent().getStringExtra("childName");
        for (Map.Entry<String, List<Boolean>> entry : selectedItems.entrySet()) {
            String group = entry.getKey();
            String key = childName + "_" + group;
            String savedSelections = sharedPreference.getString(key, "");

            if (!savedSelections.isEmpty()) {
                String[] selections = savedSelections.split(",");

                List<Boolean> childSelected = new ArrayList<>();
                for (String selection : selections) {
                    childSelected.add(Boolean.parseBoolean(selection));
                }

                selectedItems.put(group, childSelected);
            }
        }
    }
}