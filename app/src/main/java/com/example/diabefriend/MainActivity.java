package com.example.diabefriend;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.diabefriend.ui.measurement.MeasurementFragment;
import com.example.diabefriend.ui.search.SearchForProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(createOnNavigationItemListener());
        navigation.setSelectedItemId(R.id.measurement);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener createOnNavigationItemListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.measurement:
                        selectedFragment = new MeasurementFragment();
                        break;
                    case R.id.search_for_product:
                        selectedFragment = new SearchForProductFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
                return true;
            }
        };
    }

}
