package toblindr.student.chalmers.se.thealchemist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NAME = "param1";
    private static final String IMAGE_NAME = "param2";

    // TODO: Rename and change types of parameters
    private String name_of_item;
    private String image_path_of_item;

    private OnFragmentInteractionListener mListener;

    private ImageView imageView;
    private TextView textView;
    private RelativeLayout backPane;

    public NewItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name Parameter 1.
     * @param imagename Parameter 2.
     * @return A new instance of fragment NewItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewItemFragment newInstance(String name, String imagename) {
        NewItemFragment fragment = new NewItemFragment();
        Bundle args = new Bundle();

        args.putString(NAME, name);
        args.putString(IMAGE_NAME, imagename);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name_of_item = getArguments().getString(NAME);
            image_path_of_item = getArguments().getString(IMAGE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_item, container, false);
        imageView = view.findViewById(R.id.itemImage);
        textView = view.findViewById(R.id.nameOfItem);

        textView.setText(name_of_item);
        Bitmap b = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier( image_path_of_item ,
                        "drawable", getContext().getPackageName()));
        if(b != null){
            Bitmap.createScaledBitmap(b,400,400,false);
            imageView.setImageBitmap(b);
        }else{
            imageView.setImageBitmap(Util.getDefaultItemBitmap(getResources(),getContext(),400,400));
        }

        backPane = view.findViewById(R.id.backPane);
        backPane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBack();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void sendBack() {
        if (mListener != null) {
            mListener.hideFragment();
        }
    }

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
        void hideFragment();
    }
}
