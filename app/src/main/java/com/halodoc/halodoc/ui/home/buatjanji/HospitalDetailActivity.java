package com.halodoc.halodoc.ui.home.buatjanji;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityHospitalDetailBinding;
import com.halodoc.halodoc.utils.DatePickerFragment;
import com.halodoc.halodoc.utils.MonthPicker;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class HospitalDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityHospitalDetailBinding binding;
    public static final String EXTRA_HOSPITAL = "hospital";

    private String name;
    private String location;
    private String type;
    private String dp;
    private String about;
    private String uid;
    private String bookingDate;
    private String services;
    private String notes;
    private String price;

    private List<String> servicesAvailable;

    private HospitalModel hospitalModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dapatkan atribut dari rumah sakit yang di klik pengguna
        hospitalModel = getIntent().getParcelableExtra(EXTRA_HOSPITAL);
        name = hospitalModel.getName();
        location = hospitalModel.getLocation();
        type = hospitalModel.getType();
        dp = hospitalModel.getDp();
        about = hospitalModel.getAbout();
        uid = hospitalModel.getUid();
        servicesAvailable = hospitalModel.getServices();


        binding.title.setText(name);
        binding.type.setText(type);
        binding.description.setText(about);

        Glide.with(this)
                .load(dp)
                .into(binding.imageView8);



        //klik booking date
        clickBookingDate();

        // KEMBALI KE HALAMAN SEBELUMNYA
        backButton();

        // KLIK SELANJUTNYA
        clickNext();

        // PILIH LAYANAN
        setServices();

        // LOGIN BUTTON
        binding.loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


    }

    private void setServices() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, servicesAvailable);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.servicesEt.setAdapter(adapter);
        binding.servicesEt.setOnItemClickListener((adapterView, view, i, l) -> {
            services = binding.servicesEt.getText().toString();
            getPriceByService(services);
        });
    }

    @SuppressLint("SetTextI18n")
    private void getPriceByService(String services) {
        FirebaseFirestore
                .getInstance()
                .collection("price")
                .document(services)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    price = "" + documentSnapshot.get("price");
                    binding.price.setText("Biaya: Rp. " + price);
                });
    }

    private void clickNext() {
        binding.nextBtn.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                notes = binding.notesEt.getText().toString().trim();
                if(services == null){
                    Toast.makeText(this, "Mohon pilih pelayanan yang anda inginkan", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(bookingDate == null) {
                    Toast.makeText(this, "Mohon pilih tanggal yang anda inginkan", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(notes.isEmpty()) {
                    Toast.makeText(this, "Mohon masukkan catatan", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, HospitalPaymentActivity.class);
                intent.putExtra(HospitalPaymentActivity.EXTRA_HOSPITAL, hospitalModel);
                intent.putExtra(HospitalPaymentActivity.EXTRA_SERVICES, services);
                intent.putExtra(HospitalPaymentActivity.EXTRA_BOOKING_DATE, bookingDate);
                intent.putExtra(HospitalPaymentActivity.EXTRA_NOTES, notes);
                intent.putExtra(HospitalPaymentActivity.EXTRA_PRICE, price);
                startActivity(intent);
            } else {
                binding.notLogin.setVisibility(View.VISIBLE);
                binding.detailHospital.setVisibility(View.GONE);
            }


        });
    }

    private void clickBookingDate() {
        binding.bookingDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });
    }

    private void backButton() {
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        bookingDate = makeDateString(day, month+1, year);
        binding.bookingDate.setText(bookingDate);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + MonthPicker.getMonFormat(month) + " " + year;
    }
}