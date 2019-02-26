package com.example.diabefriend.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.diabefriend.R;
import com.example.diabefriend.model.CustomAdapter;
import com.example.diabefriend.model.OnProductNameClick;
import com.example.diabefriend.model.Product;
import com.example.diabefriend.model.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SearchForProductFragment extends Fragment {
    public SearchForProductFragment() {
    }

    private List<Product> products = new ArrayList<>();
    private TextView weightInGramsTextView;
    private EditText weightInGramsInput;
    private TextView productInfoTextView;
    private String productName;
    private TextView choiceTextView;

    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        readProductsData();

        v = inflater.inflate(R.layout.activity_search_for_products, container, false);

        choiceTextView = v.findViewById(R.id.choiceTextView);
        choiceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiceDialog();
            }
        });

        weightInGramsTextView = v.findViewById(R.id.weightInGramsTextView);
        weightInGramsTextView.setVisibility(View.INVISIBLE);

        weightInGramsInput = v.findViewById(R.id.weightInGrams);
        weightInGramsInput.setVisibility(View.INVISIBLE);

        productInfoTextView = v.findViewById(R.id.productInfoTextView);
        productInfoTextView.setVisibility(View.INVISIBLE);

        weightInGramsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateProductInfoTextView(productInfoTextView, products, weightInGramsInput, productName);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }



    private void updateVisibility() {
        weightInGramsTextView.setVisibility(View.VISIBLE);
        weightInGramsInput.setVisibility(View.VISIBLE);
        productInfoTextView.setVisibility(View.VISIBLE);
    }

    private void updateProductInfoTextView(TextView productInfoTextView, List<Product> products, EditText weightInGramsInput, String productName) {
        int weightInGrams = 0;
        if (!weightInGramsInput.getText().toString().equals("")) {
            try {
                weightInGrams = getValidWeightInGrams(weightInGramsInput);
                productInfoTextView.setText(determineTextInfo(products, productName, weightInGrams));
            } catch (NumberFormatException e) {
                productInfoTextView.setText("The number is too big!");
            }
        } else {
            productInfoTextView.setText(determineTextInfo(products, productName, weightInGrams));
        }
    }

    private void showChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getLayoutInflater().inflate(R.layout.dialog_choice, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        final EditText choiceEditText = v.findViewById(R.id.choiceEditTextDialog);

        final List<String> productNames = getProductNames();

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final CustomAdapter customAdapter = new CustomAdapter(
                getContext(),
                productNames
        );

        choiceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapter.filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        recyclerView.setAdapter(customAdapter);

        customAdapter.setOnProductNameClick(new OnProductNameClick() {
            @Override
            public void click(String localProductName) {
                productName = localProductName;
                choiceTextView.setText(productName);
                updateVisibility();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        v.post(new Runnable() {
            @Override
            public void run() {
                customAdapter.notifyDataSetChanged();
            }
        });
    }

    private String determineTextInfo(List<Product> products, String productName, int weightInGrams) {
        StringBuilder textInfoBuilder = new StringBuilder();
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                float weightInDataBase = product.getWeight();

                textInfoBuilder
                        .append("kCal: ").append(Utils.decimalFormat.format(weightInGrams * product.getkCal() / weightInDataBase))
                        .append("\nFat: ").append(Utils.decimalFormat.format(weightInGrams * product.getFat() / weightInDataBase)).append(" g")
                        .append("\nCarbohydrates: ").append(Utils.decimalFormat.format(weightInGrams * product.getCarbohydrates() / weightInDataBase)).append(" g")
                        .append("\nProteins: ").append(Utils.decimalFormat.format(weightInGrams * product.getProteins() / weightInDataBase)).append(" g");
                break;
            }
        }

        return textInfoBuilder.toString();
    }

    private int getValidWeightInGrams(EditText weightInGramsInput) {
        return Integer.valueOf(weightInGramsInput.getText().toString());
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