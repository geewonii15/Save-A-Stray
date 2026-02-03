package com.example.saveastray

import com.google.firebase.Timestamp

data class AdoptionApplication(
    var applicationId: String = "",
    var userEmail: String = "",
    var catName: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var status: String = "Pending"
)