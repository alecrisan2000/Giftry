package com.example.chat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class GiftNameDialogFragment extends DialogFragment {

    private EditText giftNameEditText;
    private String giftName;

    public interface GiftNameDialogListener {
        void onFinishGiftNameDialog(String giftName);
    }

    public GiftNameDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_name_dialog, container);
        giftNameEditText = view.findViewById(R.id.editText_gift_name);
        getDialog().setTitle("Enter Gift Name");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        giftNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    giftName = giftNameEditText.getText().toString();
                    GiftNameDialogListener listener = (GiftNameDialogListener) getActivity();
                    listener.onFinishGiftNameDialog(giftName);
                    dismiss();
                    return true;
                }
                return false;
            }
            });

        }
    }

