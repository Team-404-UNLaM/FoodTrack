package com.team404.foodtrack.domain.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.team404.foodtrack.R
import com.team404.foodtrack.data.PaymentMethod
import com.team404.foodtrack.databinding.ItemPaymentMethodBinding
class PaymentMethodAdapter (private val selectPaymentMethodClickListener: (PaymentMethod) -> Unit) :
    RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    private val paymentMethodList = mutableListOf<PaymentMethod>()
    private var selectedPaymentMethodId: Long? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val paymentMethodBinding = ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PaymentMethodViewHolder(paymentMethodBinding)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = paymentMethodList[position]

        holder.binding.paymentMethodName.text = paymentMethod.name

        val cardColor = if (selectedPaymentMethodId != null && paymentMethod.id == selectedPaymentMethodId) "#FFFFE9BB" else "#FFFFFFFF"

        holder.binding.paymentMethodArea.setBackgroundColor(Color.parseColor(cardColor))

        if (paymentMethod.paymentMethodImg != null) {
            Picasso.get()
                .load(paymentMethod.paymentMethodImg)
                .placeholder(R.drawable.ic_market)
                .error(R.drawable.ic_no_image)
                .into(holder.binding.paymentMethodImg)
        } else {
            holder.binding.paymentMethodImg.setImageResource(R.drawable.ic_market)
        }

        holder.binding.paymentMethodCard.setOnClickListener {
            selectPaymentMethodClickListener(paymentMethod)

            selectedPaymentMethodId = if (selectedPaymentMethodId != null && paymentMethod.id == selectedPaymentMethodId) null else paymentMethod.id!!
        }
    }

    override fun getItemCount(): Int {
        return paymentMethodList.size
    }

    fun updatePaymentMethodList(results: List<PaymentMethod>?, paymentMethodId: Long?) {
        paymentMethodList.clear()
        if(results != null) {
            paymentMethodList.addAll(results)
            selectedPaymentMethodId = paymentMethodId
        }
    }

    class PaymentMethodViewHolder (val binding: ItemPaymentMethodBinding) : RecyclerView.ViewHolder(binding.root)

}