package com.example.djd.fingertest;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;

/**
 * Created by djd on 18-8-28.
 */
@TargetApi(23)
public class FingerprintDialogFragment extends DialogFragment {
    private FingerprintManager mFingerprintManager;
    private CancellationSignal mCancellationSignal;
    private Cipher mChipher;
    private MainActivity mActivity;
    private TextView erroMsg;

    private boolean isSelfCancelled;

    public void setChipher(Cipher chipher) {
        this.mChipher = chipher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFingerprintManager = getContext().getSystemService(FingerprintManager.class);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Material_Light_Dialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fingerprinter_layout,container,false);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                stopListening();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startListening(mChipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopListening();
    }

    private void startListening(Cipher cipher){

        isSelfCancelled = false;

        mCancellationSignal = new CancellationSignal();
        mFingerprintManager.authenticate(new FingerprintManager.CryptoObject(mChipher),
                mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        super.onAuthenticationHelp(helpCode, helpString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(mActivity,"Authentication Succeeded",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                }, null);
    }


    private void stopListening(){
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }


}
