package com.joseangelmaneiro.lottery

enum class LotteryType(
    val tabIndex: Int,
    val tabTitle: String,
    val apiUrl: String,
    val firestoreFieldKey: String
) {
    NAVIDAD(
        tabIndex = 0,
        tabTitle = "NAVIDAD",
        apiUrl = "https://api-loteria.pabloclementeperez.com/output/LoteriaNavidad.json",
        firestoreFieldKey = "navidad"
    ),
    EL_NINO(
        tabIndex = 1,
        tabTitle = "EL NIÑO",
        apiUrl = "https://api-loteria.pabloclementeperez.com/output/LoteriaElNino.json",
        firestoreFieldKey = "elNino"
    )
}
