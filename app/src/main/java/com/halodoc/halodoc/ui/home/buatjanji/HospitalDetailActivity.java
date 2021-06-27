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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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



    }

    private void setServices() {
        //tampilkan kecamatan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialist, android.R.layout.simple_spinner_item);
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

            notes = binding.notesEt.getText().toString().trim();

            Intent intent = new Intent(this, HospitalPaymentActivity.class);
            intent.putExtra(HospitalPaymentActivity.EXTRA_HOSPITAL, hospitalModel);
            intent.putExtra(HospitalPaymentActivity.EXTRA_SERVICES, services);
            intent.putExtra(HospitalPaymentActivity.EXTRA_BOOKING_DATE, bookingDate);
            intent.putExtra(HospitalPaymentActivity.EXTRA_NOTES, notes);
            intent.putExtra(HospitalPaymentActivity.EXTRA_PRICE, price);
            startActivity(intent);
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