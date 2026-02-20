package com.clipsaver.quickreels.utils

object Util {

    fun extractVideoQuality(input: String): String {
        val trimmed = input.trim().lowercase()

        // Case 1: Already standard quality (e.g. "1080p", "240p")
        val directMatch = Regex("""\b(\d{3,4}p)\b""").find(trimmed)
        if (directMatch != null) {
            return directMatch.value
        }

        // Case 2: Format like "1080x1920p"
        val resolutionMatch = Regex("""(\d{3,4})x\d{3,4}p""").find(trimmed)
        if (resolutionMatch != null) {
            val height = resolutionMatch.groupValues[1]
            return "${height}p"
        }

        // Case 3: Contains HD / FHD / UHD etc.
        if ("uhd" in trimmed) return "UHD"
        if ("fhd" in trimmed) return "FHD"
        if ("hd" in trimmed) return "HD"

        // If no known patterns found, return original string
        return input
    }

    fun formatDuration(durationInSeconds: Double): String {
        val totalSeconds = durationInSeconds.toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        val m = minutes.toString().padStart(2, '0')
        val s = seconds.toString().padStart(2, '0')

        return "$m:$s"
    }

    fun formatCount(count: Long): String {
        return when {
            count >= 1_000_000_000 -> String.format("%.1fB", count / 1_000_000_000.0)
            count >= 1_000_000 -> {
                val million = count / 1_000_000.0
                if (million >= 100) "${million.toInt()}M" else String.format("%.1fM", million)
            }
            count >= 1_000 -> {
                val thousand = count / 1_000.0
                if (thousand >= 100) "${thousand.toInt()}K" else String.format("%.1fK", thousand)
            }
            else -> count.toString()
        }
    }
}
