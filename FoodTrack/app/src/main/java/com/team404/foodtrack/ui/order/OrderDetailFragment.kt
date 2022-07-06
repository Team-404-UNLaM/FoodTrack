package com.team404.foodtrack.ui.order

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.team404.foodtrack.R
import com.team404.foodtrack.configuration.FoodTrackDB
import com.team404.foodtrack.data.Order
import com.team404.foodtrack.data.Product
import com.team404.foodtrack.data.database.MarketFavorites
import com.team404.foodtrack.databinding.FragmentOrderDetailBinding
import com.team404.foodtrack.domain.adapters.ProductsAdapter
import com.team404.foodtrack.domain.mappers.MinifiedOrderMapper
import com.team404.foodtrack.domain.repositories.*
import com.team404.foodtrack.utils.DateAndTime
import com.team404.foodtrack.utils.buildCouponValidForMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val marketRepository : MarketRepository by inject()
    private val couponRepository : CouponRepository by inject()
    private val paymentMethodRepository : PaymentMethodRepository by inject()
    private val productRepository : ProductRepository by inject()
    private val consumptionModeRepository : ConsumptionModeRepository by inject()
    private val minifiedOrderMapper: MinifiedOrderMapper by inject()
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var selectedProductsMap: MutableMap<Long, Product>
    private lateinit var order: Order.Builder
    private lateinit var room: FoodTrackDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        order = GsonBuilder().create().fromJson(arguments?.getString("order"), Order.Builder::class.java)

        room = FoodTrackDB.getDatabase(requireContext())

        injectDependencies()
        setUpListeners()
        setOrderDataInScreen()
        setUpRecyclerView()

        return root
    }

    private fun setUpListeners() {
        binding.btnGoToPaymentMethod.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("order", GsonBuilder().create().toJson(order))
            Navigation.findNavController(it).navigate(R.id.action_orderDetailFragment_to_selectPaymentMethodFragment, bundle)
        }

        binding.btnSendOrder.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val orderRepository = OrderRepository(room.minifiedOrderDao())
                order.date(DateAndTime.getDate())
                val minifiedOrder = minifiedOrderMapper.map(order.build())
                val orderId = orderRepository.insert(minifiedOrder)
                makeMessageToSendByWhatsApp(order.id(orderId).build())

                withContext(Dispatchers.Main) {
                    Navigation.findNavController(it).navigate(R.id.action_orderDetailFragment_to_ordersHistoryFragment)
                }
            }
        }
    }

    private fun injectDependencies() {
        productsAdapter = ProductsAdapter()
    }

    private fun setUpRecyclerView() {
        binding.rvSelectedProducts.also {
            it.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
            it.setHasFixedSize(true)
            it.adapter = productsAdapter
        }

        getSelectedProductsMap()

        productsAdapter.updateProductsSelectedList(selectedProductsMap, order.products)
        productsAdapter.notifyDataSetChanged()
    }

    fun setOrderDataInScreen() {
        setMarketData()
        setConsumptionModeData()
        setCouponDataIfApplies()
        setPaymentMethodData()
        setPricesData()
    }

    fun setMarketData() {
        order.marketId?.let {
            val market = marketRepository.searchById(it)

            binding.marketName.text = market.name

            if (market.marketImg != null) {
                Picasso.get()
                    .load(market.marketImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgMarket)
            }
        }
    }

    fun setConsumptionModeData() {
        order.consumptionModeId?.let {
            val consumptionMode = consumptionModeRepository.searchById(it)

            binding.consumptionModeName.text = consumptionMode.name

            if (consumptionMode.consumptionModeImg != null) {
                Picasso.get()
                    .load(consumptionMode.consumptionModeImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgConsumptionMode)
            }
        }
    }

    fun setCouponDataIfApplies() {
        if (order.appliedCouponId != null) {
            binding.couponCard.visibility = View.VISIBLE

            val coupon = couponRepository.searchById(order.appliedCouponId!!)

            binding.couponName.text = coupon.name
            binding.validFor.text = buildCouponValidForMessage(coupon)
            binding.discountValue.text = "${coupon.discount!!.times(100).toInt()}%"

            if (coupon.couponImg != null) {
                Picasso.get()
                    .load(coupon.couponImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.couponImg)
            }
        } else {
            binding.couponCard.visibility = View.GONE
        }
    }

    fun setPaymentMethodData() {
        order.paymentMethodId?.let {
            val paymentMethod = paymentMethodRepository.searchById(it)

            binding.paymentMethodName.text = paymentMethod.name

            if (paymentMethod.paymentMethodImg != null) {
                Picasso.get()
                    .load(paymentMethod.paymentMethodImg)
                    .placeholder(R.drawable.ic_market)
                    .error(R.drawable.ic_no_image)
                    .into(binding.imgPaymentMethod)
            }
        }
    }

    fun setPricesData() {
        binding.totalValue.text = "$ ${order.totalPrice}"

        if (order.discountedPrice != null) {
            binding.totalValue.setBackgroundResource(R.drawable.line)

            binding.discountedTotalValue.visibility = View.VISIBLE
            binding.discountedTotalValue.text = "$${order.discountedPrice}"
        } else {
            binding.discountedTotalValue.visibility = View.GONE
        }
    }

    private fun getSelectedProductsMap() {
        selectedProductsMap = mutableMapOf()

        order.products!!.keys.forEach { productId ->
            val product = productRepository.searchById(productId)
            selectedProductsMap[productId] = product
        }
    }

    private fun makeMessageToSendByWhatsApp(order: Order) {
        val market = marketRepository.searchById(order.marketId!!)
        val consumptionMode = consumptionModeRepository.searchById(order.consumptionModeId!!)
        val paymentMethod = paymentMethodRepository.searchById(order.paymentMethodId!!)

        val messageToSend = """
                    #Hola ${market.name}${String(Character.toChars(0x2757))}
                    #Acabo de realizar un pedido, este es el detalle:
                    #
                    #Orden #${order.id}
                    #${DateAndTime.getDateAndTime()}
                    #
                    #${getProducts()}
                    #Modo de consumisión:
                    #${String(Character.toChars(0x1F4CC))}${consumptionMode.name}
                    #Forma de pago:
                    #${String(Character.toChars(0x1F4CC))}${paymentMethod.name}
                    #${getCouponMessageIfApplies()}
                    #${getOrderPricesMessage()}
                    #
                    #Equipo de Food Track
                """.trimMargin("#")

        if (market.cellPhone != null) {
            val marketNumber = formatMarketNumber(market.cellPhone)

            sendMessageByWhatsApp(messageToSend, marketNumber)
        }
    }

    private fun getOrderPricesMessage(): String {
        var pricesMessage = """
                        #Total del pedido: ${String(Character.toChars(0x1F4B2))}${order.totalPrice}
                    """

        if (order.discountedPrice != null) {
            pricesMessage = """
                        #Total del pedido sin descuento: ${String(Character.toChars(0x1F4B2))}${order.totalPrice}
                        #
                        #Total del pedido con descuento aplicado: ${String(Character.toChars(0x1F4B2))}${order.discountedPrice}
                    """
        }

        return pricesMessage
    }

    private fun getCouponMessageIfApplies(): String {
        var couponMessage = ""

        if (order.appliedCouponId != null) {
            val coupon = couponRepository.searchById(order.appliedCouponId!!)

            couponMessage = """
                        #Cupon de descuento aplicado:
                        #${String(Character.toChars(0x1F4CC))}${coupon.name}
                        #${coupon.discount!!.times(100).toInt()}% de descuento
                        #${buildCouponValidForMessage(coupon)}
                        #
                    """
        }

        return couponMessage
    }

    private fun sendMessageByWhatsApp(messageToSend: String, marketNumber: String) {
        val whatsAppIntent = Intent(Intent.ACTION_SEND)
        whatsAppIntent.type = "text/plain"
        whatsAppIntent.putExtra(Intent.EXTRA_TEXT, messageToSend)
        whatsAppIntent.putExtra("jid", "$marketNumber@s.whatsapp.net")
        whatsAppIntent.setPackage("com.whatsapp")
        startActivity(whatsAppIntent)
    }

    private fun formatMarketNumber(marketNumber: String): String {
        return marketNumber.replace("+", "").replace(" ", "")
    }

    private fun getProducts(): String {
        var productsMessage = ""
        order.products!!.forEach { (key,value) ->
            val product = selectedProductsMap[key]
            val checkUnicode : Int = 0x2705
            val checkIcon = String(Character.toChars(checkUnicode))
            val productToAddToProductList = "$checkIcon$value x ${product!!.name} | $${product.price}\n"
            productsMessage = productsMessage.plus(productToAddToProductList)
        }
        return productsMessage
    }
}