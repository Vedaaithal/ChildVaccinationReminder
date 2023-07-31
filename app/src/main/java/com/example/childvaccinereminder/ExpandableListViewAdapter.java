package com.example.childvaccinereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private static final String PREFERENCES_FILE = "MyPrefs";
    private static final String SELECTED_ITEMS_KEY = "SelectedItems";
    private Context context;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    private HashMap<String, List<Boolean>> selectedItems;


    ExpandableListViewAdapter(Context context, List<String> listDataGroup,
                              HashMap<String, List<String>> listChildData,
                              HashMap<String, List<Boolean>> selectedItems) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
        this.selectedItems = selectedItems;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childTitle = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView childTextView = convertView.findViewById(R.id.textViewChild);
        final CheckBox checkBox = convertView.findViewById(R.id.checkbox);

        childTextView.setText(childTitle);

        checkBox.setChecked(selectedItems.get(listDataGroup.get(groupPosition)).get(childPosition));

        checkBox.setOnCheckedChangeListener(null);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedItems.get(listDataGroup.get(groupPosition)).set(childPosition, isChecked);

            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
    {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textViewGroup = convertView.findViewById(R.id.textViewGroup);
        textViewGroup.setText(groupTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    }


