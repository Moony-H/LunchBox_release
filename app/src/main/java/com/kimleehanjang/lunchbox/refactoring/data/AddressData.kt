package com.kimleehanjang.lunchbox.refactoring.data




data class AddressData (
    val meta:AddressMeta,
    val documents:List<AddressDocuments>
)

data class AddressMeta(
    val total_count:Int
)

data class AddressDocuments(
    val address:Address?,
    val road_address: RoadAddress?
)

data class Address(
    val address_name:String
)

data class RoadAddress(
    val address_name: String
)
