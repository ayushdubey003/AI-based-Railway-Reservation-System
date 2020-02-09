package com.sih2020.railwayreservationsystem.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sih2020.railwayreservationsystem.R;
import com.sih2020.railwayreservationsystem.Utils.AppConstants;
import com.sih2020.railwayreservationsystem.Utils.GenerateBackground;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPassengerFragment extends Fragment {

    private Button addPassengerAp;
    private TextView layer1Ap, cancelButton;
    private EditText name, age;
    private RadioGroup radioGroupAp;
    private RadioButton male, female, transgender;
    private Spinner berthPreference, nationality;

    private ArrayAdapter<String> nationalityAdapter, berthAdapter;
    private ArrayList<String> mBerth = new ArrayList<>();

    private String selectedGender="";

    public AddPassengerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_passenger, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        receiveClicks();
    }

    private void receiveClicks() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.hideBottomSheet(getActivity());
            }
        });

        radioGroupAp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGroupAp.getCheckedRadioButtonId();
                if (selectedId == R.id.male_ap) {
                    selectedGender = "Male";
                } else if (selectedId == R.id.female_ap) {
                    selectedGender = "Female";
                } else {
                    selectedGender = "Transgender";
                }
            }
        });

        addPassengerAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    AppConstants.mAddPassengerModal.clear();
                    AppConstants.mAddPassengerModal.add(name.getText().toString().trim());
                    AppConstants.mAddPassengerModal.add(age.getText().toString().trim());
                    AppConstants.mAddPassengerModal.add(selectedGender);
                    AppConstants.mAddPassengerModal.add(mBerth.get(berthPreference.getSelectedItemPosition()));
                    AppConstants.mAddPassengerModal.add(AppConstants.mNationality[nationality.getSelectedItemPosition()]);
                    AppConstants.hideBottomSheet(getActivity());
                }
            }
        });
    }

    private boolean validate() {
        if(name.getText().toString().length()<=3){
            name.setError("Name should be atleast 4 characters");
            name.requestFocus();
            return false;
        }
        else if(age.getText().toString().length()>0 && age.getText().toString().length()<126){
            age.setError("Enter age between 0 and 125");
            age.requestFocus();
            return false;
        }
        else if(selectedGender.equals("")){
            Toast.makeText(getActivity(), "Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void init(View view) {
        mBerth.add("LB");
        mBerth.add("MB");
        mBerth.add("UB");
        mBerth.add("SL");
        mBerth.add("SU");

        layer1Ap = view.findViewById(R.id.layer1_ap);
        layer1Ap.setBackground(GenerateBackground.generateBackground());

        addPassengerAp = view.findViewById(R.id.add_passenger_ap);
        addPassengerAp.setBackground(GenerateBackground.generateBackground());
        addPassengerAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.hideBottomSheet(getActivity());
            }
        });

        cancelButton = view.findViewById(R.id.cancel_layer_ap);

        name = view.findViewById(R.id.input_name_ap);
        age = view.findViewById(R.id.input_age_ap);
        radioGroupAp = view.findViewById(R.id.radio_group_ap);
        male = view.findViewById(R.id.male_ap);
        female = view.findViewById(R.id.female_ap);
        transgender = view.findViewById(R.id.trans_ap);
        berthPreference = view.findViewById(R.id.berth_spinner_ap);
        nationality = view.findViewById(R.id.nationality_spinner_ap);

        nationalityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, AppConstants.mNationality);
        nationality.setAdapter(nationalityAdapter);

        berthAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mBerth);
        berthPreference.setAdapter(berthAdapter);
    }

}
