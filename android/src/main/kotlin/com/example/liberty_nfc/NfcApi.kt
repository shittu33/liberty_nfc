package com.example.liberty_nfc

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.liberty_nfc.nfc_engine.ActivityUtil
import com.example.liberty_nfc.nfc_engine.MyProvider
import com.example.liberty_nfc.nfc_engine.NfcManager
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.parser.EmvTemplate
import dev.flutter.nfc.Pigeon
import net.sf.scuba.util.Hex
import java.util.*

class NfcApi : Pigeon.NfcApi {
    lateinit var mNfcAdapter: NfcAdapter
    private lateinit var activity: Activity
    lateinit var nfcManager: NfcManager

    var result: Pigeon.Result<Pigeon.NfcResult>? = null

    //    @RequiresApi(Build.VERSION_CODES.M)
    fun init(activity: Activity) {
        this.activity = activity

        try {

            Log.i("Liberty", "Start initialization")
            nfcManager = NfcManager(activity, this::onTagDiscovered)
            Log.i("Liberty", "after manager")

            val nfcManager =
                activity.application.getSystemService(Context.NFC_SERVICE) as android.nfc.NfcManager
            Log.i("Liberty", "after service manager")

            mNfcAdapter = nfcManager.defaultAdapter
            Log.i("Liberty", "after adapter")

        } catch (e: Exception) {
            Log.e("Liberty", e.message.toString())
//            result?.error(e)
        }
        nfcManager.onResume()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onResume() {
        ActivityUtil.enableNfcForegroundDispatch(activity, mNfcAdapter)
    }

    fun onPause() {
        ActivityUtil.disableNfcForegroundDispatch(activity, mNfcAdapter)
        nfcManager.onPause()
    }

    override fun scanNfcTag(result: Pigeon.Result<Pigeon.NfcResult>?) {
        Log.i("Liberty", "Start Scanning")


        this.result = result
        Log.i("Liberty", "onScanNFCTAG RESULT>>>")

    }

    private fun onTagDiscovered(tag: Tag?) {
        Log.i("Liberty", "....start Discovery.....");

        // get IsoDep handle and run xcvr thread
        val isoDep = IsoDep.get(tag)
        if (isoDep == null) {
            Log.i("Liberty", "isoDep is null");
//            ActivityUtil.showToast(activity, "error")
        } else {

            isoDep.connect()
            val mProvider = MyProvider()
            // Define config
            mProvider.setmTagCom(isoDep)
            val config: EmvTemplate.Config = EmvTemplate.Config()
                .setContactLess(true) // Enable contact less reading (default: true)
                .setReadAllAids(true) // Read all aids in card (default: true)
                .setReadTransactions(true) // Read all transactions (default: true)
                .setReadCplc(false) // Read and extract CPCLC data (default: false)
                .setRemoveDefaultParsers(false) // Remove default parsers for GeldKarte and EmvCard (default: false)
                .setReadAt(true) // Read and extract ATR/ATS and description


            // Create Parser

            val parser = EmvTemplate.Builder() //
                .setProvider(mProvider) // Define provider
                .setConfig(config) // Define config
                //.setTerminal(terminal) (optional) you can define a custom terminal implementation to create APDU
                .build()

            // Read card

            Log.e("Liberty", "Before card")
            val card: EmvCard = parser.readEmvCard()
            Log.e("card track2 number", card.track2.cardNumber)
            val cardNumber = card.track2.cardNumber
//            val cardExpiry = card.expireDate.toString()
            val card_Expiry = Hex.toHexString(card.track2.raw).split("D")[1]
            var expiry = card_Expiry.substring(0, 4)
            val aid = card.type.aid[0]
            val cardType = card.type.toString()

            Log.i("card expiry", card.track2.expireDate.time.toString())
            Log.i("card raw", Hex.toHexString(card.track2.raw))
            Log.i("card service code 1", card.track2.service.serviceCode1.technology)
            Log.i(
                "card service code 2", card.track2.service.serviceCode2.authorizationProcessing +
                        card.track2.service.serviceCode2.name
            )
            Log.i("card service 3", card.track2.service.serviceCode3.pinRequirements)
            Log.i("card service 3", card.track2.service.serviceCode3.allowedServices)

            var appendData =
                "Card Number: $cardNumber \nExpiry: $expiry\nAID: $aid\nCard Type: $cardType"

            val calendar = Calendar.getInstance().apply { time = card.expireDate }

            val cardData = Pigeon.NfcResult().apply {
                this.cardNumber = cardNumber
                this.aid = aid
                this.cardType = cardType
                this.expiryDate = Pigeon.Date().apply {
                    this.month = calendar.get(Calendar.MONTH).toString()
                    this.year = calendar.get(Calendar.YEAR).toString()
                }
            }
            val cardTag = "card data"

            cardData.apply {
                Log.i(cardTag, "...CARD DATA...")
                Log.i(cardTag, "Card Number is: $cardNumber")

            }

            result?.success(cardData)

//            ActivityUtil.showToast(activity, appendData);


        }
    }

}