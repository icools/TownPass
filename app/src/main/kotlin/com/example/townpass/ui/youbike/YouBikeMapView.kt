package com.example.townpass.ui.youbike

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.townpass.data.model.YouBikeStation
import org.json.JSONArray
import org.json.JSONObject

/**
 * Interactive Leaflet + OpenStreetMap component for YouBike 2.0 stations in Taipei.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouBikeMapView(
    stations: List<YouBikeStation>,
    selectedStation: YouBikeStation?,
    onStationClick: (YouBikeStation) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            webViewClient = object : WebViewClient() {}
        }
    }

    // JS Bridge class to communicate click events from Leaflet markers to Compose
    class MapJsBridge {
        @JavascriptInterface
        fun onMarkerSelected(sno: String) {
            val found = stations.find { it.sno == sno } ?: return
            webView.post {
                onStationClick(found)
            }
        }
    }

    LaunchedEffect(Unit) {
        webView.addJavascriptInterface(MapJsBridge(), "AndroidMapBridge")
        val html = generateLeafletMapHtml(stations, selectedStation)
        webView.loadDataWithBaseURL("https://www.openstreetmap.org", html, "text/html", "UTF-8", null)
    }

    // Update markers when stations or selection change
    LaunchedEffect(stations, selectedStation) {
        if (stations.isNotEmpty()) {
            val jsonArray = JSONArray()
            stations.take(200).forEach { st ->
                val obj = JSONObject().apply {
                    put("sno", st.sno)
                    put("name", st.name)
                    put("lat", st.latitude)
                    put("lng", st.longitude)
                    put("avail", st.availableBikes)
                    put("empty", st.emptySpaces)
                    put("total", st.totalSpaces)
                    put("selected", st.sno == selectedStation?.sno)
                }
                jsonArray.put(obj)
            }
            val script = "if (window.renderMarkers) { window.renderMarkers(${jsonArray.toString()}); }"
            webView.evaluateJavascript(script, null)

            if (selectedStation != null) {
                val focusScript = "if (window.centerOn) { window.centerOn(${selectedStation.latitude}, ${selectedStation.longitude}); }"
                webView.evaluateJavascript(focusScript, null)
            }
        }
    }

    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize()
    )
}

/**
 * Builds the self-contained interactive Leaflet map HTML.
 */
private fun generateLeafletMapHtml(
    initialStations: List<YouBikeStation>,
    initialSelected: YouBikeStation?
): String {
    val centerLat = initialSelected?.latitude ?: 25.0375
    val centerLng = initialSelected?.longitude ?: 121.5637

    return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
        body, html, #map { margin: 0; padding: 0; width: 100%; height: 100%; background: #F3F4F6; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
        .station-pin {
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            color: #ffffff;
            font-weight: bold;
            font-size: 11px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.3);
            border: 2px solid #ffffff;
            cursor: pointer;
            transition: transform 0.2s;
        }
        .pin-normal { background: #10B981; }
        .pin-low { background: #F59E0B; }
        .pin-empty { background: #EF4444; }
        .pin-selected { border: 3px solid #24B3A8; transform: scale(1.25); z-index: 1000 !important; }
        .leaflet-popup-content-wrapper { border-radius: 12px; }
    </style>
</head>
<body>
    <div id="map"></div>
    <script>
        var map = L.map('map', { zoomControl: false }).setView([$centerLat, $centerLng], 15);
        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '© OpenStreetMap'
        }).addTo(map);

        var markersLayer = L.layerGroup().addTo(map);

        function createPinIcon(avail, isSelected) {
            var colorClass = avail === 0 ? 'pin-empty' : (avail <= 3 ? 'pin-low' : 'pin-normal');
            var selectedClass = isSelected ? ' pin-selected' : '';
            return L.divIcon({
                className: 'station-pin ' + colorClass + selectedClass,
                html: '<span>' + avail + '</span>',
                iconSize: [28, 28],
                iconAnchor: [14, 14]
            });
        }

        window.renderMarkers = function(stations) {
            markersLayer.clearLayers();
            stations.forEach(function(st) {
                var icon = createPinIcon(st.avail, st.selected);
                var marker = L.marker([st.lat, st.lng], { icon: icon });
                marker.on('click', function() {
                    if (window.AndroidMapBridge) {
                        window.AndroidMapBridge.onMarkerSelected(st.sno);
                    }
                });
                marker.bindTooltip(st.name + ' (可借:' + st.avail + ' / 可還:' + st.empty + ')', { offset: [0, -14] });
                markersLayer.addLayer(marker);
            });
        };

        window.centerOn = function(lat, lng) {
            map.flyTo([lat, lng], 16, { animate: true, duration: 0.8 });
        };
    </script>
</body>
</html>
""".trimIndent()
}
