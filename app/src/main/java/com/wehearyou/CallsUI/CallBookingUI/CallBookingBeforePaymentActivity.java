package com.wehearyou.CallsUI.CallBookingUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wehearyou.CallsUI.CallBookingUI.CallBookingAfterPaymentActivity;
import com.wehearyou.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class CallBookingBeforePaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button bookCall;

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_booking_before_payment);

        mixpanel = MixpanelAPI.getInstance(this, "d1729e123d88f668650b9197c333f789\n");

        bookCall = findViewById(R.id.bookCall);

        bookCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject props = new JSONObject();
                try {
                    props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mixpanel.track("Make Payment Clicked in Book Call", props);

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
        checkout.setImage(R.drawable.splash);

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
            options.put("description", "Call Booking");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", String.valueOf(250*100));

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

        mixpanel.track("Payment Successful Event", props);

        Intent intent = new Intent(getApplicationContext(), CallBookingAfterPaymentActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {

        JSONObject props = new JSONObject();
        try {
            props.put("Mobile Number", FirebaseAuth.getInstance().getCurrentUser().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mixpanel.track("Payment Failure Event", props);

        Toast.makeText(getApplicationContext(), "Your payment failed",Toast.LENGTH_SHORT).show();
    }
}