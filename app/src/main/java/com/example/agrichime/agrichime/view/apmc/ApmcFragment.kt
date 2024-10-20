package com.example.agrichime.agrichime.view.apmc




import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrichime.R
import com.example.agrichime.agrichime.adapter.ApmcAdapter
import com.example.agrichime.agrichime.model.APMCApi
import com.example.agrichime.agrichime.model.data.APMCCustomRecords
import com.example.agrichime.agrichime.model.data.APMCMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApmcFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApmcFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var adapter: ApmcAdapter
    var indexSpinner1: Int? = null
    var indexSpinner2: Int? = null
    var someMap: Map<Any, Array<String>>? = null
    var states: Array<String>? = null


    private lateinit var progress_apmc: ProgressBar
    private lateinit var loadingTextAPMC: TextView
    private lateinit var dateValueTextApmc: TextView
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var textAPMCWarning: TextView
    private lateinit var recycleAPMC: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        bottomNav.selectedItemId = R.id.bottomNavAPMC

//        getApmc()
        val view = inflater.inflate(R.layout.fragment_apmc, container, false)


        progress_apmc = view.findViewById(R.id.progress_apmc)
        loadingTextAPMC = view.findViewById(R.id.loadingTextAPMC)
        dateValueTextApmc = view.findViewById(R.id.dateValueTextApmc)
        spinner1 = view.findViewById(R.id.spinner1)
        spinner2 = view.findViewById(R.id.spinner2)

        textAPMCWarning = view.findViewById(R.id.textAPMCWarning)
        recycleAPMC = view.findViewById(R.id.recycleAPMC)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progress_apmc.visibility = View.GONE
        loadingTextAPMC.visibility = View.GONE

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "APMC"

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        dateValueTextApmc.text = sdf.format(Date()).toString()

        states = arrayOf(
            "None",
            "Atok",
            "Baguio",
            "Bakun",
            "Bokod",
            "Buguias",
            "Itogon",
            "La Trinidad",
            "Kabayan",
            "Kibungan",
            "Kapangan",
            "Mankayan",
            "Sablan",
            "Tuba",
            "Tublay"
        )

        var districtInAtok: Array<String> = arrayOf(
            "None",
            "Abiang",
            "Caliking",
            "Cattubo",
            "Naguey",
            "Paoay",
            "Pasdong",
            "Poblacion",
            "Topdac"

        )
        var districtInBakun: Array<String> = arrayOf(
            "None",
            "Ampusongan",
            "Bagu",
            "Dalipey",
            "Gambang",
            "Kayapa",
            "Poblacion",
            "Sinacbat"
        )

        var districtInBokod: Array<String> = arrayOf(
            "None",
            "Ambuclao",
            "Bila",
            "Bobok-Bisal",
            "Daclan",
            "Ekip",
            "Karao",
            "Nawal",
            "Pito",
            "Poblacion",
            "Tikey"
        )

        var districtInBuguias: Array<String> = arrayOf(
            "None",
            "Abatan",
            "Amgaleyguey",
            "Amlimay",
            "Baculongan Norte",
            "Baculongan Sur",
            "Bangao",
            "Buyacaoan",
            "Calamagan",
            "Catlubong",
            "Lengaoan",
            "Loo",
            "Natubleng",
            "Poblacion",
            "Sebang"

        )

        var districtInItogon: Array<String> = arrayOf(
            "None",
            "Ampucao",
            "Dalupirip",
            "Gumatdang",
            "Loacan",
            "Poblacion",
            "Tinongdan",
            "Tuding",
            "Ucab",
            "Virac"
        )

        var districtInKabayan: Array<String> = arrayOf(
            "None",
            "Adaoay",
            "Anchukey",
            "Ballay",
            "Bashoy",
            "Batan",
            "Duacan",
            "Eddet",
            "Gusaran",
            "Kabayan Barrio",
            "Lusod",
            "Pacso",
            "Poblacion",
            "Tawangan"
        )

        var districtInKapangan: Array<String> = arrayOf(
            "None",
            "Balakbak",
            "Beleng-Belis",
            "Boklaoan",
            "Cayapes",
            "Cuba",
            "Datakan",
            "Gadang",
            "Gaswiling",
            "Labueg",
            "Paykek",
            "Poblacion Central",
            "Pongayan",
            "Pudong",
            "Sagubo",
            "Taba-ao"
        )

        var emptyDistricts : Array<String> = arrayOf("None")

        var aa = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            states!!
        )

        spinner1.adapter = aa

        someMap = mapOf(
            "Atok" to districtInAtok,
            "Bakun" to districtInBakun,
            "Bokod" to districtInBokod,
            "Buguias" to districtInBuguias,
            "Itogon" to districtInItogon,
            "Kabayan" to districtInKabayan,
            "Kapangan" to districtInKapangan,

            "Baguio" to emptyDistricts,
            "La Trinidad" to emptyDistricts,
            "Mankayan" to emptyDistricts,
            "Sablan" to emptyDistricts,
            "Tuba" to emptyDistricts,
            "Tublay" to emptyDistricts
        )


        spinner1.onItemSelectedListener = object :
            AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    textAPMCWarning.text = "Please Select Municipality and Barangay"
                    recycleAPMC.visibility = View.GONE
                    textAPMCWarning.visibility = View.VISIBLE
                } else {
                    var aa2 = ArrayAdapter(
                        activity!!.applicationContext,
                        android.R.layout.simple_spinner_dropdown_item,

                        someMap!![states!![p2]]!!
                    )

                    indexSpinner1 = p2
                    spinner2.adapter = aa2
                    aa2.notifyDataSetChanged()
                }
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }

        spinner2.onItemSelectedListener = object :
            AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    textAPMCWarning.text = "Please Select District"
                    recycleAPMC.visibility = View.GONE
                    textAPMCWarning.visibility = View.VISIBLE
                } else {
                    textAPMCWarning.visibility = View.GONE
                    if (p2 != 0) {
                        getApmc("${someMap!![states!![indexSpinner1!!]]!![p2]}")
                    }
                    indexSpinner2 = p2
                    progress_apmc.visibility = View.VISIBLE
                    loadingTextAPMC.visibility = View.VISIBLE
                }
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApmcFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApmcFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getApmc(district: String) {
        val apmc1: Call<APMCMain> = APMCApi.apmcInstances.getapmc(20)
        var apmc2: Call<APMCMain>? = null
        if (indexSpinner2 != 0) {

            apmc2 = APMCApi.apmcInstances.getSomeData(district)
            Log.d("APMC District", district)

            apmc2!!.enqueue(object : Callback<APMCMain> {
                override fun onFailure(call: Call<APMCMain>, t: Throwable) {
                    Log.d("PPHPR", "Failed", t)
                    progress_apmc.visibility = View.GONE
                    loadingTextAPMC.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<APMCMain>,
                    response: Response<APMCMain>
                ) {

                    val apmcdata = response.body()
                    if (apmcdata != null) {

                        val updatedYear = apmcdata.updated_date.toString().slice(0..3)
                        val updatedMonth = apmcdata.updated_date.toString().slice(5..6)
                        val updatedDate = apmcdata.updated_date.toString().slice(8..9)


                        dateValueTextApmc.text = "$updatedDate/$updatedMonth/$updatedYear"
                        if (apmcdata.records.size == 0) {
                            progress_apmc.visibility = View.GONE
                            loadingTextAPMC.visibility = View.GONE
                            textAPMCWarning.visibility = View.VISIBLE
                            recycleAPMC.visibility = View.GONE
                            textAPMCWarning.text = "No records found!"
                        } else {
                            textAPMCWarning.visibility = View.GONE
                            recycleAPMC.visibility = View.VISIBLE
                            Log.d("APMCFrag", apmcdata.records.toString())

                            val totalRecords = apmcdata.records.size
                            var firstMarket = ""
                            if (!apmcdata.records[0].market.isNullOrEmpty()) {
                                firstMarket = apmcdata.records[0].market.toString()
                            }

                            val customRecords = ArrayList<APMCCustomRecords>()

                            val list1 = mutableListOf<String>()
                            val list2 = mutableListOf<String>()
                            val list3 = mutableListOf<String>()
                            list1.add(apmcdata.records[0].commodity)
                            list2.add(apmcdata.records[0].min_price)
                            list3.add(apmcdata.records[0].max_price)

                            var previousRecord = APMCCustomRecords(
                                apmcdata.records[0].state,
                                apmcdata.records[0].district,
                                apmcdata.records[0].market,
                                list1,
                                list2,
                                list3
                            )

                            val ss = apmcdata.records[0].market
                            Log.d("PreREc", previousRecord.toString())

                            if (totalRecords == 1) {
                                customRecords.add(previousRecord)
                            } else {
                                var count = 0
                                for (i in 1..totalRecords - 1) {

                                    if (apmcdata.records[i].market == previousRecord.market) {
                                        previousRecord.commodity.add(apmcdata.records[i].commodity)
                                        previousRecord.min_price.add(apmcdata.records[i].min_price)
                                        previousRecord.max_price.add(apmcdata.records[i].max_price)
                                        count = 1
                                    } else {
                                        count = 0
                                        customRecords.add(previousRecord)
                                        list1.add(apmcdata.records[i].commodity)
                                        list2.add(apmcdata.records[i].min_price)
                                        list3.add(apmcdata.records[i].max_price)
                                        previousRecord = APMCCustomRecords(
                                            apmcdata.records[i].state,
                                            apmcdata.records[i].district,
                                            apmcdata.records[i].market,
                                            list1,
                                            list2,
                                            list3
                                        )
                                    }
                                }
                                if (count == 1) {
                                    Log.d("LastRec", "Yes")
                                    customRecords.add(previousRecord)
                                }
                            }

                            Log.d("New APMC Data", customRecords.toString())
                            Log.d("Old APMC Data", apmcdata.toString())

                            adapter = ApmcAdapter(activity!!.applicationContext, customRecords)
                            recycleAPMC.adapter = adapter
                            recycleAPMC.layoutManager =
                                LinearLayoutManager(activity!!.applicationContext)
                            progress_apmc.visibility = View.GONE
                            loadingTextAPMC.visibility = View.GONE
                            Log.d("PHP", apmcdata.toString())
                        }

                    }
                }

            })

        }
    }
}