package com.example.aymobiledigitallibrary.util

import org.json.JSONObject

object ExportValidation {
    fun isValidJson(json: String): Boolean = runCatching { JSONObject(json); true }.getOrDefault(false)
    fun isValidCsv(csv: String): Boolean = csv.isNotBlank() && csv.contains("SESSION_INFO") && csv.contains("SUMMARY_METRICS")
}
