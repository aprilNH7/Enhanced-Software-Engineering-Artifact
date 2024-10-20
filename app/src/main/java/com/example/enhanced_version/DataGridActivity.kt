package com.example.enhanced_version

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DataGridActivity : ComponentActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_grid)

        dbHelper = DatabaseHelper(this)
        val dataRecyclerView = findViewById<RecyclerView>(R.id.dataRecyclerView)
        dataRecyclerView.layoutManager = LinearLayoutManager(this)
        productList = dbHelper.getAllProducts().toMutableList()
        productAdapter = ProductAdapter(productList)
        dataRecyclerView.adapter = productAdapter
        findViewById<Button>(R.id.buttonAddData).setOnClickListener {
            startActivityForResult(Intent(this, AddItemActivity::class.java), REQUEST_CODE_ADD_ITEM)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_ITEM && resultCode == RESULT_OK) {
            refreshProductList()
        }
    }
    private fun refreshProductList() {
        productList.clear()
        productList.addAll(dbHelper.getAllProducts())
        productAdapter.notifyDataSetChanged()
    }

    companion object {
        const val REQUEST_CODE_ADD_ITEM = 1
    }

    class ProductAdapter(private val productList: List<Product>) :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

        class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val productIdTextView: TextView = itemView.findViewById(R.id.textViewProductId)
            val productNameTextView: TextView = itemView.findViewById(R.id.textViewProductName)
            val productQuantityTextView: TextView = itemView.findViewById(R.id.textViewProductQuantity)
            val productPriceTextView: TextView = itemView.findViewById(R.id.textViewProductPrice)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
            return ProductViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = productList[position]
            holder.productIdTextView.text = "ID: ${product.id}"
            holder.productNameTextView.text = product.name
            holder.productQuantityTextView.text = "Quantity: ${product.quantity}"
            holder.productPriceTextView.text = "Price: $${product.price}"
        }

        override fun getItemCount() = productList.size
    }
}
