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
    private String productName;
    private TextView choiceTextView;

    private TextView kCalNumberView;
    private TextView fatNumberView;
    private TextView carbohydratesNumberView;
    private TextView proteinNumberView;

    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        readProductsData();

        v = inflater.inflate(R.layout.activity_search_for_products, container, false);

        assignAndSetComponents();

        return v;
    }

    private void assignAndSetComponents() {
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

        kCalNumberView = v.findViewById(R.id.kCalNumberView);
        kCalNumberView.setVisibility(View.INVISIBLE);
        fatNumberView = v.findViewById(R.id.fatNumberView);
        fatNumberView.setVisibility(View.INVISIBLE);
        carbohydratesNumberView = v.findViewById(R.id.carbohydratesNumberView);
        carbohydratesNumberView.setVisibility(View.INVISIBLE);
        proteinNumberView = v.findViewById(R.id.proteinNumberView);
        proteinNumberView.setVisibility(View.INVISIBLE);

        weightInGramsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateProductInfoTextView(products, weightInGramsInput, productName);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateVisibility() {
        weightInGramsTextView.setVisibility(View.VISIBLE);
        weightInGramsInput.setVisibility(View.VISIBLE);

        kCalNumberView.setVisibility(View.VISIBLE);
        fatNumberView.setVisibility(View.VISIBLE);
        carbohydratesNumberView.setVisibility(View.VISIBLE);
        proteinNumberView.setVisibility(View.VISIBLE);
    }

    private void updateProductInfoTextView(List<Product> products, EditText weightInGramsInput, String productName) {
        int weightInGrams = 0;
        Product product = new Product("", 0, 0, 0, 0, 0);
        if (!weightInGramsInput.getText().toString().equals("")) {
            try {
                product = getProductByName(products, productName);
                weightInGrams = getValidWeightInGrams(weightInGramsInput);
                if (product != null) {
                    setValuesForTextViews(product, weightInGrams);
                }
            } catch (NumberFormatException e) {
                setInvalidTextMessageForNumberViews("The number is too big!");
            }
        } else {
            setValuesForTextViews(product, weightInGrams);
        }
    }

    private void setValuesForTextViews(Product product, int weightInGrams) {
        kCalNumberView.setText(determineCaloriesNumberText(product, weightInGrams));
        fatNumberView.setText(determineFatNumberText(product, weightInGrams));
        carbohydratesNumberView.setText(determineCarbohydratesNumberText(product, weightInGrams));
        proteinNumberView.setText(determineProteinNumberText(product, weightInGrams));
    }

    private void setInvalidTextMessageForNumberViews(String message) {
        kCalNumberView.setText(message);
        fatNumberView.setText(message);
        carbohydratesNumberView.setText(message);
        proteinNumberView.setText(message);
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
                updateProductInfoTextView(products, weightInGramsInput, productName);
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

    private Product getProductByName(List<Product> products, String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    private String determineCaloriesNumberText(Product product, int weightInGrams) {
        return Utils.decimalFormat.format(weightInGrams * product.getkCal() / product.getWeight()) + " kCal";
    }

    private String determineFatNumberText(Product product, int weightInGrams) {
        return Utils.decimalFormat.format(weightInGrams * product.getFat() / product.getWeight()) + " g";
    }

    private String determineCarbohydratesNumberText(Product product, int weightInGrams) {
        return Utils.decimalFormat.format(weightInGrams * product.getCarbohydrates() / product.getWeight()) + " g";
    }

    private String determineProteinNumberText(Product product, int weightInGrams) {
        return Utils.decimalFormat.format(weightInGrams * product.getProteins() / product.getWeight()) + " g";
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
        InputStream inputStream = getResources().openRawResource(R.raw.productsdatabase);
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
    }

}
