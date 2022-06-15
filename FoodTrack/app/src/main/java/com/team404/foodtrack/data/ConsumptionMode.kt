package com.team404.foodtrack.data

class ConsumptionMode(
    val id: Long?,
    val name: String?,
    val consumptionModeImg: String?) {

    data class Builder(
        var id: Long? = null,
        var name: String? = null,
        var consumptionModeImg: String? = null) {

        fun id(id: Long) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun consumptionModeImg(consumptionModeImg: String) = apply { this.consumptionModeImg = consumptionModeImg }
        fun build() = ConsumptionMode(id, name, consumptionModeImg)
    }
}