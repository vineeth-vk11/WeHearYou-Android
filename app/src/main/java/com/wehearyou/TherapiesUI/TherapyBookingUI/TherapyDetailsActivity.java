package com.wehearyou.TherapiesUI.TherapyBookingUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wehearyou.R;
import com.wehearyou.TherapiesUI.TherapyBookingUI.TherapyBookingSuccessfulActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class TherapyDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    String name, cost, description;
    ArrayList<String> images = new ArrayList<>();

    TextView nameText2, costText, descText, toPayText;
    ImageSlider imageSlider;
    Button pay;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_details);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        cost = intent.getStringExtra("cost");
        description = intent.getStringExtra("description");
        images = (ArrayList<String>) intent.getSerializableExtra("images");

        nameText2 = findViewById(R.id.nameText);
        costText = findViewById(R.id.cost);
        descText = findViewById(R.id.description);
        toPayText = findViewById(R.id.textView29);
        pay = findViewById(R.id.book);

        nameText2.setText(name);
        costText.setText("₹ " + cost);
        toPayText.setText("₹ " + cost);
        descText.setText(description);
        imageSlider = findViewById(R.id.image_slider);

        List<SlideModel> slideModels = new ArrayList<>();

        for(int i = 0; i < images.size(); i++){
            slideModels.add(new SlideModel(images.get(i),"", ScaleTypes.CENTER_CROP));
        }

        imageSlider.setImageList(slideModels);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Therapy Make Payment Button Clicked", props);
                startPayment();
            }
        });
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.ic_logonew);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "WeHearYou");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Therapy Product");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", String.valueOf(Integer.parseInt(cost)*100));

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        JSONObject props = new JSONObject();
        try {
            props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mixpanel.track("Therapy Payment Successfully Completed", props);

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String therapyName = name;
        String amountPaid = cost;

        HashMap<String, Object> data = new HashMap<>();
        data.put("therapyName",therapyName);
        data.put("date",date);
        data.put("amountPaid",amountPaid);

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("TherapyBookings").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Intent intent = new Intent(getApplicationContext(), TherapyBookingSuccessfulActivity.class);
                intent.putExtra("cost",cost);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        JSONObject props = new JSONObject();
        try {
            props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mixpanel.track("Therapy Payment Failed", props);
    }
}