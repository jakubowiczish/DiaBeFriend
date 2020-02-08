package com.example.diabefriend.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabefriend.R;
import com.example.diabefriend.data.DataManager;
import com.example.diabefriend.model.CustomAdapter;
import com.example.diabefriend.model.OnProductNameClick;
import com.example.diabefriend.model.Product;
import com.example.diabefriend.model.Utils;

import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SearchForProductFragment extends Fragment {

    private List<Product> products;
    private TextView weightInGramsTextView;
    private EditText weightInGramsInput;
    private String productName;
    private TextView choiceTextView;

    private TextView kCalNumberView;
    private TextView fatNumberView;
    private TextView carbohydratesNumberView;
    private TextView proteinNumberView;

    private View v;

    private DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        readProductsData();

        v = inflater.inflate(R.layout.activity_search_for_products, container, false);
        assignAndSetComponents();

        return v;
    }


    private void readProductsData() {
        dataManager = new DataManager(getContext());
        products = dataManager.readProductsData();
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
                updateProductInfoTextView(products, productName);
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


    private void updateProductInfoTextView(List<Product> products, String productName) {
        int weightInGrams = 100;
        Product product = dataManager.getProductByName(products, productName);

        if (!weightInGramsInput.getText().toString().equals("")) {
            try {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View v = getLayoutInflater().inflate(R.layout.dialog_choice, null);
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();

        final EditText choiceEditText = v.findViewById(R.id.choiceEditTextDialog);

        final List<String> productNames = dataManager.getProductNames(products);

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
                updateProductInfoTextView(products, productName);
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


    private String determineCaloriesNumberText(Product product, int weightInGrams) {
        return Utils.decimalFormat.format(weightInGrams * product.getKCal() / product.getWeight()) + " kCal";
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
}
