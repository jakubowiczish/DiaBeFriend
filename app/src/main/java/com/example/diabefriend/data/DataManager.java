package com.example.diabefriend.data;

import android.content.Context;
import android.util.Log;

import com.example.diabefriend.R;
import com.example.diabefriend.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private Context context;

    public DataManager(Context context) {
        this.context = context;
    }

    public List<Product> readProductsData() {
        List<Product> products = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.productsdatabase);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\t");
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

        return products;
    }

    public List<String> getProductNames(List<Product> products) {
        List<String> productNames = new ArrayList<>();
        for (Product product : products) {
            productNames.add(product.getName());
        }
        return productNames;
    }

    public Product getProductByName(List<Product> products, String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }


}
