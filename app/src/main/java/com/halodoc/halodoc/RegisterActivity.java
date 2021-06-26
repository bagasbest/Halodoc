package com.halodoc.halodoc;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.ActivityRegisterBinding;
import com.halodoc.halodoc.utils.MonthPicker;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // INISIASI PENGAMBILAN TANGGAL LAHIR
        initDatePicker();

        // KETIKA KEMBALI KE HALAMAN LOGIN
        binding.backToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        // KETIKA KLIK INPUT TANGGAL LAHIR
        binding.button.setOnClickListener(view -> {
            datePickerDialog.show();
            binding.button.setText(getTodayDate());
        });

        // KETIKA KLIK MENDAFTAR
        binding.registerBtn.setOnClickListener(view -> {
            // VALIDASI SEMUA INPUT FORM
            validateFom();
        });
    }




    private void validateFom() {
        String name = binding.nameEt.getText().toString().trim();
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();
        String noHp = binding.phoneEt.getText().toString().trim();
        String dob = getTodayDate();
        String heightBody = binding.heightEt.getText().toString().trim();
        String weightBody = binding.weightEt.getText().toString().trim();

        // PILIH JENIS KELAMIN
        int selectId = binding.radioGroup2.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectId);


        // VALIDASI APAKAH SELURUH FORM TERISI DENGAN BENAR
        if (name.isEmpty()) {
            binding.nameEt.setError("Nama Lengkap tidak boleh kosong");
            return;
        } else if (email.isEmpty()) {
            binding.emailEt.setError("Email tidak boleh kosong");
            return;
        } else if (!email.contains("@") || !email.contains(".")) {
            binding.emailEt.setError("Format Email tidak sesuai");
            return;
        } else if (password.isEmpty()) {
            binding.passwordEt.setError("Password tidak boleh kosong");
            return;
        } else if (noHp.isEmpty()) {
            binding.phoneEt.setError("Nomor Telepon tidak boleh kosong");
            return;
        } else if (heightBody.isEmpty()) {
            binding.heightEt.setError("Tinggi Badan tidak boleh kosong");
            return;
        } else if (weightBody.isEmpty()) {
            binding.weightEt.setError("Berat Badan tidak boleh kosong");
            return;
        } else if (radioButton.getText().toString().isEmpty()) {
            Toast.makeText(this, "Jenis Kelamin tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(dob.isEmpty()) {
            Toast.makeText(this, "Tanggal Lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }


        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // simpan data pengguna ke database
                        saveUserData(name, email, noHp, dob, radioButton.getText().toString(), heightBody, weightBody);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.e("Error Register", Objects.requireNonNull(task.getException()).toString());
                        Toast.makeText(RegisterActivity.this, "Ups, terdapat kendala ketika ingin mendaftar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String name, String email, String noHp, String dob, String gender, String height, String weight) {

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Map<String, Object> users = new HashMap<>();
        users.put("name", name);
        users.put("email", email);
        users.put("uid", uid);
        users.put("role", "user");
        users.put("phone", noHp);
        users.put("dob", dob);
        users.put("gender", gender);
        users.put("height", height);
        users.put("weight", weight);
        users.put("userDp", "");

        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(users)
                .addOnSuccessListener(unused -> {
                    // sembunyikan progress bar untuk selesai loading
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Selamat, anda berhasil terdapftar pada Halodoc", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // sembunyikan progress bar untuk selesai loading
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Anda tidak berhasil mendaftar", Toast.LENGTH_SHORT).show();
                });

    }

    // AMBIL TANGGAL SEKARANG
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        mon = mon + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, mon, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, mon, day) -> {
             mon = mon + 1;

            String date = makeDateString(day, mon, year);
            binding.button.setText(date);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, mon, day);
    }

    private String makeDateString(int day, int mon, int year) {
        return day + " " + MonthPicker.getMonFormat(mon) + " " + year;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}