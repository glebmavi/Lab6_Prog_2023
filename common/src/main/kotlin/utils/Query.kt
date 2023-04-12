package utils

import kotlinx.serialization.Serializable

@Serializable
data class Query (val queryType: QueryType, val info: String, val args: Map<String, String>)
