package com.example.fragmentedGym.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragmentedGym.R;
import com.example.fragmentedGym.database.GymDB;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentedPurchase extends Fragment {

    @BindView(R.id.amount_edittext)
    EditText howManyEditText;
    @BindView(R.id.equipment_textview)
    TextView materialTextView;
    @BindView(R.id.buy_button)
    Button purchaseButton;
    @BindView(R.id.cancel_button)
    Button cancelButton;

    private FragmentedShop fragmentedShop;

    public interface FragmentedShop {
        void redrawCall();
    }

    public FragmentedPurchase(FragmentedShop fragmentedShop) {
        this.fragmentedShop = fragmentedShop;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        final String basket = getArguments().getString("my_string");
        if(basket!=null){
            materialTextView.setText(basket);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howManyEditText.setText("");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextNum = howManyEditText.getText().toString().trim();
                if(editTextNum.equals("")){
                    Toast.makeText(getContext(),"Please input a valid number",Toast.LENGTH_LONG).show();
                }else{
                    howManyEditText.setText("");
                    GymDB db = new GymDB(getContext(), null);
                    db.onPurchase(basket, Integer.parseInt(editTextNum));
                    getActivity().getSupportFragmentManager().popBackStack();
                    fragmentedShop.redrawCall();
                }
            }
        });
    }

}
