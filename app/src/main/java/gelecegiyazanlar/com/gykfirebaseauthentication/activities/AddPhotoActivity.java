package gelecegiyazanlar.com.gykfirebaseauthentication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import gelecegiyazanlar.com.gykfirebaseauthentication.R;

public class AddPhotoActivity extends AppCompatActivity {

    ImageView userPhoto;
    Button selectPhotoBtn, savePhotoBtn;
    private static final int IMAGE_REQUEST = 111;
    Uri filePath;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        firebaseStorage = FirebaseStorage.getInstance();

        userPhoto = (ImageView) findViewById(R.id.user_saved_photo);
        selectPhotoBtn = (Button) findViewById(R.id.select_photo_button);
        savePhotoBtn = (Button) findViewById(R.id.save_photo_button);

        selectPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        savePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhoto();
            }
        });
    }

    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*"); //intentimin tipini seçeceğim ve tüm fotoları seç dedim
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), IMAGE_REQUEST); //
    }

    private void savePhoto() {
       if (filePath != null) {
           StorageReference storageReference = firebaseStorage.getReference();
           storageReference.child("userprofilphoto").putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Toast.makeText(AddPhotoActivity.this, "Fotoğraf Kaydetme Başarılı", Toast.LENGTH_LONG).show();
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(AddPhotoActivity.this, "Fotoğraf Kaydetme Başarısız", Toast.LENGTH_LONG).show();
               }
           });
       }
       else {
           showDialog("İşlem Başarısız", "Herhangi Bir Fotoğraf Seçilmedi!");
       }

    }

    private void showDialog(String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("TAMAM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode, @Nullable Intent data) {  //request: Resim seçerken gönderdiğim istek kodum
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();// fotoğrafımı aldığım path


            try {
                Picasso.with(AddPhotoActivity.this).load(filePath).fit().centerCrop().into(userPhoto);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
