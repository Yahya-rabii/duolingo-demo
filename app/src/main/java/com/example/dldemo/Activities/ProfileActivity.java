package com.example.dldemo.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dldemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

	private static final String TAG = "ProfileActivity";
	private static final int PICK_IMAGE_REQUEST = 1;

	private EditText mEmailField, mPasswordField, mUsernameField;
	private TextView mFullNameField;
	private ImageView mProfileImage;
	private Button mUpdateButton;
			private ImageButton mChooseImageButton;

	private FirebaseAuth mAuth;
	private DatabaseReference mDatabase;
	private StorageReference mStorage;

	private String mCurrentUserId;
	private Uri mImageUri;

	@SuppressLint({"WrongViewCast", "MissingInflatedId"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_profile);

		mAuth = FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mStorage = FirebaseStorage.getInstance().getReference();

		mFullNameField = findViewById(R.id.fullname_field);
		mEmailField = findViewById(R.id.email_field);
		mPasswordField = findViewById(R.id.password_field);
		mUsernameField = findViewById(R.id.username_field);
		mProfileImage = findViewById(R.id.profileScreenImageView);
		mChooseImageButton = findViewById(R.id.profileScreenImageButton);
		mUpdateButton = findViewById(R.id.update_button);

		mCurrentUserId = mAuth.getCurrentUser().getUid();

		mDatabase.child("users").child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				String fullName = snapshot.child("fullName").getValue().toString();
				String email = snapshot.child("email").getValue().toString();
				String username = snapshot.child("username").getValue().toString();

				mFullNameField.setText(fullName);
				mEmailField.setText(email);
				mUsernameField.setText(username);





				final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Get the Firebase storage reference for the profile image
				FirebaseStorage storage = FirebaseStorage.getInstance();
				StorageReference storageRef = storage.getReference().child("images").child(uid).child("profile.jpg");

// Get the profileImageUrl from the Firebase Realtime Database
				DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
				userRef.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot snapshot) {
						if (snapshot.exists()) {
							String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
							if (profileImageUrl != null) {
								// Load the profile image into the ImageView using Picasso
								Picasso.get().load(profileImageUrl).into(mProfileImage);
							}
						}
					}

					@Override
					public void onCancelled(@NonNull DatabaseError error) {
						Log.d(TAG, "onCancelled: " + error.getMessage());
					}
				});









			}








			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Toast.makeText(ProfileActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
			}
		});

		mChooseImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openFileChooser();
			}
		});

		mUpdateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = mEmailField.getText().toString();
				String password = mPasswordField.getText().toString();
				String username = mUsernameField.getText().toString();

				if (mImageUri != null) {
					uploadImage(email, password, username);
				} else {
					updateProfile(email, password, username, null);
				}
			}
		});
	}

	private void openFileChooser() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			mImageUri = data.getData();
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
				mProfileImage.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void uploadImage(final String email, final String password, final String username) {
		final String imageName = UUID.randomUUID().toString();
		final StorageReference imageRef = mStorage.child("images/" + mCurrentUserId + "/" + imageName);

		imageRef.putFile(mImageUri)
				.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
							@Override
							public void onSuccess(Uri uri) {
								String imageUrl = uri.toString();
								updateProfile(email, password, username, imageUrl);
							}
						});
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(ProfileActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
						Log.d(TAG, "Error uploading image: " + e.getMessage());
					}
				})
				.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
						double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
						Log.d(TAG, "Upload progress: " + progress + "%");
					}
				});
	}

	private void updateProfile(String email, String password, String username, String imageUrl) {
		if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
			Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
			return;
		}

		Map<String, Object> profileUpdates = new HashMap<>();
		profileUpdates.put("email", email);
		profileUpdates.put("username", username);

		mAuth.getCurrentUser().updateEmail(email)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "User email address updated.");
						} else {
							Toast.makeText(ProfileActivity.this, "Error updating email", Toast.LENGTH_SHORT).show();
							Log.d(TAG, "Error updating email: " + task.getException().getMessage());
						}
					}
				});

		if (!TextUtils.isEmpty(password)) {
			mAuth.getCurrentUser().updatePassword(password)
					.addOnCompleteListener(new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							if (task.isSuccessful()) {
								Log.d(TAG, "User password updated.");
							} else {
								Toast.makeText(ProfileActivity.this, "Error updating password", Toast.LENGTH_SHORT).show();
								Log.d(TAG, "Error updating password: " + task.getException().getMessage());
							}
						}
					});
		}

		if (!TextUtils.isEmpty(imageUrl)) {
			profileUpdates.put("profileImageUrl", imageUrl);
		}

		mDatabase.child("users").child(mCurrentUserId).updateChildren(profileUpdates)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {

						if (task.isSuccessful()) {
							Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
							finish();
						} else {
							Toast.makeText(ProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
							Log.d(TAG, "Error updating profile: " + task.getException().getMessage());
						}
					}
				});
	}
}