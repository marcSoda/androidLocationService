package com.example.locationservice.models


data class Places(val error_message: String?,
                  val html_attributions: List<String>,
                  val results: List<Result>,
                  val status: String)

data class Result(val geometry: QuerySearchGeometryInfo,
                  val formatted_address: String,
                  val icon: String,
                  val id: String,
                  val name: String,
                  val opening_hours: QuerySearchOpeningInfo,
                  val photos: List<QuerySearchPhotosInfo>,
                  val place_id: String,
                  val scope: String,
                  val alt_ids: List<QuerySearchAlteranteIdInfo>,
                  val reference: String,
                  val types: List<String>,
                  val vicinity: String)

data class QuerySearchGeometryInfo(val location: QuerySearchLocationInfo)

data class QuerySearchLocationInfo(val lat: Double, val lng: Double)

data class QuerySearchOpeningInfo(val open_now: Boolean)


data class QuerySearchPhotosInfo(val height: Int,
                                 val html_attributions: List<String>,
                                 val photo_reference: String,
                                 val width: Int)

data class QuerySearchAlteranteIdInfo(val place_id: String, val scope: String)

