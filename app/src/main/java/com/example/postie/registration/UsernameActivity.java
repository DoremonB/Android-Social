package com.example.postie.registration;
//https://github.com/ArthurHub/Android-Image-Cropper
//https://github.com/bumptech/glide
//https://github.com/Karumi/Dexter
//https://github.com/hdodenhof/CircleImageView
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.postie.MainActivity;
import com.example.postie.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsernameActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button removeBtn,createAccountBtn;
    private EditText username;
    private ProgressBar progressBar;
    private Uri photoUri;

    public final static String USERNAME_PATTERN="^[a-z0-9_-]{3,15}$";

    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        init();
        storageRef=FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(UsernameActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            selectImage();            
                        }
                        else{
                            Toast.makeText(UsernameActivity.this, "Please allow Permissions", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
                
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUri=null;
                profileImageView.setImageResource(R.drawable.default_profile_pic);
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setError(null);
                if(username.getText().toString().isEmpty() || username.getText().toString().length()<3 ||username.getText().toString().length()>15){
                    username.setError("Length should be between 3 to 15");
                    return;
                }

                if(!username.getText().toString().matches(USERNAME_PATTERN)){
                    username.setError("Only \"a-z,0-9,_,-\" characters are allowed.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firestore.collection("users")
                        .whereEqualTo("username",username.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    List<DocumentSnapshot> documents=task.getResult().getDocuments();
                                    if(documents.isEmpty()){
                                        uploadData();
                                    }
                                    else{
                                        progressBar.setVisibility(View.INVISIBLE);
                                        username.setError("Already exists");
                                        return;
                                    }
                                }
                                else{
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error=task.getException().getMessage();
                                    Toast.makeText(UsernameActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void init(){
        profileImageView=findViewById(R.id.profile_image);
        removeBtn=findViewById(R.id.remove_btn);
        createAccountBtn=findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progressBar);
        username=findViewById(R.id.username);
    }

    private void selectImage(){
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.colorAccent))
                .setActivityTitle("Profile Photo")
                .setFixAspectRatio(true)
                .setAspectRatio(1,1)
                .start(this);
    }

    private void uploadData(){
        if(photoUri!=null){//upload profile(image ,photo)
            final StorageReference ref = storageRef.child("profiles/"+firebaseAuth.getCurrentUser().getUid());
            final UploadTask uploadTask = ref.putFile(photoUri);



            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        String error=task.getException().getMessage();
                        Toast.makeText(UsernameActivity.this, error, Toast.LENGTH_SHORT).show();
                        throw task.getException();

                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
//                        url=ref.getDownloadUrl().toString();
                        url=downloadUri.toString();
                        uploadUsername();
                    } else {
                        // Handle failures
                        progressBar.setVisibility(View.INVISIBLE);
                        String error=task.getException().getMessage();
                        Toast.makeText(UsernameActivity.this, error, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else{//upload urname only
            uploadUsername();
        }
    }
    private void uploadUsername(){
        Map<String,Object> map=new HashMap<>();
        map.put("username",username.getText().toString());
        map.put("profile_Url",url);

        firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent mainIntent=new Intent(UsernameActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                        else{
                            progressBar.setVisibility(View.INVISIBLE);
                            String error=task.getException().getMessage();
                            Toast.makeText(UsernameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();

                Glide
                    .with(this)
                    .load(photoUri)
                    .centerCrop()
                    .placeholder(R.drawable.default_profile_pic)
                    .into(profileImageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
