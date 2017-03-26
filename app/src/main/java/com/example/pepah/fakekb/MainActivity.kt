package com.example.pepah.fakekb

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import android.R.array
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import android.R.string.cancel
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.*


class MainActivity : AppCompatActivity() {

    lateinit var fingerPrintIcon: ImageView
    lateinit var fingerPrintText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        if (intent.action == Intent.ACTION_VIEW) {
            applyIntentContent()
        }
    }

    private fun initUI() {
        setupToolbar()
        setupSpinner()
        setupFinishButton()
    }

    private fun applyIntentContent() {
        if (intent.getStringExtra("target_bank_account") != null) {
            vBankCode.setText(intent.getStringExtra("target_bank_account"))
        }
        vDueDate.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM /* Feb 21, 2017 */).format(Calendar.getInstance().timeInMillis))
        vAmount.setText(BigDecimal(intent.getStringExtra("amount")).formatAmount())
        vCurrency.text = intent.getStringExtra("currency_code")
        vMessage.setText(intent.getStringExtra("payment_reference"))
    }

    private fun setupSpinner() {
        val paymentTypes = listOf("Domestic payment", "European payment(SEPA)")
        val dataAdapterMethods = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentTypes)
        dataAdapterMethods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vSpinnerPaymentType.adapter = dataAdapterMethods

        val accounts = listOf("Default account", "Different account")
        val dataAdapterAccounts = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accounts)
        dataAdapterAccounts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vSpinnerAccounts.adapter = dataAdapterAccounts
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            finish()
        }
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(vToolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupFinishButton() {
        vButtonFinish.setOnClickListener {
            showScanFinger()
        }
    }

    private fun showScanFinger() {
        val builder = AlertDialog.Builder(this)
        val dialog = layoutInflater.inflate(R.layout.custom_dialog, null, false)
        fingerPrintIcon = dialog.findViewById(R.id.vFingerPrintIcon) as ImageView
        fingerPrintText = dialog.findViewById(R.id.vFingerPrintText) as TextView
        builder.setView(dialog)
                .setNegativeButton(R.string.close, DialogInterface.OnClickListener { dialog, id ->
                }).show()
        fingerPrintIcon.postDelayed({ showScanSuccesful() }, 4000)

    }

    private fun showScanSuccesful() {
        fingerPrintIcon.setImageResource(R.drawable.ic_fingerprint_succes)
        fingerPrintText.setText(R.string.fingerprint_reckognized)
        fingerPrintIcon.postDelayed({
            setResult(Activity.RESULT_OK)
            finish()
        }, 4000)
    }

}
