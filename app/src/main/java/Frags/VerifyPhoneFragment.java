package Frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidmads.firebaseuserauthsample.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVerifyInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VerifyPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyPhoneFragment extends Fragment {

    Button btnVerify;
    Button btnResend;
    //private String ver_code;
    EditText txtVerificationCode;

    private OnVerifyInteractionListener mListener;

    public VerifyPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyPhoneFragment newInstance(String param1, String param2) {
        VerifyPhoneFragment fragment = new VerifyPhoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_verify_phone, container, false);
        btnVerify=(Button) rootView.findViewById(R.id.button_verify_phone);
        btnResend =(Button) rootView.findViewById(R.id.button_resend);
        txtVerificationCode=(EditText) rootView.findViewById(R.id.field_verification_code);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=txtVerificationCode.getText().toString().trim();
                if (!code.isEmpty()){
                    mListener.onVerifyFragmentInteraction(code);
                }else {
                    txtVerificationCode.setError("Empty editbox.");
//                    Toast.makeText(getActivity().getApplicationContext(),"Please enter code",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResend.setAlpha((float).5);
                String code=txtVerificationCode.getText().toString().trim();
                if (!code.isEmpty()){
                    mListener.onVerifyFragmentInteraction("resend");
                }

            }
        });



        return rootView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerifyInteractionListener) {
            mListener = (OnVerifyInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVerifyInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnVerifyInteractionListener {
        // TODO: Update argument type and name
        void onVerifyFragmentInteraction(String code);
    }
}
