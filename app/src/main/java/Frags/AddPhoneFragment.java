package Frags;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidmads.firebaseuserauthsample.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPhoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPhoneFragment extends Fragment {
    Spinner sItems;
    Button next;
    EditText phoneNumber,countryCode;


    private OnFragmentInteractionListener mListener;

    public AddPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPhoneFragment newInstance(String param1, String param2) {
        AddPhoneFragment fragment = new AddPhoneFragment();
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
        View rootView= inflater.inflate(R.layout.fragment_add_phone, container, false);

        next=(Button) rootView.findViewById(R.id.button_start_verification);
        phoneNumber=(EditText) rootView.findViewById(R.id.field_phone_number);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (validatePhoneNumber(phoneNumber.getText().toString().trim())) {
                    //Initiate phone auth
                    mListener.onFragmentInteraction("init_auth",phoneNumber.getText().toString().trim());
                }

            }
        });

        return rootView;
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String msg,String phone);
    }

    private boolean validatePhoneNumber(String phone) {

        if (TextUtils.isEmpty(phone)) {
            phoneNumber.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
}
