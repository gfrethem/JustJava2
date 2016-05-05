package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {

    int quantity = 2;
    boolean hasWhip = false;
    boolean hasChoc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * This method is called when the Whipped cream checkbox is clicked.
     */
    public void addWhip(View view) {
        CheckBox whippedCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        hasWhip = whippedCheckbox.isChecked();
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (quantity == 10) {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.noMoreThanTen);
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();
        } else {
            quantity = quantity + 1;
            displayQuantity(quantity);
        }
    }

    /**
     * This method is called when the - button is clicked.
     */

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, R.string.noLessThanOne, Toast.LENGTH_SHORT).show();
        } else {
            quantity = quantity - 1;
            displayQuantity(quantity);
        }
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText name = (EditText) findViewById(R.id.customer_name);
        String name_string = name.getText().toString();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        hasChoc = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhip, hasChoc);

        String priceMessage = createOrderSummary(name_string, price, hasWhip, hasChoc);
        String[] emails = {"gfrethem@gmail.com"};
        composeEmail(emails, getString(R.string.emailSubject) + name_string, priceMessage);
    }

    /**
     * This method calculates the price of an order
     *
     * @param hasWhip Order has Whipped cream (T/F)
     * @param hasChoc Order has Chocolate (T/F)
     * @return price of an order
     */
    private int calculatePrice(boolean hasWhip, boolean hasChoc) {

        int pricePerCup = 5;

        if (hasWhip) {
            pricePerCup += 1;
        }

        if (hasChoc) {
            pricePerCup += 2;
        }

        return quantity * pricePerCup;
    }

    /**
     * This method creates an order summary
     *
     * @param name    of customer
     * @param price   of order
     * @param hasWhip Order has Whipped cream (T/F)
     * @param hasChoc Order has Chocolate (T/F)
     * @return String Order Summary
     */
    private String createOrderSummary(String name, int price, boolean hasWhip, boolean hasChoc) {
        String orderSummary = "Name: " + name;
        orderSummary += getString(R.string.addWhippedCream) + hasWhip;
        orderSummary += getString(R.string.addChocolate) + hasChoc;
        orderSummary = orderSummary + "\n" + getString(R.string.quantity) + ": " + quantity;
        orderSummary = orderSummary + getString(R.string.total) + price;
        orderSummary = orderSummary + getString(R.string.thankYou);
        return orderSummary;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method sends an email with the order
     *
     * @param addresses Email addresses to be sent to
     * @param subject Subject Line of email
     * @param order_text Text of the email containing the Order
     */
    public void composeEmail(String[] addresses, String subject, String order_text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, order_text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}