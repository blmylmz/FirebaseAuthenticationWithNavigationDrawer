package gelecegiyazanlar.com.gykfirebaseauthentication.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gelecegiyazanlar.com.gykfirebaseauthentication.R;

public class AddNoteActivity extends AppCompatActivity {

    EditText userNoteEt;
    Button addNoteBtn, gotoNotePage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        userNoteEt = (EditText) findViewById(R.id.user_notes_add);
        addNoteBtn= (Button) findViewById(R.id.add_notes_btn);
        gotoNotePage = (Button) findViewById(R.id.go_to_notes_btn);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        gotoNotePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    private void addNote(){
        //firebase yazma
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference().child("GezdigimYerler");
        String notesId = myRef.push().getKey(); //mesela toplu mesajlaşmada zaman bazında öncelik sırasına göre sıralar mesajları
        String userNote= userNoteEt.getText().toString(); //kulanıcının girdiği yazıyı string tipinde değişkene atıyor

        if (userNote.length()>0){ //kullanıcı bir not girdiyse
            myRef.child(notesId).child("sehirAdi").setValue(userNote); //benzersiz bir kilit oluşturup firebase'e atsın
            showDialog("İşlem başarılı", "Notunuz Kaydedildi");
        }
        else {
            showDialog("İşlem Başarısız", "Not Alanı Boş Bırakılamaz!");
        }
        userNoteEt.setText("");
    }

    private void showDialog(String title, String message){
       final AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
       builder.setTitle(title);
       builder.setMessage(message);
       builder.setNegativeButton("TAMAM", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int which) {

           }
       });
       builder.show();
    }
}
