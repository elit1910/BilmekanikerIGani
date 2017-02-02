package com.example.bilmekanikerigani;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;




/**
 * This is a application for a local carshop in Grani Finland.
 * In the wintertime people need to change their car tyres, thus it is very busy time for the
 * business.
 * This app is made in order to better organize the huge demand and optimise
 * the amount of work accordingly.
 */
public class MainActivity extends AppCompatActivity {

    int antalBildack = 4;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked and adding four tyres.
     */
    public void increment(View view) {
        if (antalBildack == 20) {
            return;
        }
        antalBildack = antalBildack + 4;
        visaAntalet(antalBildack);
    }

    /**
     * This method is called when the minus button is clicked and reducing the amount of tyres by 4.
     */
    public void decrement(View view) {
        if (antalBildack == 0) {
            return;
        }
        antalBildack = antalBildack - 4;
        visaAntalet(antalBildack);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get user's name
        EditText namn_fält = (EditText) findViewById(R.id.namn_fält);
        Editable namnEditering = namn_fält.getText();
        String namn = namnEditering.toString();

        // get the time frame
        EditText tid = (EditText) findViewById(R.id.Tidpunkt);
        Editable tidEditering = tid.getText();
        String tidpunkt = tidEditering.toString();

        // get the Date
        EditText datum1 = (EditText) findViewById(R.id.datum);
        Editable datumEditering = datum1.getText();
        String datum = datumEditering.toString();

        // Figure out if the user wants dubbdack
        CheckBox dubbdack = (CheckBox) findViewById(R.id.dubbdack_checkbox);
        boolean dubbdackBolean = dubbdack.isChecked();

        // Figure out if the user wants friktionsdack
        CheckBox friktionsdack = (CheckBox) findViewById(R.id.friktionsdack_checkbox);
        boolean friktionsdackBolean = friktionsdack.isChecked();

        //Only one of the options can be selected
        if (antalBildack == 4){
        if (dubbdackBolean = true ){
            friktionsdackBolean = false;
            friktionsdack.toggle();
        }

        if  (friktionsdackBolean = true){
            dubbdackBolean = false;
            dubbdack.toggle();
        }
        }

        // Calculate the price
        int pris = RaknaPris(dubbdackBolean, friktionsdackBolean);

        // Display the order summary on the screen
        String meddelande = OrderSammanfattning(namn, tidpunkt, datum, pris, dubbdackBolean, friktionsdackBolean);

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","BilmekanikerGrani@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.order_summary_email_subject, namn));
        intent.putExtra(Intent.EXTRA_TEXT, meddelande);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param dubbdack is whether or not we should include "dubbdack"  in the price
     * @param friktionsdack    is whether or not we should include "friktionsdack" topping in the price
     * @return total price
     */
    private int RaknaPris(boolean dubbdack, boolean friktionsdack) {
        // First calculate the price of one cup of coffee
        int grundpris = 20;

        // If the user wants whipped cream, add $1 per cup
        if (dubbdack) {
            grundpris = grundpris + 25;
        }

        // If the user wants chocolate, add $2 per cup
        if (friktionsdack) {
            grundpris = grundpris + 0;
        }

        // Calculate the total order price by multiplying by the quantity
        return antalBildack * grundpris;
    }

    /**
     * Create summary of the order.
     *
     * @param namn on the order
     * @param pris of the order
     * @param dubbdackBolean is whether or not to add "dubbdack" to the coffee
     * @param friktionsdackBolean is whether or not to add "friktionddack" to the coffee
     * @return text summary
     */
    private String OrderSammanfattning(String namn, String tidpunkt, String datum, int pris, boolean dubbdackBolean,
                                      boolean friktionsdackBolean) {
        String meddelande = getString(R.string.Ordersammmanfattning, namn);
        meddelande += "\n" + getString(R.string.OrdersammanfattningDubbDack, dubbdackBolean);
        meddelande += "\n" + getString(R.string.OrdersammanfattningFriktionsDack, friktionsdackBolean);
        meddelande += "\n" + getString(R.string.OrdersammanfattningAntalDack, antalBildack);
        meddelande += "\n" + getString(R.string.OrdersammanfattningPris,
                NumberFormat.getCurrencyInstance(Locale.FRENCH).format(pris));
        meddelande += "\n" + getString(R.string.tidpunkt, tidpunkt );
        meddelande += "\n" + getString(R.string.datum, datum);
        meddelande += "\n" + getString(R.string.tack);
        return meddelande;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void visaAntalet(int antalDack) {
        TextView quantifieraTextView = (TextView) findViewById(
                R.id.quantifiera_text_view);
        quantifieraTextView.setText("" + antalDack);
    }
}