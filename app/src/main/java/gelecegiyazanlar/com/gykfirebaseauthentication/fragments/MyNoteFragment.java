package gelecegiyazanlar.com.gykfirebaseauthentication.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gelecegiyazanlar.com.gykfirebaseauthentication.R;
import gelecegiyazanlar.com.gykfirebaseauthentication.activities.AddNoteActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyNoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyNoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ListView myNotesListView;
    ArrayAdapter <String> arrayAdapter;
    ArrayList<String> myNotesList = new ArrayList<>();
    String myPlaces;
    ProgressDialog progressDialog; //loading'deki dönen simge

    public MyNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyNoteFragment newInstance(String param1, String param2) {
        MyNoteFragment fragment = new MyNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        myNotesList = getMyNotes(); //notlarımı çeken fonksiyona atadık listeyi
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_note, container, false);
        Button addNoteBtn = (Button) view.findViewById(R.id.fragment_add_notes_btn);
        myNotesListView = view.findViewById(R.id.my_notes_lv);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, myNotesList);
        myNotesListView.setAdapter(arrayAdapter);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddNoteActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private ArrayList<String> getMyNotes(){
        showProgressDialog();
        final ArrayList<String> myNotes = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("GezdigimYerler");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // yeni not eklendiğinde vs değişiklikler buraya gelecek
                progressDialog.dismiss();
                myNotesList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    myPlaces = ds.child("sehirAdi").getValue().toString();
                    myNotesList.add(myPlaces);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
        return myNotesList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
        void onFragmentInteraction(Uri uri);
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getContext()); //fragmnette this kullanılmaz getcontext kullanırız
        progressDialog.setMessage("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show(); //diyaloğu göster
    }
}
