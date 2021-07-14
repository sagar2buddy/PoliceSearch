package com.sagar.policesearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sagar.policesearch.Constant.CONTENT_TYPE_PLAIN_TEXT;
import static com.sagar.policesearch.Constant.SHARE_INTENT_ACTION;
import static com.sagar.policesearch.Constant.SHARE_TEXT_ID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PS";

    private String state;
    private String district;

    int limit =10, offset = 0;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<PolicePojo> rowsArrayList = new ArrayList<>();
    private Context context;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initAdapter();
        initScrollListener();

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String text = intent.getStringExtra(SHARE_TEXT_ID);
                Log.d(TAG, "Intent to Share/Selected Text : "+text);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType(CONTENT_TYPE_PLAIN_TEXT);
                Intent.createChooser(intent,"Share via");
                startActivity(intent);
            }
        }, new IntentFilter(SHARE_INTENT_ACTION));
    }

    private void init () {
        sortAndSetDropdownList (R.id.spn_state, new String[]{"Andhra Pradesh"}, "ALL");
        sortAndSetDropdownList (R.id.spn_district, new String[] {"Anantapur", "Chittoor", "East Godavari"}, "ALL");
    }

    private void initAdapter() {
        recyclerView = findViewById(R.id.my_rec_view);
        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }


    /**
     * Get the spinner by Id, and sort the list
     *
     * @param spinnerID
     * @param dropdown
     * @param extraContent
     */
    private void sortAndSetDropdownList (final int spinnerID, String[] dropdown, String extraContent) {
        final List<String> sortedList = new ArrayList<>();
        if (dropdown != null) {
            Arrays.sort(dropdown);
        }
        if (extraContent != null) {
            sortedList.add(extraContent);
        }
        sortedList.addAll(Arrays.asList(dropdown));

        // Spinner details and adding the content
        Spinner spinner = findViewById(spinnerID);
        ArrayAdapter<String> adapter =  new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, sortedList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerID) {
                    case R.id.spn_state :
                        if (state != null && !state.equals(sortedList.get(position))) {
                            clear();
                        }
                        state = sortedList.get(position);
                        break;

                    case R.id.spn_district:
                        if (district != null && !district.equals(sortedList.get(position))) {
                            clear();
                        }
                        district = sortedList.get(position);
                        break;
                    default: break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Clear everything to initial state
     */
    private void clear(){
        rowsArrayList.clear();
        offset = 0;
        initAdapter();
        RecyclerViewAdapter.setSelectedPolicePojos(new HashSet<>());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * On Scroll, this method is called.
     *
     */
    private void loadMore() {
        rowsArrayList.add(null);
        //recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                offset++;
                populateData(offset);
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 100);
    }

    /**
     * Takes the offset argument and gets the data from DB, gets called on every scroll
     * with the limit and offset
     *
     * @param _offset
     */
    private void populateData (int _offset) {
        int count = _offset + 1;

        rowsArrayList.add(new PolicePojo(count + "", "psname - checking  the coutn buddy " + count, "anantapur", "andhra pradesh", "898232901"));
//        rowsArrayList.add(new PolicePojo(_offset+2+ "", "psname 2", "anantapur", "andhra pradesh", "123456778"));

//        try {
//            TowerHelperDAO dao = new TowerHelperDAO(dbFileUpPath);
//            List<TowerLocatorByIdPojo> resultList = dao.getAllByCellId(condition, circle, operator, cellId, _offset, limit);
//            Log.d(TAG, "getData List Size : " + resultList.size());
//            if (resultList.size() > 0) {
//                rowsArrayList.addAll(resultList);
//            }
//            else {
//                Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception ex){
//            Log.e(TAG, "Error in populateData ", ex);
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * Resets everything, clears all
     *
     * @param view
     */
    public void onReset (View view) {
        clear();
        ((EditText)findViewById(R.id.et_searchId)).setText("");
        ((Spinner)findViewById(R.id.spn_state)).setSelection(0);
        ((Spinner)findViewById(R.id.spn_district)).setSelection(0);
    }

    /**
     * When User Clicks on Search button, it validates the form and
     * get the data from DB => displays in card view
     *
     * @param view
     */
    public void onSearch (View view) {
        try {
//            if (!validate()) return;
            offset++;
            populateData(offset);
            recyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception ex){
            Log.e(TAG, "Error onSearch ", ex);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * When user clicks Share button, this methods get called and popsup the Share Intent
     *
     * @param view
     */
    public void onShare (View view) {
        Set<PolicePojo> selectedItems = RecyclerViewAdapter.getSelectedPolicePojos();
        List<PolicePojo> sortedList = new ArrayList<>();
        for (PolicePojo pojo : selectedItems) {
            sortedList.add(pojo);
        }
        Collections.sort(sortedList, (o1, o2) -> o1.getSno().compareTo(o2.getSno()));
        Log.d(TAG, "In Sharing List Size is : " + sortedList.size());
        Intent sendIntent = new Intent(SHARE_INTENT_ACTION);

        StringBuilder sb = new StringBuilder();
        for (PolicePojo pojo : sortedList) {
            sb.append("Sno : ").append(pojo.getSno()).append("\n");
            sb.append("PS Name : ").append(pojo.getPoliceStation()).append("\n");
            sb.append("Phone No : ").append(pojo.getPhoneNumber()).append("\n");
            sb.append("District : ").append(pojo.getDistrict()).append("\n").append("\n");
        }
        sendIntent.putExtra(SHARE_TEXT_ID, sb.toString());
        LocalBroadcastManager.getInstance(context).sendBroadcast(sendIntent);
    }
}