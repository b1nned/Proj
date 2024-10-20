package com.example.agrichime.agrichime.view.ecommerce

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrichime.R
import com.example.agrichime.agrichime.adapter.AttributesNormalAdapter
import com.example.agrichime.agrichime.adapter.AttributesSelectionAdapter
import com.example.agrichime.agrichime.model.data.CartItem
import com.example.agrichime.agrichime.utilities.CellClickListener
import com.example.agrichime.agrichime.viewmodel.EcommViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import android.content.Intent
import com.google.firebase.firestore.DocumentSnapshot

class EcommerceItemFragment : Fragment(), CellClickListener {

    private var currentItemId: Any? = null
    private lateinit var viewModel: EcommViewModel
    private lateinit var realtimeDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var loadingText: TextView
    private lateinit var colorButtons: List<ImageButton>
    private lateinit var quantityCountEcomm: TextView
    private lateinit var productTitle: TextView
    private lateinit var productPrice: TextView
    private lateinit var deliveryCost: TextView
    private lateinit var Rating: RatingBar
    private lateinit var recyclerSelectionAttributes: RecyclerView
    private lateinit var recyclerNormalAttributes: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var addToCart: Button
    private lateinit var buyNow: Button

    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(EcommViewModel::class.java)
        realtimeDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ecommerce_item, container, false)

        loadingText = view.findViewById(R.id.loadingText)
        quantityCountEcomm = view.findViewById(R.id.quantityCountEcomm)
        productTitle = view.findViewById(R.id.productTitle)
        productPrice = view.findViewById(R.id.productPrice)
        deliveryCost = view.findViewById(R.id.deliverycost)
        Rating = view.findViewById(R.id.Rating)
        recyclerSelectionAttributes = view.findViewById(R.id.recyclerSelectionAttributes)
        recyclerNormalAttributes = view.findViewById(R.id.recyclerNormalAttributes)
        progressBar = view.findViewById(R.id.progress_ecommItem)
        addToCart = view.findViewById(R.id.addToCart)
        buyNow = view.findViewById(R.id.buynow)

        colorButtons = listOf(
            view.findViewById(R.id.color1),
            view.findViewById(R.id.color2),
            view.findViewById(R.id.color3),
            view.findViewById(R.id.color4)
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "E-Commerce"

        setupColorButtons()
        setupQuantityButtons()
        loadData()
        setupAddToCart()
        setupBuyNow()
    }

    private fun setupColorButtons() {
        colorButtons.forEach { button ->
            button.setOnClickListener {
                highlightSelectedColor(button)
            }
        }
    }

    private fun highlightSelectedColor(selectedButton: ImageButton) {
        colorButtons.forEach { button ->
            val params = button.layoutParams
            if (button == selectedButton) {
                params.width = (40 * resources.displayMetrics.density).toInt()
                params.height = (35 * resources.displayMetrics.density).toInt()
            } else {
                params.width = (30 * resources.displayMetrics.density).toInt()
                params.height = (25 * resources.displayMetrics.density).toInt()
            }
            button.layoutParams = params
        }
    }

    private fun setupQuantityButtons() {
        view?.findViewById<ImageButton>(R.id.increaseQtyBtn)?.setOnClickListener {
            quantityCountEcomm.text = (quantityCountEcomm.text.toString().toInt() + 1).toString()
        }

        view?.findViewById<Button>(R.id.decreaseQtyBtn)?.setOnClickListener {
            if (quantityCountEcomm.text.toString().toInt() > 1) {
                quantityCountEcomm.text = (quantityCountEcomm.text.toString().toInt() - 1).toString()
            }
        }
    }

    private fun loadData() {
        loadingText.text = "Loading..."
        viewModel.ecommLiveData.value?.forEach {
            if (it.id == this.tag) {
                currentItemId = it.id!!
                productTitle.text = it.getString("title")
                productPrice.text = "₹${it.getString("price")}"
                deliveryCost.text = it.getString("delCharge")
                Rating.rating = it.get("rating").toString().toFloat()

                setupAttributes(it)
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
            }
        }
    }

    private fun setupAttributes(product: DocumentSnapshot) {
        val attributes = product.get("attributes") as Map<String, Any>
        val selectionAttributes = mutableListOf<MutableMap<String, Any>>()
        val normalAttributes = mutableListOf<MutableMap<String, Any>>()

        for ((key, value) in attributes) {
            if (value is ArrayList<*> && key != "Color") {
                selectionAttributes.add(mutableMapOf(key to value))
            } else if (value is String) {
                normalAttributes.add(mutableMapOf(key to value))
            }
        }

        recyclerSelectionAttributes.adapter = AttributesSelectionAdapter(requireActivity(), selectionAttributes, this)
        recyclerSelectionAttributes.layoutManager = LinearLayoutManager(requireActivity())

        recyclerNormalAttributes.adapter = AttributesNormalAdapter(requireActivity(), normalAttributes)
        recyclerNormalAttributes.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setupAddToCart() {
        addToCart.setOnClickListener {
            addToCart.isClickable = false
            progressBar.visibility = View.VISIBLE
            loadingText.text = "Adding to Cart..."
            loadingText.visibility = View.GONE

            val realtimeRef = realtimeDatabase.getReference("${firebaseAuth.currentUser!!.uid}/cart/$currentItemId")
            val quantity = quantityCountEcomm.text.toString().toInt()
            val cartItem = CartItem(quantity, sdf.format(Date()))

            realtimeRef.setValue(cartItem).addOnCompleteListener {
                Toast.makeText(requireActivity(), "Item Added", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                addToCart.isClickable = true
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "Please Try Again!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
                addToCart.isClickable = true
            }
        }
    }

    private fun setupBuyNow() {
        buyNow.setOnClickListener {
            val productPriceValue = productPrice.text.toString().replace("₹", "").toInt()
            val totalPrice = quantityCountEcomm.text.toString().toInt() * productPriceValue + deliveryCost.text.toString().toInt()

            val intent = Intent(requireActivity(), RazorPayActivity::class.java).apply {
                putExtra("productId", currentItemId.toString())
                putExtra("itemCost", productPriceValue.toString())
                putExtra("quantity", quantityCountEcomm.text.toString())
                putExtra("deliveryCost", deliveryCost.text.toString())
            }
            startActivity(intent)
        }
    }

    override val RazorPayActivity: Unit
        get() = TODO("Not yet implemented")

    override fun onCellClickListener(name: String) {
        Log.d("EcommItem", name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cart_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.cart_item) {
            val cartFragment = CartFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, cartFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("cart")
                .commit()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
