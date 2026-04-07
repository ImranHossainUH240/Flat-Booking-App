package com.example.flatbookingapp.utils

import com.example.flatbookingapp.models.Property

object MatchUtils {

    fun calculateMatchScore(
        property: Property,
        budget: Int,
        maxUniDistance: Double,
        maxWorkDistance: Double,
        maxCommuteTime: Int,
        maxTransportCost: Double,
        requireFlexibleLease: Boolean,
        requireVisaCompatible: Boolean,
        requireStudySpace: Boolean
    ): Int {
        var score = 0

        if (property.rent <= budget) score += 30

        if (property.distanceToUniversity <= maxUniDistance) score += 20
        else if (property.distanceToUniversity <= maxUniDistance + 2) score += 10

        if (property.distanceToWork <= maxWorkDistance) score += 20
        else if (property.distanceToWork <= maxWorkDistance + 2) score += 10

        if (property.commuteTime <= maxCommuteTime) score += 15
        else if (property.commuteTime <= maxCommuteTime + 10) score += 8

        if (property.transportCost <= maxTransportCost) score += 10
        else if (property.transportCost <= maxTransportCost + 3) score += 5

        if (requireFlexibleLease && property.flexibleLease) score += 2
        if (requireVisaCompatible && property.visaCompatible) score += 2
        if (requireStudySpace && property.studySpace) score += 1

        return score
    }

    fun filterAndSortProperties(
        properties: List<Property>,
        budget: Int,
        maxUniDistance: Double,
        maxWorkDistance: Double,
        maxCommuteTime: Int,
        maxTransportCost: Double,
        requireFlexibleLease: Boolean,
        requireVisaCompatible: Boolean,
        requireStudySpace: Boolean
    ): List<Property> {

        val filtered = properties.filter { property ->
            property.rent <= budget &&
                    property.distanceToUniversity <= maxUniDistance &&
                    property.distanceToWork <= maxWorkDistance &&
                    property.commuteTime <= maxCommuteTime &&
                    property.transportCost <= maxTransportCost &&
                    (!requireFlexibleLease || property.flexibleLease) &&
                    (!requireVisaCompatible || property.visaCompatible) &&
                    (!requireStudySpace || property.studySpace)
        }

        return filtered.sortedByDescending {
            calculateMatchScore(
                it,
                budget,
                maxUniDistance,
                maxWorkDistance,
                maxCommuteTime,
                maxTransportCost,
                requireFlexibleLease,
                requireVisaCompatible,
                requireStudySpace
            )
        }
    }
}