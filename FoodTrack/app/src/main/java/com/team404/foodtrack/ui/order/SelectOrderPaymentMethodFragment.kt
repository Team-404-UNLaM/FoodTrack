package com.team404.foodtrack.ui.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.gson.GsonBuilder
import com.team404.foodtrack.R
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.databinding.FragmentSelectOrderPaymentMethodBinding
import com.team404.foodtrack.domain.repositories.MarketRepository
import com.team404.foodtrack.domain.repositories.ProductRepository
import org.koin.android.ext.android.inject

class SelectOrderPaymentMethodFragment : Fragment() {

    private var _binding: FragmentSelectOrderPaymentMethodBinding? = null
    private val marketRepository: MarketRepository by inject()
    private val productRepository: ProductRepository by inject()
    private lateinit var order: Order.Builder

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectOrderPaymentMethodBinding.inflate(inflater, container, false)
        val root: View = binding.root

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order.Builder::class.java)
        makeMessageToSendByWhatsApp(order)
        setUpListeners()

        return root
    }

    private fun makeMessageToSendByWhatsApp(order: Order.Builder?) {
        if(order !=null){
            val messageToSend =
                "Hola ${getMarketName(order.marketId)}\nAcabo de realizar el pedido #${order.id} de:\n${
                    getProducts(order.products)
                }\nPor la cantidad de: $${order.totalPrice}\n\nEquipo de Food Track"
            val marketNumber = getMarketNumber(order.marketId)
            if (marketNumber != null) {
                sendMessageByWhatsApp(messageToSend, marketNumber)
            }
        }
    }

    private fun sendMessageByWhatsApp(messageToSend: String, marketNumber: String) {
        val whatsAppIntent = Intent(Intent.ACTION_SEND)
        whatsAppIntent.type = "text/plain"
        whatsAppIntent.putExtra(Intent.EXTRA_TEXT, messageToSend)
        whatsAppIntent.putExtra("jid", "$marketNumber@s.whatsapp.net")
        whatsAppIntent.setPackage("com.whatsapp")
        startActivity(whatsAppIntent)
    }

    private fun getMarketName(marketId: Long?): String? {
        return marketId?.let { marketRepository.searchById(it).name }
    }

    private fun getMarketNumber(marketId: Long?): String? {
        return marketId?.let { marketRepository.searchById(it) }?.cellPhone?.replace("+", "")
            ?.replace(" ", "")
    }

    private fun getProducts(products: MutableMap<Long, Int>?): String {
        var productsMessage = ""
        products?.forEach { (key,value) ->
            val productToAddToProductList = "${replaceProductKeyForProductName(key)} $value"+"u."+"\n"
            productsMessage = productsMessage.plus(productToAddToProductList)
        }
        return productsMessage
    }

    private fun replaceProductKeyForProductName(key: Long): String? {
        return productRepository.searchById(key).name
    }

    private fun setUpListeners() {
        binding.btnGoToProductSelect.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_selectOrderPaymentMethodFragment_to_selectOrderProductsFragment, bundle)
        }
    }
}