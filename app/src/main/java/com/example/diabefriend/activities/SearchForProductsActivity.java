package com.example.diabefriend.activities;

import android.os.Bundle;

import com.example.diabefriend.model.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.diabefriend.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SearchForProductsActivity extends AppCompatActivity {

    private List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_products);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readProductsData();

        List<String> productNames = getProductNames();

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productNames
        );

        autoCompleteTextView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private List<String> getProductNames() {
        List<String> productNames = new ArrayList<>();
        for (Product product : products) {
            productNames.add(product.getName());
        }
        return productNames;
    }

    private void readProductsData() {
        InputStream inputStream = getResources().openRawResource(R.raw.productsdata);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(";");
                Product product = new Product(
                        tokens[0],
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3]),
                        Float.parseFloat(tokens[4]),
                        Float.parseFloat(tokens[5])
                );
                products.add(product);

                Log.d("Product created", "New product has just been created: " + product);
            }
        } catch (IOException e) {
            Log.wtf("SearchForProductsActivity", "Error while reading data from file on line" + line, e);
            e.printStackTrace();
        }
    }


}
