package com.minhthieu.instagramofficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import ir.shahabazimi.instagrampicker.InstagramPicker;

public class PostActivity extends AppCompatActivity {

    private ImageView uploadPostBt;
    private ImageView backBt;
    private EditText imageCaption;
    // Uri
    // cái Uri này dùng local trong chương trình thôi
    private Uri imageUri;
    // cái này là cái link trên mạng luôn nè, upload lên mạng thì có link
    // nó vẫn đc gọi là Uri do Uri bao luôn Url mà, nên kệ cmn đi
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Anh Xa
        AnhXa();

        // cài này sẽ start cái library pick ảnh và tống nó lên imageView
        StartTheGallery();

        // Contain OnClickListener
        ContainOnClickListener();

    }

    private void ContainOnClickListener() {
        // upload Bt
        uploadPostBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadToStorage();
            }
        });

        // back Bt
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void UploadToStorage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + ".jpg");

            StorageTask uploadImage = storageRef.putFile(imageUri);

            uploadImage.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();

                        // đặt cái đường dẫn này vào cái real time databse
                        // cái storage để chứa ảnh thôi, cái real time ms chưá full
                        Toast.makeText(PostActivity.this, "Upload to storage succesul", Toast.LENGTH_SHORT).show();


                        uploadToRealTimeDatabase();
                        progressDialog.dismiss();


                    } else {
                        Toast.makeText(PostActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void uploadToRealTimeDatabase() {
        DatabaseReference realTimeRef = FirebaseDatabase.getInstance().getReference("Posts");

        String postId = realTimeRef.push().getKey();
        String caption = imageCaption.getText().toString().trim();

        HashMap<String, Object> imageDetail = new HashMap<>();
        imageDetail.put("postId", postId);
        imageDetail.put("imageUrl", downloadUri.toString());
        imageDetail.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageDetail.put("imageCaption", caption);
        imageDetail.put("likeCount", 0);

        assert postId != null;
        realTimeRef.child(postId).setValue(imageDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(PostActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getImageExtension() {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));
    }


    private void StartTheGallery() {
        // start the post library
        InstagramPicker in = new InstagramPicker(PostActivity.this);
        in.show('1', '1', address -> {
            // get the image Uri from the library
            imageUri = Uri.parse(address);


            // put the image to container and be ready to post
            ImageView imageToPost = (ImageView) findViewById(R.id.image_to_post);
            imageToPost.setImageURI(imageUri);

        });
    }

    private void AnhXa() {
        uploadPostBt = (ImageView) findViewById(R.id.upload_post_bt);
        backBt = (ImageView) findViewById(R.id.back_bt);
        imageCaption = (EditText) findViewById(R.id.image_caption);
    }

}